package ch.quickorder.model;

import ch.quickorder.entities.Product;
import ch.quickorder.entities.ProductDetails;
import com.clusterpoint.api.request.CPSSearchRequest;
import com.clusterpoint.api.response.CPSSearchResponse;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.*;

public class ProductDetailsModel extends CpsBasedModel {
    private static ProductDetailsModel instance;

    public static ProductDetailsModel getInstance() {
        if (instance == null) {
            instance = new ProductDetailsModel();
        }

        return instance;
    }

    private Marshaller productDetailsMarshaller;
    private Unmarshaller productDetailsUnmarshaller;

    public ProductDetailsModel() {
        super("ProductDetails");

        try {
            JAXBContext context = JAXBContext.newInstance(ProductDetails.class);
            productDetailsMarshaller = context.createMarshaller();
            productDetailsUnmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
        }
    }

    public ProductDetails getDetailsForProduct( String productId) {

        return getFirstOrNull(getProductDetailsWithQuery("<product>" + productId + "</product>"));
    }

    private List<ProductDetails> getProductDetailsWithQuery(String query) {

        List< ProductDetails> productDetailsList = new ArrayList<>();

        try {
            Map<String, String> attributesList = new HashMap<>();
            attributesList.put("id", "yes");
            attributesList.put("product", "yes");
            attributesList.put("descriptions", "yes");
            attributesList.put("ingredients", "yes");
            attributesList.put("types", "yes");
            attributesList.put("language", "yes");

            System.out.println( currentTime() + "Starting product details query");

            CPSSearchRequest search_req = new  CPSSearchRequest(query, 0, 200, attributesList);
            CPSSearchResponse searchResponse = (CPSSearchResponse) cpsConnection.sendRequest(search_req);

            if (( searchResponse == null) || (searchResponse.getDocuments() == null) ||  (searchResponse.getDocuments().isEmpty())) {
                System.err.println( currentTime() + "Unable to query product details");
                return null;
            }

            System.out.println( currentTime() + "Product details query finished in " + searchResponse.getSeconds());

            Iterator<Element> iterator = searchResponse.getDocuments().iterator();

            while (iterator.hasNext()) {
                ProductDetails productDetails = (ProductDetails) productDetailsUnmarshaller.unmarshal(iterator.next());
                productDetailsList.add(productDetails);
            }

            System.out.println( currentTime() + "Product details list created");
        } catch (Exception e) {
            System.err.println(currentTime() + "Unable to query product details: " + e.getMessage());
            return null;
        }

        return productDetailsList;
    }

    private ProductDetails getFirstOrNull(Collection<ProductDetails> products) {

        if( products.isEmpty()) {
            return null;
        }

        return products.iterator().next();
    }
}
