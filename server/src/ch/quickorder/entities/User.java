package ch.quickorder.entities;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="document")
public class User {

    private String id;

    private String firstName;
    private String lastName;

    private List< String> orders;
}
