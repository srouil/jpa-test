package jpatest.dtomapping;

import javax.ejb.EJB;
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
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests showing different patterns for mapping of entities to dtos and back
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.DISABLED)
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
    DepartmentServiceMapMergeBean departmentServiceMM;

    /**
     * Test shows ...
     */
    @Test
    @UsingDataSet("dtomapping/initial.yml")
    @ShouldMatchDataSet("dtomapping/expected.yml")
    public void testUpdateMapMerge() {

        // Given
        // Initial dataset

        // When
        DepartmentDTO department = departmentServiceMM.findDepartmentById(1001L);
        
        // Update department attribute
        department.setName("R&D and more");
        
        // Update inverse OneToMany
        EmployeeDTO employeeToRemove = department.getEmployees().get(0);
        employeeToRemove.setDepartment(null);
        
        // We must keep employee in collection so that it will be mapped to an entities and finally be reached when cascading merge operation
        // This is quite couter-intuitive
        // department.getEmployees().remove(employeeToRemove);
        
        EmployeeDTO employee = departmentServiceMM.findEmployeeById(1002L);
        employee.setDepartment(department);
        department.getEmployees().add(employee);

        // Update ManyToMany
        department.getProjects().clear();
        ProjectDTO project = departmentServiceMM.findProjectById(1002L);
        department.getProjects().add(project);
        
        departmentServiceMM.updateDepartment(department);
        
        // Then
        // Expected dataset
    }

}
