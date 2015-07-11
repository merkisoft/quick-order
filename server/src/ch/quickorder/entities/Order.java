package ch.quickorder.entities;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@XmlRootElement(name="document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {

    private String id;
    private String restaurant;
    private Integer ticketNumber;
    private String status;
    private Long timestamp;
    private Integer table;

    @XmlElementWrapper(name="items")
    @XmlElement(name = "item", type = OrderItem.class)
    private Collection< OrderItem> items;

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

    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTable() {
        return table;
    }

    public void setTable(Integer table) {
        this.table = table;
    }

    public Collection<OrderItem> getItems() {
        return items;
    }

    public void setItems(Collection<OrderItem> items) {
        this.items = items;
    }
}
