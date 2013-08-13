package jpatest.enumusertype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

//@formatter:off
@Entity
@Table(name = "ordr")
@NamedQuery(
        name = Order.SELECT_PAID_ORDERS_BY_DELIVERY_STATUS, 
        query = "select o from Order o where o.state = " + OrderState.CODE_PAID + " " +
                "and o.deliveryStatus = :deliveryStatus")
//@formatter:on
public class Order {

    public static final String SELECT_PAID_ORDERS_BY_DELIVERY_STATUS = "selectPaidOrdersByDeliveryStatus";

    @Id
    @GeneratedValue
    private Long id;

    private String comment;

    //@formatter:off
    @Type(
        type = "jpatest.enumusertype.GenericEnumUserType", 
        parameters = { 
            @Parameter(name = "enumClass", value = "jpatest.enumusertype.OrderState"),
            @Parameter(name = "identifierMethod", value = "getCode") })
    //@formatter:on
    private OrderState state;

    // Alternative solution to enum user type
    @Column(name = "previous_state")
    private Integer previousStateCode;

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
    private DeliveryStatus deliveryStatus;

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

    public OrderState getPreviousState() {
        OrderState result = null;
        if (previousStateCode != null) {
            result = OrderState.valueOf(previousStateCode);
        }

        return result;
    }

    public void setPreviousState(OrderState previousState) {
        if (previousState != null) {
            previousStateCode = previousState.getCode();
        } else {
            previousStateCode = null;
        }
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
