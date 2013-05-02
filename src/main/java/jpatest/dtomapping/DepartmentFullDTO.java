package jpatest.dtomapping;

import java.util.ArrayList;
import java.util.List;

public class DepartmentFullDTO extends DepartmentLightDTO {

    private List<EmployeeDTO> employees = new ArrayList<EmployeeDTO>();

    private List<ProjectDTO> projects = new ArrayList<ProjectDTO>();

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }

    public List<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDTO> projects) {
        this.projects = projects;
    }

}
