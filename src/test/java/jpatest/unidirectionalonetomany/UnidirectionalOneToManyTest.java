package jpatest.unidirectionalonetomany;

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

/**
 * This test show different behavior of Hibernate when using List (bag semantic) or Set in 
 * unidirectional XToMany associations. 
 * 
 * This shows that the rules for choosing the right collection type for unidirectional ToMany associations 
 * are different from traditional parent-child parent where ToMany association is inverse. 
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
@ApplyScriptBefore("scripts/unidirectionalonetomany/cleanup.sql")
@Cleanup(phase = TestExecutionPhase.NONE)
public class UnidirectionalOneToManyTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Employee.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    @Test
    @UsingDataSet("unidirectionalonetomany/initial.yml")
    @ShouldMatchDataSet("unidirectionalonetomany/expected.yml")
    public void testAdd() {

        // Given
        // Initial dataset

        // When
        Employee employee = em.find(Employee.class, 1001L);
        
        // Here Hibernate will issue one insert into employee_phone because association is mapped as Set
        Phone phone = new Phone();
        phone.setNumber("+41264300228");
        em.persist(phone);
        employee.getPhones().add(phone);
        
        // Here Hibernate will first delete all records from employee_phone for this employee 
        // and re-insert all elements of collections into employee_project because association is mapped as List (bag semantic)
        Project project = new Project();
        project.setName("Fiasco");
        em.persist(project);
        employee.getProjects().add(project);

        // Then
        // Expected dataset
    }
    
}