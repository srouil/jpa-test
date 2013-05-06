package jpatest.dtomapping;

public interface DepartmentService {

    public <T extends DepartmentLightDTO> T findDepartmentById(Long id, Class<T> dtoClass);

    public DepartmentFullDTO createDepartment(DepartmentFullDTO departmentDTO);

    public void updateDepartment(DepartmentLightDTO departmentDTO);

    public void deleteDepartment(DepartmentLightDTO departmentDTO);

    public EmployeeDTO findEmployeeById(Long id);

    public ProjectDTO findProjectById(Long id);

    public CompanyDTO findCompanyById(Long id);
}
