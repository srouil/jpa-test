package jpatest.dtomapping;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests showing different patterns for mapping of entities to dtos and back
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
@ApplyScriptBefore("scripts/dtomapping/cleanup.sql")
@Cleanup(phase = TestExecutionPhase.NONE)
public class DtoMappingTest {

    @Deployment
    public static Archive<?> createTestArchive() {

        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

        return ShrinkWrap.create(WebArchive.class, "test.war").addAsLibraries(resolver.artifact("net.sf.dozer:dozer:5.3.1").resolveAsFiles()).addPackage(Employee.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml").addAsResource("dozer-mapping.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    @EJB
    DepartmentServiceBean departmentService;

    /**
     * Test shows that it is possible to add several new entities into a set
     */
    @Test
    @UsingDataSet("dtomapping/initial.yml")
    public void test() {

        // Given
        // Initial dataset

        // When
        DepartmentDTO department = departmentService.findDepartmentById(1001L);
        
        // Then
        // Expected dataset
        //        Assert.assertEquals("Two new instances is added", 4, department.getEmployees().size());
    }

}
