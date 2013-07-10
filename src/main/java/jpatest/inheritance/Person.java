package jpatest.inheritance;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("P")
public class Person extends Subject {

    private String firstName;

}
