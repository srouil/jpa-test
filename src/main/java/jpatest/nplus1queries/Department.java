package jpatest.nplus1queries;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
//@formatter:off
@NamedQueries({
    @NamedQuery(
        name = Department.SELECT_ALL_DEPARTMENTS,             
        query = "select d from Department d"),
    @NamedQuery(
        name = Department.SELECT_ALL_DEPARTMENTS_FETCH_ALL,             
        query = "select DISTINCT d from Department d " +
                "left join fetch d.employees e left join fetch e.employeeInfo " +
                "left join fetch d.projects")
})
//@formatter:on
public class Department {

    public static final String SELECT_ALL_DEPARTMENTS = "selectAllDepartments";
    public static final String SELECT_ALL_DEPARTMENTS_FETCH_ALL = "selectAllDepartmentsFetchAll";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department")
    private Set<Employee> employees = new HashSet<Employee>();

    @ManyToMany
    @JoinTable(name = "department_project", joinColumns = @JoinColumn(name = "department_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects = new HashSet<Project>();

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Set<Project> getProjects() {
        return projects;
    }

}
