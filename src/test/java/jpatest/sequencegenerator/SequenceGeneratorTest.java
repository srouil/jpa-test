package jpatest.sequencegenerator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ApplyScriptBefore;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import sequencegenerator.AlertEvent;
import sequencegenerator.SystemEvent;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
@ApplyScriptBefore("scripts/sequencegenerator/cleanup.sql")
public class SequenceGeneratorTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(SystemEvent.class.getPackage()).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    /**
     * Test showing behavior of @SequenceGenerator with Hibernate 4
     */
    @Test
    public void testCreateSystemEvent() {

        // Given

        // When
        for (int i = 0; i < 24; i++) {
            SystemEvent e = new SystemEvent();
            e.setText("Hello world !!!");
            em.persist(e);
        }

        // Then
        //        Assert.assertEquals(1L, e.getId().longValue());
    }

    /**
     * Test showing Hibernate-specific @GenericGenerator with SequenceHiLoGenerator
     */
    @Test
    public void testCreateAlertEvent() {

        // Given

        // When
        for (int i = 0; i < 23; i++) {
            AlertEvent e = new AlertEvent();
            e.setText("Hello world !!!");
            em.persist(e);
        }

        // Then
        //        Assert.assertEquals(1L, e.getId().longValue());
    }
}
