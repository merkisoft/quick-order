package ch.quickorder.model;

import ch.quickorder.entities.Order;
import ch.quickorder.entities.User;
import ch.quickorder.util.OrderStatus;
import com.clusterpoint.api.request.CPSPartialReplaceRequest;
import com.clusterpoint.api.request.CPSReplaceRequest;
import com.clusterpoint.api.request.CPSSearchRequest;
import com.clusterpoint.api.response.CPSModifyResponse;
import com.clusterpoint.api.response.CPSSearchResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.*;

public class UsersModel extends CpsBasedModel {

    private static UsersModel ourInstance = new UsersModel();

    public static UsersModel getInstance() {
        return ourInstance;
    }

    private Marshaller userMarshaller;
    private Unmarshaller userUnmarshaller;

    private UsersModel() {
        super("Users");

        try {
            JAXBContext context = JAXBContext.newInstance(User.class);
            userMarshaller = context.createMarshaller();
            userUnmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            System.err.println( currentTime() + "Unable to create marshaller/unmarshaller: " + e.getMessage());
        }
    }

    public Collection<User> getUsers() {

        return getUsersWithQuery("*");
    }

    public User getUserById( String id) {

        return getFirstOrNull(getUsersWithQuery("<id>" + id + "</id>"));
    }

    public boolean deleteOrderFromUser( String id) {

        String query = "<orders><id>" + id + "</id></orders>";

        User user = getFirstOrNull(getUsersWithQuery(query));

        if (user == null) {
            return false;
        }

        Iterator< String> orderIterator = user.getOrders().iterator();

        while (orderIterator.hasNext()) {
            String orderId = orderIterator.next();

            if (orderId.equals(id)) {
                orderIterator.remove();
                break;
            }
        }

        try {
            Document doc = documentBuilder.newDocument();
            userMarshaller.marshal(user, doc);

            cpsConnection.sendRequest( new CPSPartialReplaceRequest(doc));
        } catch (Exception e) {
            System.err.println( "Unable to delete order from user: " + e);
            return false;
        }

        return true;
    }

    public Collection<Order> getOpenOrdersForUser( String id) {

        System.out.println( currentTime() + "Get open orders for user " + id);

        User user = UsersModel.getInstance().getUserById(id);

        if (user == null) {
            return null;
        }

        System.out.println( currentTime() + "User found");

        Collection< Order> orderList = new ArrayList<>();

        Iterator< String> orderIterator = user.getOrders().iterator();

        boolean userChanged = false;

        while (orderIterator.hasNext()) {
            String orderId = orderIterator.next();

            Order order = OrdersModel.getInstance().getOrderById(orderId);

            if (order == null) {
                orderIterator.remove();
                userChanged = true;
                continue;
            }

            if (order.getStatus().equals( OrderStatus.Ordered.name())
                    || order.getStatus().equals( OrderStatus.Paid.name())) {
                orderList.add(order);
            }
        }

        // Write back user object if it has changed
        if (userChanged) {
            try {
                Document doc = documentBuilder.newDocument();
                userMarshaller.marshal(user, doc);

                CPSModifyResponse updateResponse = (CPSModifyResponse) cpsConnection.sendRequest(new CPSReplaceRequest(doc));
                System.out.println(currentTime() + "User replaced in " + updateResponse.getSeconds());
            } catch (Exception e) {
                System.err.println( currentTime() + "Unable to update user entry" + e);
            }
        }

        System.out.println( currentTime() + "Returning list of ordersfor user");
        return orderList;
    }

    // Must be called within an transaction context!
    public boolean addOrderToUser( String id, String orderId) {

        try {
            System.out.println( currentTime() + "Adding order to user");

            User user = getUserById(id);

            if (user == null) {
                return false;
            }

            user.getOrders().add(orderId);

            Document doc = documentBuilder.newDocument();
            userMarshaller.marshal(user, doc);

            CPSPartialReplaceRequest partialReplaceRequest = new CPSPartialReplaceRequest(doc);
            CPSModifyResponse updateResponse = (CPSModifyResponse) cpsConnection.sendRequest(partialReplaceRequest);

            System.out.println( currentTime() + "Order added to user in " + updateResponse.getSeconds());
        } catch (Exception e) {
            System.err.println( currentTime() + "Unable to add order to user: " + e);
            return false;
        }

        return true;
    }

    private List<User> getUsersWithQuery(String query) {

        List<User> userList = new ArrayList<>();

        try {
            Map<String, String> attributesList = new HashMap<>();
            attributesList.put("id", "yes");
            attributesList.put("firstName", "yes");
            attributesList.put("lastName", "yes");
            attributesList.put("orders", "yes");

            System.out.println(currentTime() + "Starting users query");

            CPSSearchRequest search_req = new  CPSSearchRequest(query, 0, 200, attributesList);
            CPSSearchResponse searchResponse = (CPSSearchResponse) cpsConnection.sendRequest(search_req);

            if (( searchResponse == null) || (searchResponse.getHits() == 0)) {
                System.err.println( currentTime() + "Unable to query users");
                return null;
            }

            System.out.println( currentTime() + "Users query finished in " + searchResponse.getSeconds());

            Iterator<Element> iterator = searchResponse.getDocuments().iterator();

            while (iterator.hasNext()) {
                User user = (User) userUnmarshaller.unmarshal(iterator.next());
                userList.add(user);
            }
        } catch (Exception e) {
            System.err.println(currentTime() + "Unable to query users: " + e.getMessage());
            return null;
        }

        return userList;
    }

    private User getFirstOrNull(Collection< User> users) {

        if(( users == null) || users.isEmpty()) {
            return null;
        }

        return users.iterator().next();
    }
}
