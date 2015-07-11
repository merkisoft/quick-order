package ch.quickorder.model;

import ch.quickorder.entities.Order;
import com.clusterpoint.api.CPSResponse;
import com.clusterpoint.api.request.*;
import com.clusterpoint.api.response.CPSSearchResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;

public class OrdersModel extends CpsBasedModel {
    private static OrdersModel ourInstance = new OrdersModel();

    public static OrdersModel getInstance() {
        return ourInstance;
    }

    private OrdersModel() {
        super("Orders");
    }

    public Collection<Order> getOrders() {

        return getOrdersWithQuery( "*");
    }

    public Order getOrderById(String id) {

        return getFirstOrNull(getOrdersWithQuery("<id>" + id + "</id>"));
    }

    public Order createOrder(String userId, Order order) {

        // Fill in missing order details
        order.setId( UUID.randomUUID().toString());
        order.setTimestamp( System.currentTimeMillis());

        try {
            // Begin transaction
            CPSBeginTransactionRequest beginTransactionRequest = new CPSBeginTransactionRequest();
            cpsConnection.sendRequest(beginTransactionRequest);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            // Insert order
            {
                JAXBContext context = JAXBContext.newInstance(Order.class);
                Marshaller marshaller = context.createMarshaller();

                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.newDocument();
                marshaller.marshal(order, doc);

                CPSInsertRequest insertRequest = new CPSInsertRequest(doc);
                CPSResponse response = cpsConnection.sendRequest(insertRequest);
            }

            // Update user
            UsersModel.getInstance().addOrderToUser(userId, order.getId());

            // End transaction
            CPSCommitTransactionRequest commitTransactionRequest = new CPSCommitTransactionRequest();
            cpsConnection.sendRequest(commitTransactionRequest);
        } catch (Exception e) {
            return null;
        }

        return order;
    }

    private Collection<Order> getOrdersWithQuery(String query) {

        List<Order> orderList = new ArrayList<>();

        try {
            Map<String, String> attributesList = new HashMap<>();
            attributesList.put("id", "yes");
            attributesList.put("restaurant", "yes");
            attributesList.put("items", "yes");
            attributesList.put("timestamp", "yes");
            attributesList.put("ticketNumber", "yes");
            attributesList.put("status", "yes");

            CPSSearchRequest search_req = new  CPSSearchRequest(query, 0, 200, attributesList);
            CPSSearchResponse searchResponse = (CPSSearchResponse) cpsConnection.sendRequest(search_req);

            if (searchResponse.getHits() > 0) {
                List<Element> results = searchResponse.getDocuments();
                Iterator<Element> it = results.iterator();

                JAXBContext context = JAXBContext.newInstance(Order.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());

                while (it.hasNext()) {
                    Order order = (Order) unmarshaller.unmarshal(it.next());
                    orderList.add(order);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderList;
    }

    private Order getFirstOrNull(Collection< Order> orders) {

        if( orders.isEmpty()) {
            return null;
        }

        return orders.iterator().next();
    }
}
