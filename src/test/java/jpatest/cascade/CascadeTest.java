package jpatest.cascade;

import javax.persistence.EntityManager;
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

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
@ApplyScriptBefore("scripts/cascade/cleanup.sql")
@Cleanup(phase = TestExecutionPhase.NONE)
public class CascadeTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Department.class.getPackage()).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    /**
     * Test showing that during persistence context synchronization, persist operation is called for entities of 
     * associations using CascadeType.PERSIST or CascadeType.ALL
     */
    @Test
    @UsingDataSet("cascade/initial1.yml")
    @ShouldMatchDataSet("cascade/expected1.yml")
    public void testSynchronization() {

        // Given
        // Initial dataset

        // When
        Department dept = em.find(Department.class, 1000L);

        // Add new employeee
        Employee emp = new Employee();
        emp.setFirstName("Lulu");
        emp.setLastName("Latortue");
        emp.setDepartment(dept);
        dept.getEmployees().add(emp);

        // Then
        // Expected dataset        
    }

    /**
     * Test showing that EntityManager persist operation is propagated to associations using CascadeType.PERSIST or CascadeType.ALL
     */
    @Test
    @ShouldMatchDataSet("cascade/expected2.yml")
    public void testCascadePersist() {

        // Given
        // Initial dataset

        // When
        Department dept = new Department();
        dept.setName("QA");

        // Since manager does not use cascade we need to persist it first
        // Otherwise we get java.lang.IllegalStateException: org.hibernate.TransientObjectException: object references an unsaved transient instance 
        Employee manager = new Employee();
        manager.setFirstName("John");
        manager.setLastName("Tester");
        em.persist(manager);

        dept.setManager(manager);

        // Add new employee
        Employee emp = new Employee();
        emp.setFirstName("Lulu");
        emp.setLastName("Latortue");
        emp.setDepartment(dept);
        dept.getEmployees().add(emp);

        em.persist(dept);

        // Then
        // Expected dataset        
    }

    /**
     * Test showing that when orphanRemoval=true is defined for a OneToMany association, 
     * targeted entity is deleted when removed from association (orphaned)
     */
    @Test
    @UsingDataSet("cascade/initial2.yml")
    @ShouldMatchDataSet("cascade/expected3.yml")
    public void testOrphanRemoval() {

        // Given
        // Initial dataset

        // When
        Department dept = em.find(Department.class, 1000L);
        dept.getEmployees().clear();

        // Then
        // Expected dataset        
    }
}
