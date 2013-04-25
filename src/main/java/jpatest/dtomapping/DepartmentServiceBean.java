package jpatest.dtomapping;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

@Stateless
public class DepartmentServiceBean {

    public static Mapper mapper;

    @PostConstruct
    public void initDozer() {
        if (mapper == null ){
            List<String> files = new ArrayList<String>();
            files.add("dozer-mapping.xml");
            mapper = new DozerBeanMapper(files);
        }
    }
    
    @PersistenceContext(unitName = "")
    EntityManager em;
    
    public DepartmentDTO findDepartmentById(Long id) {
        Department d = em.find(Department.class, id);
        return mapper.map(d, DepartmentDTO.class);
    }

    public DepartmentDTO createDepartment(DepartmentDTO department) {
        return null;
    }

    public DepartmentDTO updateDepartment(DepartmentDTO department) {
        return null;
    }

    public void deleteDepartment(DepartmentDTO department) {

    }

    public EmployeeDTO findEmployeeById(Long id) {
        return null;
    }

    public EmployeeDTO createEmployee(EmployeeDTO employee) {
        return null;
    }

}
