package jpatest.pessimisticlocking;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpatest.Resources;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests ...
 */
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
@Transactional(TransactionMode.DISABLED)
public class PessimisticLockingTest {

    @Deployment
    public static Archive<?> createTestArchive() {

        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Resource.class.getPackage()).addClass(Resources.class).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    SomeService someService;

    @PersistenceContext
    EntityManager em;

    @EJB
    EmployeeServiceImpl employeeService;

    /**
     * Test showing use of pessimistic locking to acquire exclusive lock on a
     * named resource.
     */
    @Test
    public void testLockResource() {

        // Use low values of sleep time because timeout of JBoss H2 DB is very
        // low (around 1000 ms).
        // With Oracle, higher values can be used, timeout is higher.
        // Oracle Dialect will generate SQL query "SELECT ... FOR UPATE WAIT 30"
        // that allow to arbitrary set timeout.

        Thread t1 = new Thread() {

            @Override
            public void run() {

                for (int i = 0; i < 3; i++) {
                    someService.doSomething(300);
                }
            }
        };

        Thread t2 = new Thread() {

            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    someService.doSomething(400);
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

    /**
     * Test showing use of pessimistic locking to lock a particular record.
     */
    @Test
    @UsingDataSet("pessimisticlocking/initial.yml")
    public void testLockRecord() {

        Thread t1 = new Thread() {

            @Override
            public void run() {

                for (int i = 0; i < 5; i++) {
                    employeeService.adjustEmployeeVacation(1000L, 5);
                }
            }
        };

        Thread t2 = new Thread() {

            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    employeeService.adjustEmployeeVacation(1000L, 10);
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

        Employee e = em.find(Employee.class, 1000L);

        // assert usually fails if EmployeeService does not use a pessimistic
        // lock on employee
        Assert.assertEquals(new Integer(55), e.getVacationDays()); // 5*5 + 3*10
    }
}
