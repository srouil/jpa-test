package jpatest.optimisticlocking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ApplyScriptBefore;
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
 * Tests showing different scenarios using optimistic locking
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.DISABLED)
@ApplyScriptBefore("scripts/optimisticlocking/cleanup.sql")
@Cleanup(phase = TestExecutionPhase.NONE)
public class OptimisticLockingTest {

    @Deployment
    public static Archive<?> createTestArchive() {

        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Employee.class.getPackage()).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    @EJB
    DepartmentService departmentService;

    /**
     * Test concurrent updates of Department attributes
     */
    @Test
    @UsingDataSet("optimisticlocking/initial.yml")
    @ShouldMatchDataSet("optimisticlocking/expected1.yml")
    public void testUpdateAttributes() {

        // Given
        // Initial Dataset

        Department dept = departmentService.findDepartmentById(1001L);

        // When
        // update
        dept.setName("Technology");
        departmentService.updateDepartment(dept);

        // second update of initial object, should throw javax.persistence.OptimisticLockException
        dept.setName("Dummy");
        try {
            departmentService.updateDepartment(dept);
            fail("Must throw javax.persistence.OptimisticLockException");
        } catch (EJBException e) {

            // Expected
            assertEquals(OptimisticLockException.class, e.getCause().getClass());
        }

        // Then
        // Expected dataset
    }

    /**
     * Test concurrent updates of inverse OneToMany association
     * - Hibernate will update version ID of department when inverse OneToMany association "employees" is modified
     * - no need to specially lock Department object using OPTIMISTIC_FORCE_INCREMENT
     */
    @Test
    @UsingDataSet("optimisticlocking/initial.yml")
    @ShouldMatchDataSet("optimisticlocking/expected2.yml")
    public void testUpdateInverseOneToMany() {

        // Given
        // Initial Dataset

        Department dept = departmentService.findDepartmentById(1001L);

        // When

        // add new employee to association
        Employee emp = departmentService.findEmployeeById(1002L);
        emp.setDepartment(dept);
        dept.getEmployees().add(emp);

        departmentService.updateDepartment(dept);

        // remove employee from association, should throw javax.persistence.OptimisticLockException
        dept.getEmployees().clear();

        try {
            departmentService.updateDepartment(dept);
            fail("Must throw javax.persistence.OptimisticLockException");
        } catch (EJBException e) {

            // Expected
            assertEquals(OptimisticLockException.class, e.getCause().getClass());
        }

        // Then
        // Expected dataset
    }

    /**
     * Test LockModeType.OPTIMISTIC_FORCE_INCREMENT used with a query:
     * Version ID of all returned entities (here one department and one employee) is incremented when persistence context is flushed, 
     * even if no entity has been modified.
     */
    @Test
    @UsingDataSet("optimisticlocking/initial.yml")
    @ShouldMatchDataSet("optimisticlocking/expected3.yml")
    public void testForceIncrement() {

        // Given
        // Initial Dataset

        // When
        Department dept = departmentService.findDepartmentById(1001L, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        // Then
        // Expected dataset
    }

    /**
     * Test shows how it is possible to get non-repeatable reads with isolation level "read committed".
     * Test runs with TX and starts a new TX to simulate a concurrent TX.
     */
    @Test
    @Transactional(TransactionMode.COMMIT)
    @UsingDataSet("optimisticlocking/initial.yml")
    @ShouldMatchDataSet("optimisticlocking/expected4.yml")
    public void testNonRepeatableRead() {

        // Given 
        // Initial Dataset
        Department dept1 = departmentService.findDepartmentById(1001L);

        // When
        // Update department in a new TX
        departmentService.setDepartmentNameRequiresNew(1001L);

        // We must detach Department object, otherwise next query will retrieve Department object 
        // from persistence context instead of using query result. 
        // This would not be necessary if using SELECT NEW or reporting queries.
        em.detach(dept1);

        // Then
        // Second execution of same query in same TX gives different result
        Department dept2 = departmentService.findDepartmentById(1001L);
        assertFalse(dept1.getName().equals(dept2.getName()));

        // Expected Dataset
    }
}
