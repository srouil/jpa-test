package jpatest.hashcodeequals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ApplyScriptBefore;
import org.jboss.arquillian.persistence.Cleanup;
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
 * Tests showing different use case relying on hashCode() and equals() implementation in entity classes
 * 
 * Currently only solution without hC/e implementation (rely on Object hC/e) is tested.
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
@ApplyScriptBefore("scripts/hashcodeequals/cleanup.sql")
@Cleanup(phase = TestExecutionPhase.NONE)
public class HashCodeEqualsTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Employee.class.getPackage()).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    /**
     * Test shows that it is possible to add several new entities into a set
     */
    @Test
    @UsingDataSet("hashcodeequals/initial.yml")
    public void testAddSeveralInstances() {

        // Given
        // Initial dataset

        // When
        Department department = em.find(Department.class, 1001L);

        Employee emp1 = new Employee();
        emp1.setFirstName("Reto");
        emp1.setLastName("Eins");
        emp1.setDepartment(department);
        department.getEmployees().add(emp1);

        Employee emp2 = new Employee();
        emp2.setFirstName("Ueli");
        emp2.setLastName("Zwei");
        emp2.setDepartment(department);
        department.getEmployees().add(emp2);

        // Then
        // Expected dataset
        Assert.assertEquals("Two new instances is added", 4, department.getEmployees().size());
    }

    /**
     * Test shows that equals() and hashCode() do not work for detached entities 
     * or entities from different persistence contexts.
     */
    @Test
    @UsingDataSet("hashcodeequals/initial.yml")
    public void testDetached() {

        // Given
        // Initial dataset

        // When
        Employee emp1 = em.find(Employee.class, 1001L);
        em.detach(emp1);

        Employee emp2 = em.find(Employee.class, 1001L);

        // Then
        Assert.assertFalse("Detached entities or entities from different persistence contexts are not equals", emp1.equals(emp2));
    }

    /**
     * Test the comparison of persistent entity with proxy. 
     * 
     * This seems to work because, depending on the order of the two line below we get both 
     * department and employee.getDepartement either as javassist proxy object or persistent entity
     * (two times the same object).
     * 
     * Therefore, I cannot reproduce the problem of http://burtbeckwith.com/blog/?p=53
     */
    @Test
    @UsingDataSet("hashcodeequals/initial.yml")
    public void testEqualsProxy() {

        // Given
        // Initial dataset

        // When
        Department department = em.find(Department.class, 1001L);
        Employee emp1 = em.find(Employee.class, 1001L);

        // Then
        Assert.assertTrue(emp1.getDepartment().equals(department));
    }

    @Test
    @UsingDataSet("hashcodeequals/initial.yml")
    public void testContainsAfterPersist() {

        // Given
        // Initial dataset

        // When
        Department department = em.find(Department.class, 1001L);

        Employee emp1 = new Employee();
        emp1.setFirstName("Reto");
        emp1.setLastName("Eins");
        emp1.setDepartment(department);
        department.getEmployees().add(emp1);

        em.persist(emp1);

        // Then
        Assert.assertTrue(department.getEmployees().contains(emp1));
    }

    /**
     * Test showing how modifying the result of hashCode() and equals() methods for an object contained in a Set
     * violates the contracts of Set
     */
    @Test
    public void testHashSet() {

        Set<Element> set = new HashSet<Element>();
        Element e = new Element();
        set.add(e);
        assertTrue(set.contains(e));

        e.setId(3L);
        assertFalse("Element has changed, violating set contract", set.contains(e));
    }

    /**
     * Test showing that modifying the result of hashCode() and equals() methods is unproblematic for lists 
     * (List implementations use object identity to access contained objects)
     */
    @Test
    public void testList() {

        List<Element> list = new LinkedList<Element>();
        Element e = new Element();
        list.add(e);
        assertTrue(list.contains(e));

        e.setId(3L);
        assertTrue(list.contains(e));
    }

}

/**
 * Simple POJO whose ID attribute can be set, modifying the result of hashCode() and equals() methods 
 */
class Element {

    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return -1;
        }

        return id.intValue();
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Element) {
            Element e = (Element) o;

            return e.getId() == id;
        }

        return false;
    }
}
