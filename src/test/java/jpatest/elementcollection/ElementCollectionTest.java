package jpatest.elementcollection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
@Cleanup(phase = TestExecutionPhase.BEFORE)
public class ElementCollectionTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Person.class.getPackage()).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext(unitName = "jpaTestPU")
    EntityManager em;

    /**
     * Default fetch behavior is LAZY. This query uses "left join fetch" clause to eagerly load element collections
     * like we would do it with OneToManyy
     */
    @Test
    @UsingDataSet("elementcollection/initial.yml")
    public void testSelectFetch() {

        // Given
        // Initial dataset

        // When
        Person p = em.createNamedQuery(Person.SELECT_PERSON_BY_ID, Person.class).setParameter("id", 1001L).getSingleResult();

        // Then
        // One single SQL query is executed
    }

    /**
     * This shows how an element collection of a persistent entity can be modified 
     */
    @Test
    @UsingDataSet("elementcollection/initial.yml")
    @ShouldMatchDataSet("elementcollection/expected.yml")
    public void testModifyPersistentElementCollection() {

        // Given
        // Initial dataset
        Person pers = em.find(Person.class, 1001L);

        // When
        // Remove element of collection
        pers.getPhoneNumbers().remove(0);

        // Updated element of collection
        pers.getPhoneNumbers().get(0).setNumber("+41262222223");

        // Add a new element to collection
        PhoneNumber newPn = new PhoneNumber();
        newPn.setType(NumberType.MOBILE);
        newPn.setNumber("+41799999999");
        pers.getPhoneNumbers().add(newPn);

        pers.getEmailAddresses().add("lulu@latortue.org");

        // Then
        // Expected dataset
    }

    /**
     * This shows how an element collection of a detached entity can be modified 
     */
    @Test
    @UsingDataSet("elementcollection/initial.yml")
    @ShouldMatchDataSet("elementcollection/expected.yml")
    public void testModifyDetachedElementCollection() {

        // Given
        // Initial dataset
        Person pers = em.createNamedQuery(Person.SELECT_PERSON_BY_ID, Person.class).setParameter("id", 1001L).getSingleResult();

        em.detach(pers);

        // When
        // Remove element of collection
        pers.getPhoneNumbers().remove(0);

        // Updated element of collection
        pers.getPhoneNumbers().get(0).setNumber("+41262222223");

        // Add a new element to collection
        PhoneNumber newPn = new PhoneNumber();
        newPn.setType(NumberType.MOBILE);
        newPn.setNumber("+41799999999");
        pers.getPhoneNumbers().add(newPn);

        pers.getEmailAddresses().add("lulu@latortue.org");

        em.merge(pers);

        // Then
        // Expected dataset
    }

}
