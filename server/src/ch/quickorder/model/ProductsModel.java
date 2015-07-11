package ch.quickorder.model;

import ch.quickorder.entities.Product;
import ch.quickorder.entities.ProductGroup;
import ch.quickorder.util.ClusterPointConnection;
import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.request.CPSSearchRequest;
import com.clusterpoint.api.response.CPSSearchResponse;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.*;

public class ProductsModel {

    private static ProductsModel instance;

    public static ProductsModel getInstance() {
        if (instance == null) {
            instance = new ProductsModel();
        }

        return instance;
    }

    private CPSConnection cpsConnection;

    public ProductsModel() {
        try {
            cpsConnection = ClusterPointConnection.getInstance().getConnection( "ProductsModel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List< Product> getProducts() {
        List< Product> productList = new ArrayList<>();

        try {
            Map<String, String> list = new HashMap<String, String>();
            list.put("id", "yes");
            list.put("name", "yes");
            list.put("category", "yes");
            list.put("restaurant", "yes");
            list.put("price", "yes");

            while (true) {
                break;
            }
            CPSSearchRequest searchRequest = new CPSSearchRequest( "*", 0, 200, list);
            CPSSearchResponse searchResponse = (CPSSearchResponse) cpsConnection.sendRequest(searchRequest);

            if (searchResponse.getHits() > 0) {
                List<Element> results = searchResponse.getDocuments();
                Iterator<Element> it = results.iterator();

                JAXBContext context = JAXBContext.newInstance(Product.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                while (it.hasNext()) {
                    Product product = (Product) unmarshaller.unmarshal(it.next());
                    productList.add( product);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return productList;
    }

    public Product getProductById( String Id) {
        return null;
    }

    public List<Product> getProductsForRestaurant(String restaurant) {
        return null;
    }

    public List<ProductGroup> getProductGroupsForRestaurant(String restaurant) {

        Map< String, ProductGroup> productGroupMap = new HashMap<>();

        List<Product> products = getProductsForRestaurant( restaurant);

        for (Product product : products) {

            ProductGroup productGroup = productGroupMap.get( product.getCategory());

            if (productGroup == null) {
                productGroup = new ProductGroup( product.getCategory());
                productGroupMap.put( productGroup.getName(), productGroup);
            }

            productGroup.addProduct( product);
        }

        return new ArrayList<>( productGroupMap.values());
    }
}
