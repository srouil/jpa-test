package jpatest.inheritance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("C")
public class Company extends Subject {

    @Column(name = "COMM_REG_NR")
    private String commercialRegisterNr;

}
