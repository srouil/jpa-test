package jpatest.elementcollection;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class PhoneNumber {

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private NumberType type;

    @Column(name = "NBR")
    private String number;

    public NumberType getType() {
        return type;
    }

    public void setType(NumberType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
