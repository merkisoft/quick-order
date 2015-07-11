package ch.quickorder.model;

import ch.quickorder.entities.Order;
import com.clusterpoint.api.request.CPSSearchRequest;
import com.clusterpoint.api.response.CPSSearchResponse;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
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

    public Order getUserById( String id) {

        return getFirstOrNull(getOrdersWithQuery("<id>" + id + "</id>"));
    }

    private Collection<Order> getOrdersWithQuery(String query) {

        List<Order> orderList = new ArrayList<>();

        try {
            Map<String, String> attributesList = new HashMap<>();
            attributesList.put("id", "yes");
            attributesList.put("restaurant", "yes");
            attributesList.put("products", "yes");

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
