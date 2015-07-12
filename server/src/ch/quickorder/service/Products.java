package ch.quickorder.service;

import ch.quickorder.entities.Product;
import ch.quickorder.model.ProductsModel;
import ch.quickorder.util.LogPrefix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;

@Controller
@RequestMapping(produces = "application/json", value = "/products")
public class Products {

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Product> getProducts(@RequestHeader("x-qo-userid") String userId) {
        System.out.println(LogPrefix.currentTime("> Products.getProducts: Begin"));

        try {
            return ProductsModel.getInstance().getProducts();
        } finally {
            System.out.println(LogPrefix.currentTime( "< Products.getProducts: End"));
        }
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Product getProductById(@PathVariable String id,
                           @RequestHeader("x-qo-userid") String userId) {
        System.out.println(LogPrefix.currentTime("> Products.getProductById: Begin"));

        try {
            return ProductsModel.getInstance().getProductById(id);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Products.getProductById: End"));
        }
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Product> getProductByName(@PathVariable String name,
                                         @RequestHeader("x-qo-userid") String userId) {
        System.out.println(LogPrefix.currentTime("> Products.getProductByName: Begin"));

        try {
            return ProductsModel.getInstance().getProductsByName(name);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Products.getProductByName: End"));
        }
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.PUT)
    public
    @ResponseBody
    Boolean updateProductPrice(@PathVariable String id,
                               @RequestParam(value="price") BigDecimal price,
                               @RequestHeader("x-qo-userid") String user) {
        System.out.println(LogPrefix.currentTime("> Products.updateProductPrice: Begin"));

        try {
            return ProductsModel.getInstance().updateProductPrice(id, price);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Products.updateProductPrice: End"));
        }
    }

}
