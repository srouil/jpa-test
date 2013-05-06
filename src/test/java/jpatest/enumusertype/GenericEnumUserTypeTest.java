package jpatest.enumusertype;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    @UsingDataSet("enumusertype/initial.yml")
    public void testFind() {

        // Given
        // Initial dataset

        // When
        Order order = em.find(Order.class, 1001L);

        // Then
        Assert.assertEquals("Order state is NEW", order.getState(), OrderState.NEW);
    }

    @Test
    @UsingDataSet("enumusertype/initial.yml")
    @ShouldMatchDataSet("enumusertype/expected.yml")
    public void testCreateUpdate() {

        // Given
        // Initial dataset

        // When
        Order order = em.find(Order.class, 1001L);
        order.setState(OrderState.PAID);
        order.setDeliveryStatus(DeliveryStatus.SHIPPED);

        Order order2 = new Order();
        order2.setComment("second order");
        order2.setState(OrderState.ERROR);
        em.persist(order2);

        // Then
        // Expected dataset
    }

}
