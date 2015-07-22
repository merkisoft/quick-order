package ch.quickorder.entities;

import javax.xml.bind.annotation.*;
import java.util.Collection;

@XmlRootElement(name="document")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductDetails {

    private String id;
    private String language;

    private String product;

    @XmlElementWrapper(name="descriptions")
    @XmlElement(name = "description", type = String.class)
    private Collection< String> descriptions;

    @XmlElementWrapper(name="ingredients")
    @XmlElement(name = "ingredient", type = String.class)
    private Collection< String> ingredients;

    @XmlElementWrapper(name="types")
    @XmlElement(name = "type", type = String.class)
    private Collection< String> types;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Collection<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Collection<String> descriptions) {
        this.descriptions = descriptions;
    }

    public Collection<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Collection<String> getTypes() {
        return types;
    }

    public void setTypes(Collection<String> types) {
        this.types = types;
    }
}
