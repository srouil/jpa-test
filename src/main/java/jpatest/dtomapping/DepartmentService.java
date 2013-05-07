package jpatest.dtomapping;

public interface DepartmentService {

    public <T extends DepartmentLightDTO> T findDepartmentById(Long id, Class<T> dtoClass);

    public DepartmentFullDTO createDepartment(DepartmentFullDTO departmentDTO);

    /**
     * Parameter and return type is defined as DepartmentLightDTO. 
     * However the actual type can be DepartmentLightDTO or DepartmentFullDTO depending on what attributes
     * need to be updated. 
     */
    public DepartmentLightDTO updateDepartment(DepartmentLightDTO departmentDTO);

    public void deleteDepartment(DepartmentLightDTO departmentDTO);

    public EmployeeDTO findEmployeeById(Long id);

    public ProjectDTO findProjectById(Long id);

    public CompanyDTO findCompanyById(Long id);
}
