package jpatest.dtomapping;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDTO extends BaseDTO {

    private Long id;

    private String name;

    private List<EmployeeDTO> employees = new ArrayList<EmployeeDTO>();

    private List<ProjectDTO> projects = new ArrayList<ProjectDTO>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
