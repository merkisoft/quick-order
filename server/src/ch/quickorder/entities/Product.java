package ch.quickorder.entities;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@XmlRootElement(name="document")
public class Product {

    private String id;

    private String restaurant;

    private String name;
    private String category;

    private BigDecimal price;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public Integer getCategoryPosition() {
        ProductGroup productGroup = new ProductGroup( getCategory());
        return productGroup.getPosition();
    }

    public String getCategoryName() {
        ProductGroup productGroup = new ProductGroup( getCategory());
        return productGroup.getName();
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getPriceFormatted() {
        DecimalFormat decimalFormat = new DecimalFormat( "#,##0.00");
        return decimalFormat.format( price.doubleValue());
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
