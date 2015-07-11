package ch.quickorder.model;

import ch.quickorder.entities.User;
import com.clusterpoint.api.request.CPSPartialReplaceRequest;
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
        }
    }

    public Collection<User> getUsers() {

        return getUsersWithQuery( "*");
    }

    public User getUserById( String id) {

        return getFirstOrNull(getUsersWithQuery("<id>" + id + "</id>"));
    }

    public User getUserForOrder( String orderId) {

        String query = "<orders><id>" + orderId + "</id></orders>";

        return getFirstOrNull(getUsersWithQuery(query));
    }

    // Must be called within an transaction context!
    public boolean addOrderToUser( String id, String orderId) {

        try {
            User user = getUserById(id);
            user.getOrders().add(orderId);

            Document doc = documentBuilder.newDocument();
            userMarshaller.marshal(user, doc);

            CPSPartialReplaceRequest partialReplaceRequest = new CPSPartialReplaceRequest(doc);
            CPSModifyResponse updateResponse = (CPSModifyResponse) cpsConnection.sendRequest(partialReplaceRequest);
        } catch (Exception e) {
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

            CPSSearchRequest search_req = new  CPSSearchRequest(query, 0, 200, attributesList);
            CPSSearchResponse searchResponse = (CPSSearchResponse) cpsConnection.sendRequest(search_req);

            if ((searchResponse.getDocuments() == null) ||  (searchResponse.getDocuments().isEmpty())) {
                return null;
            }

            Iterator<Element> iterator = searchResponse.getDocuments().iterator();

            while (iterator.hasNext()) {
                User user = (User) userUnmarshaller.unmarshal(iterator.next());
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return userList;
    }

    private User getFirstOrNull(Collection< User> users) {

        if( users.isEmpty()) {
            return null;
        }

        return users.iterator().next();
    }
}
