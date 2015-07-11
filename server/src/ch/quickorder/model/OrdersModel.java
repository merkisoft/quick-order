package ch.quickorder.model;

import ch.quickorder.entities.Order;
import ch.quickorder.entities.User;
import ch.quickorder.util.OrderStatus;
import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.request.*;
import com.clusterpoint.api.response.CPSSearchResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.*;

public class OrdersModel extends CpsBasedModel {
    private static OrdersModel ourInstance = new OrdersModel();

    public static OrdersModel getInstance() {
        return ourInstance;
    }

    private Marshaller orderMarshaller;
    private Unmarshaller orderUnmarshaller;

    private OrdersModel() {
        super("Orders");

        try {
            JAXBContext context = JAXBContext.newInstance(Order.class);
            orderMarshaller = context.createMarshaller();
            orderUnmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
        }
    }

    public Collection<Order> getOrders() {

        return getOrdersWithQuery("*");
    }

    public Order getOrderById(String id) {

        return getFirstOrNull(getOrdersWithQuery("<id>" + id + "</id>"));
    }

    public boolean deleteOrderById(String id, String userId) {

        try {
            // Begin transaction
            CPSBeginTransactionRequest beginTransactionRequest = new CPSBeginTransactionRequest();
            cpsConnection.sendRequest(beginTransactionRequest);

            // Find and update user
            if (UsersModel.getInstance().deleteOrderFromUser( id) == false) {
                return false;
            }

            // Delete order
            CPSDeleteRequest deleteRequest = new CPSDeleteRequest(id);
            cpsConnection.sendRequest(deleteRequest);

            // End transaction
            CPSCommitTransactionRequest commitTransactionRequest = new CPSCommitTransactionRequest();
            cpsConnection.sendRequest(commitTransactionRequest);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean markOrderAsPaid(String user, String id) {

        Order order = new Order();
        order.setId( id);
        order.setStatus(OrderStatus.Paid.name());

        try {
            Document doc = documentBuilder.newDocument();
            orderMarshaller.marshal(order, doc);

            CPSPartialReplaceRequest partialReplaceRequest = new CPSPartialReplaceRequest(doc);
            cpsConnection.sendRequest( partialReplaceRequest);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Order createOrder(String userId, Order order) {

        try {
            // Begin transaction
            CPSBeginTransactionRequest beginTransactionRequest = new CPSBeginTransactionRequest();
            cpsConnection.sendRequest(beginTransactionRequest);

            // Fill in missing order details
            order.setId(UUID.randomUUID().toString());
            order.setTimestamp(System.currentTimeMillis());
            order.setStatus(OrderStatus.Ordered.name());
            order.setTicketNumber(RestaurantsModel.getInstance().getTicketNumber(order.getRestaurant()));

            // Insert order
            {
                Document doc = documentBuilder.newDocument();
                orderMarshaller.marshal(order, doc);

                CPSInsertRequest insertRequest = new CPSInsertRequest(doc);
                cpsConnection.sendRequest(insertRequest);
            }

            // Update user
            UsersModel.getInstance().addOrderToUser(userId, order.getId());

            // End transaction
            CPSCommitTransactionRequest commitTransactionRequest = new CPSCommitTransactionRequest();
            cpsConnection.sendRequest(commitTransactionRequest);
        } catch (Exception e) {
            System.err.println( "Unable to create order: " + e.getMessage());
            return null;
        }

        return order;
    }

    private Collection<Order> getOrdersWithQuery(String query) {

        List<Order> orderList = new ArrayList<>();

        try {
            Map<String, String> attributesList = new HashMap<>();
            attributesList.put("id", "yes");
            attributesList.put("items", "yes");
            attributesList.put("restaurant", "yes");
            attributesList.put("status", "yes");
            attributesList.put("timestamp", "yes");
            attributesList.put("ticketNumber", "yes");

            CPSSearchRequest search_req = new  CPSSearchRequest(query, 0, 200, attributesList);
            CPSSearchResponse searchResponse = (CPSSearchResponse) cpsConnection.sendRequest(search_req);

            if (( searchResponse == null) || (searchResponse.getDocuments() == null) ||  (searchResponse.getDocuments().isEmpty())) {
                return null;
            }

            Iterator<Element> iterator = searchResponse.getDocuments().iterator();

            while (iterator.hasNext()) {
                Order order = (Order) orderUnmarshaller.unmarshal(iterator.next());
                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderList;
    }

    private Order getFirstOrNull(Collection< Order> orders) {

        if(( orders == null) || orders.isEmpty()) {
            return null;
        }

        return orders.iterator().next();
    }
}
