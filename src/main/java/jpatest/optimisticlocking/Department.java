package jpatest.optimisticlocking;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

//@formatter:off
@Entity
@NamedQuery(
    name = Department.SELECT_DEPARTMENT_BY_ID,             
    query = "select d from Department d " +
            "left join fetch d.employees " +
            "where d.id = :departmentId")
//@formatter:on
public class Department extends BaseEntity {

    public static final String SELECT_DEPARTMENT_BY_ID = "selectDepartmentById";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<Employee> employees = new ArrayList<Employee>();

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

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

}
