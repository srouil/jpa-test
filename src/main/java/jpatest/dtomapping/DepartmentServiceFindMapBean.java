package jpatest.dtomapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;

/**
 * Implementation of a simplistic service interface for working with Department,
 * Employee and Project.
 * 
 * This class implement the "find -> map" solution for updating entities:
 * Persistent entity is found for ID of DTO DTO state is mapped to persistent
 * entity
 */
@Stateless
public class DepartmentServiceFindMapBean implements DepartmentService {

    public static DozerBeanMapper mapper;

    @PostConstruct
    public void initDozer() {
        if (mapper == null) {
            List<String> files = new ArrayList<String>();
            files.add("dozer-find-map.xml");
            mapper = new DozerBeanMapper(files);

            // initialize custom converter
            Map<String, CustomConverter> customConverters = new HashMap<String, CustomConverter>();
            DTOToEntityConverter c = new DTOToEntityConverter();
            c.setEntityManager(em);
            customConverters.put("dtoToEntityConverter", c);
            mapper.setCustomConvertersWithId(customConverters);
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

        // Find
        Department department = em.find(Department.class, departmentDTO.getId());

        // Map - apply (potentially partial) state of DTO to persistent entity
        mapper.map(departmentDTO, department);

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
