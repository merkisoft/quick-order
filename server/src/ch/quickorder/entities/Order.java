package ch.quickorder.entities;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name="document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {

    private String id;

    private String restaurant;

    @XmlElementWrapper(name="products")
    @XmlElement(name = "id", type = String.class)
    private Collection< String> products = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public Collection<String> getProducts() {
        return products;
    }

    public void setProducts(Collection<String> products) {
        this.products = products;
    }
}
