package jpatest.detachedentities;

import java.util.Collection;

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
@Cleanup(phase = TestExecutionPhase.NONE)
public class DetachedEntitiesTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Person.class.getPackage()).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    /**
     * This shows how an owned collection can be modified after the owning entity has been detached
     * - remove entity of collection => will be removed by merge() because of orphanRemoval = true
     * - update entity of collection => will be updated by merge() because of cascade
     * - add new entity to collection => will be inserted by merge() because of cascade
     */
    @Test
    @UsingDataSet("detachedentities/initial.yml")
    @ShouldMatchDataSet("detachedentities/expected.yml")
    public void testMergeOwnedOneToMany() {

        // Given
        Person pers = em.find(Person.class, 1L);

        // Load lazy-loaded collection
        pers.getPhoneNumbers().size();
        em.detach(pers);

        // When
        // Remove entity of collection
        pers.getPhoneNumbers().remove(findPhoneNumber(pers.getPhoneNumbers(), "+41261111111"));

        // Updated entity of collection
        PhoneNumber pn = findPhoneNumber(pers.getPhoneNumbers(), "+41262222222");
        pn.setNumber("+41262222223");

        // Add a new entity to collection
        PhoneNumber newPn = new PhoneNumber();
        newPn.setType(NumberType.HOME);
        newPn.setNumber("+41269999999");
        pers.getPhoneNumbers().add(newPn);

        // Merge entity
        em.merge(pers);

        // Then
        // Expected dataset
    }

    private PhoneNumber findPhoneNumber(Collection<PhoneNumber> phoneNumbers, String number) {

        PhoneNumber result = null;
        for (PhoneNumber p : phoneNumbers) {
            if (p.getNumber().equals(number)) {
                result = p;
                break;
            }
        }

        System.out.println("found: " + result);
        return result;
    }

}
