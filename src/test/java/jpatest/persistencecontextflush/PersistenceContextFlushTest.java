package jpatest.persistencecontextflush;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpatest.persistencecontextflush.Book;
import jpatest.persistencecontextflush.Department;
import jpatest.persistencecontextflush.Employee;
import jpatest.persistencecontextflush.Person;
import jpatest.persistencecontextflush.PhoneNumber;

import org.hibernate.Session;
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
public class PersistenceContextFlushTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Department.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    /**
     * This shows flush behavior of Hibernate when issuing a query for entity where there is new persistence instances
     * in persistence context
     */
    @Test
    public void testQueryFlush() {

        // Given
        Department dept = new Department();
        dept.setName("R&D");
        em.persist(dept);

        Employee emp = new Employee();
        emp.setFirstName("Roger");
        emp.setLastName("Legros");
        emp.setDepartment(dept);
        dept.getEmployees().add(emp);
        em.persist(emp);

        // When        
        // Persistence context will be flushed because it contains persistent departments and employees
        List<Department> allDepts = em.createNamedQuery(Department.SELECT_ALL_DEPARTMENTS).getResultList();

        // Then
        Session session = em.unwrap(Session.class);
        assertFalse("Persistence context does not contain any un-flushed changes", session.isDirty());
    }

    /**
     * This shows flush behavior of Hibernate when issuing a query for entity for which there is no change
     */
    @Test
    public void testQueryNoFlush() {

        // Given
        Department dept = new Department();
        dept.setName("R&D");
        em.persist(dept);

        Employee emp = new Employee();
        emp.setFirstName("Roger");
        emp.setLastName("Legros");
        emp.setDepartment(dept);
        dept.getEmployees().add(emp);
        em.persist(emp);

        // When        
        // Persistence context will not be flushed because it does contains changes to Book entity
        List<Book> allBooks = em.createNamedQuery(Book.SELECT_ALL_BOOKS).getResultList();

        // Then
        Session session = em.unwrap(Session.class);
        assertTrue("Persistence still contains some un-flushed changes", session.isDirty());
    }
    
    /**
     * This shows flush behavior of Hibernate when persistence context contains a managed Person entity having a owned relationship to a detached PhoneNumber entity
     * (changes to the relationship are synchronised) 
     * 
     * See JPA2 specification chap. 3.2.4
     */
    @Test
    @UsingDataSet("persistencecontextflush/owned_onetomany_initial.yml")
    @ShouldMatchDataSet("persistencecontextflush/owned_onetomany_expected.yml")
    public void testFlushDetachedOwnedOneToMany() {

        // Given
        Person pers = em.find(Person.class, 1L);
        PhoneNumber n = em.find(PhoneNumber.class, 1L);
        em.detach(n);

        // When
        pers.getPhoneNumbers().add(n);
        
        // Then
        // Expected dataset
    }
    
}