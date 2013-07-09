package jpatest.optimisticlocking;

import javax.persistence.LockModeType;

public interface DepartmentService {

    public Department findDepartmentById(Long departmentId);

    public Department findDepartmentById(Long departmentId, LockModeType lockModeType);

    public Department updateDepartment(Department department);

    public void setDepartmentNameRequiresNew(Long departmentId);

    public Employee findEmployeeById(Long employeeId);
}
