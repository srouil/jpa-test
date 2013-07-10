package jpatest.optimisticlocking;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Implementation of a simplistic service interface for working with Department,
 * and Employee.
 */
@Stateless
public class DepartmentServiceImpl implements DepartmentService {

    @PersistenceContext(unitName = "")
    EntityManager em;

    @Override
    public Department findDepartmentById(Long id) {
        return findDepartmentById(id, LockModeType.NONE);
    }

    @Override
    public Department findDepartmentById(Long id, LockModeType lockModeType) {
        TypedQuery<Department> q = em.createNamedQuery(Department.SELECT_DEPARTMENT_BY_ID, Department.class);
        q.setParameter("departmentId", id);
        q.setLockMode(lockModeType);

        return q.getSingleResult();
    }

    @Override
    public Department updateDepartment(Department department) {

        return em.merge(department);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setDepartmentNameRequiresNew(Long departmentId) {

        Department dept = em.find(Department.class, departmentId);
        dept.setName("Test");
    }

    public Employee findEmployeeById(Long employeeId) {
        return em.find(Employee.class, employeeId);
    }
}
