package jpatest.enumusertype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "ordr")
public class Order {

    @Id
    @GeneratedValue
    Long id;

    String comment;

    //@formatter:off
    @Type(
        type = "jpatest.enumusertype.GenericEnumUserType", 
        parameters = { 
            @Parameter(name = "enumClass", value = "jpatest.enumusertype.OrderState"),
            @Parameter(name = "identifierMethod", value = "getCode") })
    //@formatter:on
    OrderState state;

    //@formatter:off
    @Column(name = "delivery_status")
    @Type(
        type = "jpatest.enumusertype.GenericEnumUserType", 
        parameters = { 
            @Parameter(name = "enumClass", value = "jpatest.enumusertype.DeliveryStatus"),
            @Parameter(name = "identifierMethod", value = "getCode"),
            @Parameter(name = "valueOfMethod", value = "valueOfCode"),
            })
    //@formatter:on
    DeliveryStatus deliveryStatus;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Long getId() {
        return id;
    }

}
