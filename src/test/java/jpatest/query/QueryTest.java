package jpatest.query;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import jpatest.query.Category;
import jpatest.query.Person;

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
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
// TODO check, it is correct?
@Cleanup(phase = TestExecutionPhase.AFTER)
public class QueryTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Person.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    /**
     * This shows group by / having aggregate functions. 
     * 
     * It perform a query to retrieve all persons having no category
     */
    @Test
    @UsingDataSet("query/initial.yml")
    public void testGroupByHaving() {

        // Given
        // Initial dataset

        // When
        // Query using JPQL
        TypedQuery<Person> q = em.createNamedQuery(Person.SELECT_PERSONS_WITHOUT_CATEGORIES, Person.class);

        // Then
        assertEquals(1, q.getResultList().size());

        // When
        // Query using Criteria
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> c = cb.createQuery(Person.class);
        Root<Person> person = c.from(Person.class);
        c.select(person);
        Join<Person, Category> categories = person.join("categories", JoinType.LEFT);
        c.groupBy(person);
        c.having(cb.equal(cb.count(categories), 0));
        q = em.createQuery(c);
        List<Person> result = q.getResultList();

        // Then
        assertEquals(1, result.size());
        assertEquals("Samuel", result.get(0).getFirstName());
    }

    /**
     * This shows usage of Criteria API to build a dynamic join and where clauses depending on search parameters
     */
    @Test
    @UsingDataSet("query/initial.yml")
    public void testSearch() {

        // Given
        // Initial dataset

        // When/Then
        assertEquals(1, searchPersons("Legr%", "friends").size());
        assertEquals(2, searchPersons(null, "friends").size());
    }

    private List<Person> searchPersons(String lastNamePattern, String categoryName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> c = cb.createQuery(Person.class);
        Root<Person> person = c.from(Person.class);
        c.select(person);

        List<Predicate> predicates = new ArrayList<Predicate>();
        if (lastNamePattern != null) {
            predicates.add(cb.like(person.get("lastName").as(String.class), lastNamePattern));
        }
        if (categoryName != null) {
            Join<Person, Category> categories = person.join("categories", JoinType.LEFT);
            predicates.add(cb.equal(categories.get("name").as(String.class), categoryName));
        }
        c.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        TypedQuery<Person> q = em.createQuery(c);
        return q.getResultList();
    }

    /**
     * This shows usage of ALL expression in where clause. 
     * 
     * It perform a query to retrieve the person with max salary
     */
    @Test
    @UsingDataSet("query/initial.yml")
    public void testWhereAll() {

        // Given
        // Initial dataset

        // When
        // Query using JPQL
        TypedQuery<Person> q = em.createNamedQuery(Person.SELECT_PERSON_WITH_MAX_SALARY, Person.class);

        // Then
        assertEquals("Legros", q.getSingleResult().getLastName());

        // When
        // Query using Criteria
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> c = cb.createQuery(Person.class);
        Root<Person> person = c.from(Person.class);
        c.select(person);
        Subquery<BigDecimal> sc = c.subquery(BigDecimal.class);
        Root<Person> person1 = sc.from(Person.class);
        sc.select(person1.get("salary").as(BigDecimal.class));
        sc.where(cb.isNotNull(person1.get("salary")));
        c.where(cb.greaterThanOrEqualTo(person.get("salary").as(BigDecimal.class), cb.all(sc)));
        q = em.createQuery(c);

        // Then
        assertEquals("Legros", q.getSingleResult().getLastName());
    }

    /**
     * This shows usage of COUNT aggregate function in select clause. 
     * 
     * It perform a query to count number of categories for each person
     */
    @Test
    @UsingDataSet("query/initial.yml")
    public void testSelectCount() {

        // Given
        // Initial dataset

        // When
        // Query using JPQL
        Query q = em.createQuery("select d, count(e) from Department d left join d.employees e group by d");
        q.getResultList();
    }

    /**
     * This shows result of query according to what is specified in select clause
     */
    @Test
    @UsingDataSet("query/initial.yml")
    public void testSelect() {

        // Given
        // Initial dataset

        // When
        // Query all employees from Department R&D
        Query q1 = em.createQuery("select e from Department d join d.employees e where d.name = 'R&D'");

        // Old EJB-QL syntax
        Query q2 = em.createQuery("select e from Department d, in (d.employees) e where d.name = 'R&D'");

        // This is supposed to be invalid syntax (collection-valued path in select clause) but is supported by Hibernate
        Query q3 = em.createQuery("select d.employees from Department d where d.name = 'R&D'");

        // Then
        assertEquals(2, q1.getResultList().size());
        assertEquals(2, q2.getResultList().size());
        assertEquals(2, q3.getResultList().size());
    }

}
