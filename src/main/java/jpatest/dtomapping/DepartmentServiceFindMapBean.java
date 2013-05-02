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
 * This class implement the "find -> map" solution for updating entities:
 * Persistent entity is found for ID of DTO
 * DTO state is mapped to persistent entity
 */
@Stateless
public class DepartmentServiceFindMapBean {

    public static Mapper mapper;

    @PostConstruct
    public void initDozer() {
        if (mapper == null) {
            List<String> files = new ArrayList<String>();
            files.add("dozer-find-map.xml");
            mapper = new DozerBeanMapper(files);
        }
    }

    @PersistenceContext(unitName = "")
    EntityManager em;

    public <T extends DepartmentLightDTO> T findDepartmentById(Long id, Class<T> dtoClass) {
        Department d = em.find(Department.class, id);
        return mapper.map(d, dtoClass);
    }

    public DepartmentFullDTO createDepartment(DepartmentFullDTO departmentDTO) {
        Department department = mapper.map(departmentDTO, Department.class);
        em.persist(department);
        return mapper.map(department, DepartmentFullDTO.class);
    }

    public void updateDepartment(DepartmentLightDTO departmentDTO) {

        // Find
        Department department = em.find(Department.class, departmentDTO.getId());

        // Map - apply (potentially partial) state of DTO to persistent entity
        mapper.map(departmentDTO, department);
    }

    public void deleteDepartment(DepartmentLightDTO departmentDTO) {
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
