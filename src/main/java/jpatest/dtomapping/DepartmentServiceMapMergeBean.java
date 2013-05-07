package jpatest.dtomapping;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.DozerBeanMapper;

/**
 * Implementation of a simplistic service interface for working with Department,
 * Employee and Project.
 * 
 * This class implement the "map -> merge" solution for updating entities: DTO
 * is mapped to a new detached entities Detached entity is merged with
 * persistence context
 */
@Stateless
public class DepartmentServiceMapMergeBean implements DepartmentService {

    public static DozerBeanMapper mapper;

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

    @Override
    public <T extends DepartmentLightDTO> T findDepartmentById(Long id, Class<T> dtoClass) {
        Department d = em.find(Department.class, id);
        return mapper.map(d, dtoClass);
    }

    @Override
    public DepartmentFullDTO createDepartment(DepartmentFullDTO departmentDTO) {
        Department department = mapper.map(departmentDTO, Department.class);
        em.persist(department);
        return mapper.map(department, DepartmentFullDTO.class);
    }

    @Override
    public DepartmentLightDTO updateDepartment(DepartmentLightDTO departmentDTO) {

        // Map DTO to a new detached entity object
        Department department = mapper.map(departmentDTO, Department.class);

        // Merge with persistence context
        department = em.merge(department);

        // Following steps can be omitted if update method does not need to return a DTO
        // Flush persistence context to increment version attribute
        em.flush();

        // Map to the same DTO type as input parameter
        return mapper.map(department, departmentDTO.getClass());
    }

    @Override
    public void deleteDepartment(DepartmentLightDTO departmentDTO) {
        Department department = mapper.map(departmentDTO, Department.class);
        em.remove(department);
    }

    @Override
    public EmployeeDTO findEmployeeById(Long id) {
        Employee e = em.find(Employee.class, id);
        return mapper.map(e, EmployeeDTO.class);
    }

    @Override
    public ProjectDTO findProjectById(Long id) {
        Project p = em.find(Project.class, id);
        return mapper.map(p, ProjectDTO.class);
    }

    @Override
    public CompanyDTO findCompanyById(Long id) {
        Company c = em.find(Company.class, id);
        return mapper.map(c, CompanyDTO.class);
    }

}
