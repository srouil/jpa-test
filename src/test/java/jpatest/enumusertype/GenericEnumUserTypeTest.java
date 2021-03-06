package jpatest.enumusertype;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests showing usage of GenericEnumUserType
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
@Cleanup(phase = TestExecutionPhase.AFTER)
public class GenericEnumUserTypeTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Order.class.getPackage()).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    @Test
    @UsingDataSet("enumusertype/initial1.yml")
    public void testFind() {

        // Given
        // Initial dataset

        // When
        Order order = em.find(Order.class, 1001L);

        // Then
        Assert.assertEquals("Order state is NEW", order.getState(), OrderState.NEW);
    }

    @Test
    @UsingDataSet("enumusertype/initial1.yml")
    @ShouldMatchDataSet("enumusertype/expected.yml")
    public void testCreateUpdate() {

        // Given
        // Initial dataset

        // When
        Order order = em.find(Order.class, 1001L);
        order.setState(OrderState.PAID);
        order.setPreviousState(OrderState.NEW);
        order.setDeliveryStatus(DeliveryStatus.SHIPPED);

        Order order2 = new Order();
        order2.setComment("second order");
        order2.setState(OrderState.ERROR);
        order2.setPreviousState(OrderState.NEW);
        em.persist(order2);

        // Then
        // Expected dataset
    }

    @Test
    @UsingDataSet("enumusertype/initial2.yml")
    public void testQuery() {

        // Given
        // Initial dataset

        // When
        TypedQuery<Order> q = em.createNamedQuery(Order.SELECT_PAID_ORDERS_BY_DELIVERY_STATUS, Order.class);
        q.setParameter("deliveryStatus", DeliveryStatus.DELIVERED);

        // Then
        Assert.assertEquals(1, q.getResultList().size());
    }

}
