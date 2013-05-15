package jpatest.elementcollection;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;

@Entity
@NamedQuery(name = "selectPersonById", query = "select p from Person p left join fetch p.phoneNumbers where p.id = :id")
public class Person {

    public static final String SELECT_PERSON_BY_ID = "selectPersonById";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    @ElementCollection
    @CollectionTable(name = "PERSON_PHONE_NUMBER")
    @OrderBy("type")
    private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();

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

    public Long getId() {
        return id;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

}
