package ch.quickorder.entities;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="document")
public class Order {

    private String id;

    private String restaurant;

    List< String> products;
}
