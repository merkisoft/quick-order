package ch.quickorder.model;

import ch.quickorder.entities.Product;
import ch.quickorder.entities.ProductGroup;
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
import java.math.BigDecimal;
import java.util.*;

public class ProductsModel extends CpsBasedModel {

    private static ProductsModel instance;

    public static ProductsModel getInstance() {
        if (instance == null) {
            instance = new ProductsModel();
        }

        return instance;
    }

    private Marshaller productMarshaller;
    private Unmarshaller productUnmarshaller;

    public ProductsModel() {
        super("Products");

        try {
            JAXBContext context = JAXBContext.newInstance(Product.class);
            productMarshaller = context.createMarshaller();
            productUnmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
        }
    }

    public Collection<Product> getProducts() {

        return getProductsWithQuery("*");
    }

    public Product getProductById( String id) {

        return getFirstOrNull(getProductsWithQuery("<id>" + id + "</id>"));
    }

    public Collection<Product> getProductsByName(String name) {
        return getProductsWithQuery("<name>" + name + "</name>");
    }

    public List<Product> getProductsForRestaurant(String restaurant) {

        String query = "<restaurant>" + restaurant + "</restaurant>";
        return getProductsWithQuery(query);
    }

    public boolean updateProductPrice(String id, BigDecimal price) {

        Product product = new Product();
        product.setId( id);
        product.setPrice( price);

        try {
            Document doc = documentBuilder.newDocument();
            productMarshaller.marshal(product, doc);

            CPSPartialReplaceRequest partialReplaceRequest = new CPSPartialReplaceRequest(doc);
            cpsConnection.sendRequest(partialReplaceRequest);
        } catch (Exception e) {
            return false;
        }

        return true;
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

                while (it.hasNext()) {
                    Product product = (Product) productUnmarshaller.unmarshal(it.next());
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

            ProductGroup productGroup = new ProductGroup(product.getCategory());
            productGroup = productGroupMap.get( productGroup.getName());

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
