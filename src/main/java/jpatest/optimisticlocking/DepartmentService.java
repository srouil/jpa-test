package jpatest.optimisticlocking;

import javax.persistence.LockModeType;

public interface DepartmentService {

    public Department findDepartmentById(Long id);

    public Department findDepartmentById(Long id, LockModeType lockModeType);

    public Department updateDepartment(Department department);

    public void setDepartmentNameRequiresNew(Long departmentId);

}
