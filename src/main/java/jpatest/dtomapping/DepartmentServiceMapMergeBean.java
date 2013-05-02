package jpatest.dtomapping;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * Implementation of a simplistic service interface for working with Department, Employee and Project.
 * 
 * This class implement the "map -> merge" solution for updating entities:
 * DTO is mapped to a new detached entities
 * Detached entity is merged with persistence context
 */
@Stateless
public class DepartmentServiceMapMergeBean {

    public static Mapper mapper;

    @PostConstruct
    public void initDozer() {
        if (mapper == null) {
            List<String> files = new ArrayList<String>();
            files.add("dozer-map-merge.xml");
            mapper = new DozerBeanMapper(files);
        }
    }

    @PersistenceContext(unitName = "")
    EntityManager em;

    public DepartmentFullDTO findDepartmentById(Long id) {
        Department d = em.find(Department.class, id);
        return mapper.map(d, DepartmentFullDTO.class);
    }

    public DepartmentFullDTO createDepartment(DepartmentFullDTO departmentDTO) {
        Department department = mapper.map(departmentDTO, Department.class);
        em.persist(department);
        return mapper.map(department, DepartmentFullDTO.class);
    }

    public void updateDepartment(DepartmentFullDTO departmentDTO) {
        Department department = mapper.map(departmentDTO, Department.class);
        department = em.merge(department);
    }

    public void deleteDepartment(DepartmentFullDTO departmentDTO) {
        Department department = mapper.map(departmentDTO, Department.class);
        em.remove(department);
    }

    public EmployeeDTO findEmployeeById(Long id) {
        Employee e = em.find(Employee.class, id);
        return mapper.map(e, EmployeeDTO.class);
    }

    public ProjectDTO findProjectById(Long id) {
        Project p = em.find(Project.class, id);
        return mapper.map(p, ProjectDTO.class);
    }

    public CompanyDTO findCompanyById(Long id) {
        Company c = em.find(Company.class, id);
        return mapper.map(c, CompanyDTO.class);
    }

}
