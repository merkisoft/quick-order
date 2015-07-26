package ch.quickorder.service;

import ch.quickorder.model.ProductDetailsModel;
import ch.quickorder.util.LogPrefix;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(produces = "application/json", value = "/product/details")
public class ProductDetails {
    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    ch.quickorder.entities.ProductDetails getDetailsForProduct(@RequestParam(value="locale", defaultValue="en") String locale,
                                                               @PathVariable String id,
                                                               @RequestHeader("x-qo-userid") String userId) {
        System.out.println(LogPrefix.currentTime("> ProductDetails.getDetailsForProduct: Begin"));

        try {
            return ProductDetailsModel.getInstance().getDetailsForProduct(id);
        } finally {
            System.out.println(LogPrefix.currentTime( "< ProductDetails.getDetailsForProduct: End"));
        }
    }
}
