package ch.quickorder.model;

import ch.quickorder.entities.Product;
import ch.quickorder.entities.Restaurant;
import com.clusterpoint.api.request.CPSSearchRequest;
import com.clusterpoint.api.response.CPSSearchResponse;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.*;

public class RestaurantsModel extends CpsBasedModel {

    private static RestaurantsModel ourInstance = new RestaurantsModel();

    public static RestaurantsModel getInstance() {
        return ourInstance;
    }

    private RestaurantsModel() {
        super("Restaurants");
    }

    public Collection< Restaurant> getRestaurantsByName( String name) {

        String query = "<name>" + name + "</name>";
        return getRestaurantByQuery( query);
    }

    public Restaurant getRestaurantById( String id) {

        return getFirstOrNull(getRestaurantByQuery("<id>" + id + "</id>"));
    }

    public Collection< Restaurant> getRestaurantsByCity( String city) {

        String query = "<city>" + city + "</city>";
        return getRestaurantByQuery( query);
    }

    public Collection< Restaurant> getRestaurantByQuery( String query) {

        List< Restaurant> restaurantList = new ArrayList<>();

        try {
            Map<String, String> attributesList = new HashMap<>();
            attributesList.put("id", "yes");
            attributesList.put("name", "yes");
            attributesList.put("city", "yes");

            CPSSearchRequest searchRequest = null;

            searchRequest = new CPSSearchRequest( query, 0, 200, attributesList);
            CPSSearchResponse searchResponse = (CPSSearchResponse) cpsConnection.sendRequest(searchRequest);

            if (searchResponse.getDocuments().isEmpty() == false) {

                List<Element> results = searchResponse.getDocuments();
                Iterator<Element> it = results.iterator();

                JAXBContext context = JAXBContext.newInstance(Product.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                while (it.hasNext()) {
                    Restaurant restaurant = (Restaurant) unmarshaller.unmarshal(it.next());
                    restaurantList.add( restaurant);
                }
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
