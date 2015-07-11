package ch.quickorder.entities;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name="document")
public class User {

    private String id;

    private String firstName;
    private String lastName;

    private Collection< String> orders;
}
