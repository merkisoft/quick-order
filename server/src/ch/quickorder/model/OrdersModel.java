package ch.quickorder.model;

import ch.quickorder.entities.Order;
import ch.quickorder.util.OrderStatus;
import com.clusterpoint.api.request.*;
import com.clusterpoint.api.response.CPSBeginTransactionResponse;
import com.clusterpoint.api.response.CPSModifyResponse;
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

        Collection<Order> orders = getOrdersWithQuery("<id>" + id + "</id>");

        if (orders == null) {
            System.err.println( currentTime() + "Can't look up order " + id);
            return null;
        }

        return getFirstOrNull(orders);
    }

    public boolean deleteOrderById(String id, String userId) {

        try {
            System.out.println( currentTime() + "Starting create delete transaction");

            // Begin transaction
            CPSBeginTransactionRequest beginTransactionRequest = new CPSBeginTransactionRequest();
            CPSBeginTransactionResponse beginTransactionResponse = (CPSBeginTransactionResponse) cpsConnection.sendRequest(beginTransactionRequest);

            System.err.println( currentTime() + "Transaction started in " + beginTransactionResponse.getSeconds());

            // Find and update user
            if (UsersModel.getInstance().deleteOrderFromUser( id) == false) {
                System.err.println( currentTime() + "Unable to delete order from user");
                return false;
            }

            // Delete order
            CPSDeleteRequest deleteRequest = new CPSDeleteRequest(id);
            CPSModifyResponse modifyResponse = (CPSModifyResponse) cpsConnection.sendRequest(deleteRequest);

            System.out.println( currentTime() + "Order deleted in " + modifyResponse.getSeconds());

            // End transaction
            CPSCommitTransactionRequest commitTransactionRequest = new CPSCommitTransactionRequest();
            cpsConnection.sendRequest(commitTransactionRequest);

            System.out.println(currentTime() + "Delete order transaction committed");
        } catch (Exception e) {
            System.err.println(currentTime() + "Unable to delete order: " + e.getMessage());
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

            System.out.println(currentTime() + "Marking order as paid");

            CPSPartialReplaceRequest partialReplaceRequest = new CPSPartialReplaceRequest(doc);
            CPSModifyResponse modifyResponse = (CPSModifyResponse) cpsConnection.sendRequest(partialReplaceRequest);

            System.out.println(currentTime() + " Order marked as paid in " + modifyResponse.getSeconds());
        } catch (Exception e) {
            System.err.println( currentTime() + "Unable mark order as paid");
            return false;
        }

        return true;
    }

    public Order createOrder(String userId, Order order) {

        try {
            System.out.println(currentTime() + "Starting create order transaction");

            // Begin transaction
            CPSBeginTransactionRequest beginTransactionRequest = new CPSBeginTransactionRequest();
            CPSBeginTransactionResponse beginTransactionResponse = (CPSBeginTransactionResponse) cpsConnection.sendRequest(beginTransactionRequest);

            System.out.println(currentTime() + "Transaction started in " + beginTransactionResponse.getSeconds());

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
                CPSModifyResponse modifyResponse = (CPSModifyResponse) cpsConnection.sendRequest(insertRequest);

                System.out.println(currentTime() + "Order inserted in " + modifyResponse.getSeconds());
            }

            // Update user
            UsersModel.getInstance().addOrderToUser(userId, order.getId());

            // End transaction
            CPSCommitTransactionRequest commitTransactionRequest = new CPSCommitTransactionRequest();
            cpsConnection.sendRequest(commitTransactionRequest);

            System.out.println(currentTime() + "Create order transaction committed");
        } catch (Exception e) {
            System.err.println(currentTime() + "Unable to create order: " + e.getMessage());
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

            System.out.println(currentTime() + "Starting orders query");

            CPSSearchRequest search_req = new  CPSSearchRequest(query, 0, 200, attributesList);
            CPSSearchResponse searchResponse = (CPSSearchResponse) cpsConnection.sendRequest(search_req);

            if (( searchResponse == null) || (searchResponse.getDocuments() == null) ||  (searchResponse.getDocuments().isEmpty())) {
                System.err.println( currentTime() + "Unable to query orders");
                return null;
            }

            System.out.println(currentTime() + "Users query finished in " + searchResponse.getSeconds());

            Iterator<Element> iterator = searchResponse.getDocuments().iterator();

            while (iterator.hasNext()) {
                Order order = (Order) orderUnmarshaller.unmarshal(iterator.next());
                orderList.add(order);
            }
        } catch (Exception e) {
            System.err.println(currentTime() + "Unable to query orders: " + e.getMessage());
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
