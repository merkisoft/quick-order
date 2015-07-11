package ch.quickorder.service;

import ch.quickorder.entities.Product;
import ch.quickorder.model.ProductsModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;

@Controller
@RequestMapping(produces = "application/json", value = "/rest/products")
public class Products {

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Product> getProducts(@RequestHeader("x-qo-userid") String userId) {
        return ProductsModel.getInstance().getProducts();
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Product getProductById(@PathVariable String id,
                           @RequestHeader("x-qo-userid") String userId) {
        return ProductsModel.getInstance().getProductById(id);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Product> getProductByName(@PathVariable String name,
                                         @RequestHeader("x-qo-userid") String userId) {
        return ProductsModel.getInstance().getProductsByName(name);
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.PUT)
    public
    @ResponseBody
    Boolean updateProductPrice(@PathVariable String id,
                               @RequestParam(value="price") BigDecimal price,
                               @RequestHeader("x-qo-userid") String user) {
        return ProductsModel.getInstance().updateProductPrice(id, price);
    }

}
