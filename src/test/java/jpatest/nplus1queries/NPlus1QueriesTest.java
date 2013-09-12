package jpatest.nplus1queries;

import java.util.List;

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

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
@ApplyScriptBefore("scripts/nplus1queries/cleanup.sql")
@UsingDataSet("nplus1queries/initial.yml")
@Cleanup(phase = TestExecutionPhase.NONE)
public class NPlus1QueriesTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Department.class.getPackage()).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    /**
     * Test showing potential N+1 queries:
     * - one query to find employees
     * - one query for each department that is not loaded yet
     */
    @Test
    public void testFindNPlus1() {

        // Given
        // Initial dataset

        // When
        List<Employee> employees1 = em.createNamedQuery(Employee.SELECT_ALL_EMPLOYEES, Employee.class).getResultList();

        // Then
        // 3 queries (see log)
    }

    /**
     * Test showing named query with fetch clauses
     */
    @Test
    public void testFindFetchManyToOne() {

        // Given
        // Initial dataset

        // When
        List<Employee> employees = em.createNamedQuery(Employee.SELECT_ALL_EMPLOYEES_FETCH_ALL, Employee.class).getResultList();

        // Then
        // 1 query (see log)
    }

    /**
     * Test showing how associations are loaded when using EntityManager.find()
     */
    @Test
    public void testEntityManagerFind() {

        // Given
        // Initial dataset

        // When
        Employee employee = em.find(Employee.class, 1L);

        // Then
        // 1 query with joins
    }

    /**
     * Test showing how XtoMany are lazily fetched
     */
    @Test
    public void testFindXtoMany() {

        // Given
        // Initial dataset

        // When
        List<Department> departments = em.createNamedQuery(Department.SELECT_ALL_DEPARTMENTS, Department.class).getResultList();

        // Then
        // 2 queries 
        // - select * from department
        // - select * from employee e where e.department_id = 1
        Assert.assertEquals("2 departments are found", 2, departments.size());
        System.out.println(departments.get(0).getEmployees().iterator().next().getFirstName());
    }

    /**
     * Test showing how XtoMany are eagerly fetched by query.
     * 
     * Note that JPQL query use "DISTINCT" to eliminate duplicates department (cartesian product caused by join)
     */
    @Test
    public void testFindFetchXtoMany() {

        // Given
        // Initial dataset

        // When
        List<Department> departments = em.createNamedQuery(Department.SELECT_ALL_DEPARTMENTS_FETCH_ALL, Department.class).getResultList();

        // Then
        // 1 query with joins
        Assert.assertEquals("2 departments are found", 2, departments.size());
        System.out.println(departments.get(0).getEmployees().iterator().next().getFirstName());
    }
}
