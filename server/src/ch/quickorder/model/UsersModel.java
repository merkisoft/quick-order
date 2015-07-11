package ch.quickorder.model;

import ch.quickorder.entities.Product;
import ch.quickorder.entities.User;
import ch.quickorder.service.Users;
import ch.quickorder.util.ClusterPointConnection;
import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.request.CPSSearchRequest;
import com.clusterpoint.api.response.CPSSearchResponse;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.*;

public class UsersModel {

    private static UsersModel ourInstance = new UsersModel();

    public static UsersModel getInstance() {
        return ourInstance;
    }

    private CPSConnection cpsConnection;

    private UsersModel() {
        try {
            cpsConnection = ClusterPointConnection.getInstance().getConnection( "Users");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Collection<User> getUsers() {

        return getUsersWithQuery( "*");
    }

    public User getUserById( String id) {

        return getFirstOrNull(getUsersWithQuery("<id>" + id + "</id>"));
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

            if (searchResponse.getHits() > 0) {
                List<Element> results = searchResponse.getDocuments();
                Iterator<Element> it = results.iterator();

                JAXBContext context = JAXBContext.newInstance(User.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                while (it.hasNext()) {
                    User user = (User) unmarshaller.unmarshal(it.next());
                    userList.add(user);
                }
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
