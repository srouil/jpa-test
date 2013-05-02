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
 * Tests showing 2 different strategies for mapping of entities to dtos and back
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
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml").addAsResource("dozer-map-merge.xml").addAsResource("dozer-find-map.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    @EJB
    DepartmentServiceMapMergeBean departmentServiceMM;

    @EJB
    DepartmentServiceFindMapBean departmentServiceFM;

    /**
     * Test shows different use cases with services update() method implemented with the map-merge strategy
     */
    @Test
    @UsingDataSet("dtomapping/initial.yml")
    @ShouldMatchDataSet("dtomapping/expected1.yml")
    public void testUpdateMapMerge() {

        // Given
        // Initial dataset

        // When
        DepartmentFullDTO department = departmentServiceMM.findDepartmentById(1001L);

        // Update department attribute
        department.setName("R&D and more");

        // Modify ManyToOne association
        CompanyDTO newCompany = departmentServiceMM.findCompanyById(1002L);
        department.setCompany(newCompany);

        // Remove element from OneToMany collection to delete it 
        EmployeeDTO employeeToRemove = department.getEmployees().get(0);
        department.getEmployees().remove(employeeToRemove);

        // Update attribute of OneToMany collection element
        EmployeeDTO employeeToUpdate = department.getEmployees().get(0);
        employeeToUpdate.setFirstName("Lulue");

        // Add a new Employee to collection
        EmployeeDTO newEmployee = new EmployeeDTO();
        newEmployee.setFirstName("Samuel");
        newEmployee.setLastName("Rouiller");
        newEmployee.setDepartment(department);
        department.getEmployees().add(newEmployee);

        // Update ManyToMany
        department.getProjects().clear();
        ProjectDTO project = departmentServiceMM.findProjectById(1002L);
        department.getProjects().add(project);

        departmentServiceMM.updateDepartment(department);

        // Then
        // Expected dataset
    }

    /**
     * Test shows different use cases with services update() method implemented with the find-map strategy
     */
    @Test
    @UsingDataSet("dtomapping/initial.yml")
    @ShouldMatchDataSet("dtomapping/expected1.yml")
    public void testUpdateFullFindMap() {

        // Given
        // Initial dataset

        // When
        DepartmentFullDTO department = departmentServiceFM.findDepartmentById(1001L, DepartmentFullDTO.class);

        // Update department attribute
        department.setName("R&D and more");

        // Modify ManyToOne association
        CompanyDTO newCompany = departmentServiceMM.findCompanyById(1002L);
        department.setCompany(newCompany);

        // Remove element from OneToMany collection to delete it 
        EmployeeDTO employeeToRemove = department.getEmployees().get(0);
        department.getEmployees().remove(employeeToRemove);

        // Update attribute of OneToMany collection element
        EmployeeDTO employeeToUpdate = department.getEmployees().get(0);
        employeeToUpdate.setFirstName("Lulue");

        // Add a new Employee to collection
        EmployeeDTO newEmployee = new EmployeeDTO();
        newEmployee.setFirstName("Samuel");
        newEmployee.setLastName("Rouiller");
        newEmployee.setDepartment(department);
        department.getEmployees().add(newEmployee);

        // Update ManyToMany
        department.getProjects().clear();
        ProjectDTO project = departmentServiceFM.findProjectById(1002L);
        department.getProjects().add(project);

        departmentServiceFM.updateDepartment(department);

        // Then
        // Expected dataset
    }

    @Test
    @UsingDataSet("dtomapping/initial.yml")
    @ShouldMatchDataSet("dtomapping/expected2.yml")
    public void testUpdateLightFindMap() {

        // Given
        // Initial dataset

        // When
        DepartmentLightDTO department = departmentServiceFM.findDepartmentById(1001L, DepartmentLightDTO.class);

        // Update department attribute
        department.setName("Research");

        departmentServiceFM.updateDepartment(department);

        // Then
        // Expected dataset
    }
}
