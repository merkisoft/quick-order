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
            cpsConnection = ClusterPointConnection.getInstance().getConnection( "Products");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Collection<Product> getProducts() {

        return getProductsWithQuery("*");
    }

    public Product getProductById( String id) {

        return getFirstOrNull(getProductsWithQuery("<id>" + id + "</id>"));
    }

    public Collection<Product> getProductsByName(String name) {
        return getProductsWithQuery( "<name>" + name + "</name>");
    }

    public List<Product> getProductsForRestaurant(String restaurant) {

        String query = "<restaurant>" + restaurant + "</restaurant>";
        return getProductsWithQuery(query);
    }

    private List<Product> getProductsWithQuery(String query) {

        List< Product> productList = new ArrayList<>();

        try {
            Map<String, String> attributesList = new HashMap<>();
            attributesList.put("id", "yes");
            attributesList.put("name", "yes");
            attributesList.put("category", "yes");
            attributesList.put("restaurant", "yes");
            attributesList.put("price", "yes");

            CPSSearchRequest search_req = new  CPSSearchRequest(query, 0, 200, attributesList);
            CPSSearchResponse searchResponse = (CPSSearchResponse) cpsConnection.sendRequest(search_req);

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

        List< ProductGroup> sortedGroups = new ArrayList<>( productGroupMap.values());
        sortedGroups.sort(new Comparator<ProductGroup>() {
            @Override
            public int compare(ProductGroup o1, ProductGroup o2) {
                return o1.getPosition() - o2.getPosition();
            }
        });

        return sortedGroups;
    }

    private Product getFirstOrNull(Collection<Product> products) {

        if( products.isEmpty()) {
            return null;
        }

        return products.iterator().next();
    }
}
