package jpatest.dtomapping;

import java.util.HashSet;
import java.util.Set;

public class DepartmentDTO extends BaseDTO {

    private Long id;

    private String name;

    private Set<EmployeeDTO> employees = new HashSet<EmployeeDTO>();

    private Set<ProjectDTO> projects = new HashSet<ProjectDTO>();

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

    public Set<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmployeeDTO> employees) {
        this.employees = employees;
    }

    public Set<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectDTO> projects) {
        this.projects = projects;
    }

}
