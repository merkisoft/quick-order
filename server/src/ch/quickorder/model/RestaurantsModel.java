package ch.quickorder.model;

import ch.quickorder.entities.Restaurant;
import com.clusterpoint.api.request.CPSPartialReplaceRequest;
import com.clusterpoint.api.request.CPSSearchRequest;
import com.clusterpoint.api.response.CPSSearchResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.*;

public class RestaurantsModel extends CpsBasedModel {

    private static RestaurantsModel ourInstance = new RestaurantsModel();

    public static RestaurantsModel getInstance() {
        return ourInstance;
    }

    private Marshaller restaurantMarshaller;
    private Unmarshaller restaurantUnmarshaller;

    private RestaurantsModel() {
        super("Restaurants");

        try {
            JAXBContext context = JAXBContext.newInstance(Restaurant.class);
            restaurantMarshaller = context.createMarshaller();
            restaurantUnmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
        }
    }

    public Collection< Restaurant> getRestaurantsByName( String name) {

        String query = "<name>" + name + "</name>";
        return getRestaurantByQuery(query);
    }

    public Restaurant getRestaurantById( String id) {

        Collection< Restaurant> restaurants = getRestaurantByQuery("<id>" + id + "</id>");

        if (restaurants == null) {
            System.err.println( currentTime() + "Can't look up restaurant " + id);
            return null;
        }

        return getFirstOrNull(restaurants);
    }

    public Collection< Restaurant> getRestaurantsByCity( String city) {

        String query = "<city>" + city + "</city>";
        return getRestaurantByQuery(query);
    }

    // Must be called within an transaction context!
    public int getTicketNumber(String id) {

        try {
            // Get restaurant
            Restaurant restaurant = getRestaurantById(id);

            if (restaurant == null) {
                return -1;
            }

            // Increase order count
            restaurant.setOrderCount(restaurant.getOrderCount() + 1);

            // Save data back to the DB
            Document doc = documentBuilder.newDocument();
            restaurantMarshaller.marshal(restaurant, doc);

            CPSPartialReplaceRequest partialReplaceRequest = new CPSPartialReplaceRequest(doc);
            cpsConnection.sendRequest(partialReplaceRequest);

            // Return the ticker number (1...100)
            return restaurant.getOrderCount() % 100 + 1;
        } catch (Exception e) {
            return -1;
        }
    }

    public Collection< Restaurant> getRestaurantByQuery( String query) {

        List< Restaurant> restaurantList = new ArrayList<>();

        try {
            Map<String, String> attributesList = new HashMap<>();
            attributesList.put("id", "yes");
            attributesList.put("name", "yes");
            attributesList.put("city", "yes");
            attributesList.put("latitude", "yes");
            attributesList.put("longitude", "yes");

            CPSSearchRequest searchRequest = new CPSSearchRequest( query, 0, 200, attributesList);
            CPSSearchResponse searchResponse = (CPSSearchResponse) cpsConnection.sendRequest(searchRequest);

            if (( searchResponse == null) || (searchResponse.getDocuments() == null) ||  (searchResponse.getDocuments().isEmpty())) {
                System.err.println( currentTime() + "Unable to query restaurants");
                return null;
            }

            Iterator<Element> iterator = searchResponse.getDocuments().iterator();

            while (iterator.hasNext()) {
                Restaurant restaurant = (Restaurant) restaurantUnmarshaller.unmarshal(iterator.next());
                restaurantList.add( restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return restaurantList;
    }

    private Restaurant getFirstOrNull(Collection<Restaurant> restaurants) {

        if( restaurants.isEmpty()) {
            return null;
        }

        return restaurants.iterator().next();
    }

}
