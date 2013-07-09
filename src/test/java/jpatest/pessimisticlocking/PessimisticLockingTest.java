package jpatest.pessimisticlocking;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests ...
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.DISABLED)
@Cleanup(phase = TestExecutionPhase.NONE)
public class PessimisticLockingTest {

    @Deployment
    public static Archive<?> createTestArchive() {

        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Resource.class.getPackage()).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    SomeService someService;

    @PersistenceContext
    EntityManager em;

    /**
     * Test showing use of pessimistic locking to acquire exclusive lock on a named resource.
     *  
     * Exception handling still need investigation:
     * we get EJBTransactionRolledbackException -> GenericJdbcException instead of LockTimeoutException or 
     * essimisticLockException as defined in JPA specification. 
     */
    @Test
    public void testLock() {

        // Use low values of sleep time because timeout of JBoss H2 DB is very low (probably 1000 ms)
        Thread t1 = new Thread() {

            @Override
            public void run() {

                for (int i = 0; i < 3; i++) {
                    someService.doSomething(500);
                }
            }
        };

        Thread t2 = new Thread() {

            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    someService.doSomething(300);
                }
            }
        };

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
