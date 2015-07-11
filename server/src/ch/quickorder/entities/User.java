package ch.quickorder.entities;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name="document")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    private String id;

    private String firstName;
    private String lastName;

    @XmlElementWrapper(name="orders")
    @XmlElement(name = "id", type = String.class)
    private Collection< String> orders;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Collection<String> getOrders() {
        return orders;
    }

    public void setOrders(Collection<String> orders) {
        this.orders = orders;
    }
}
