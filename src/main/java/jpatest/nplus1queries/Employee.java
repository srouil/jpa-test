package jpatest.nplus1queries;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
//@formatter:off
@NamedQueries({
    @NamedQuery(
            name = Employee.SELECT_ALL_EMPLOYEES,             
            query = "select e from Employee e"),
    @NamedQuery(
            name = Employee.SELECT_ALL_EMPLOYEES_FETCH_ALL,             
            query = "select e from Employee e left join fetch e.department left join fetch e.employeeInfo")
})
//@formatter:on
public class Employee {

    public static final String SELECT_ALL_EMPLOYEES = "selectAllEmployees";
    public static final String SELECT_ALL_EMPLOYEES_FETCH_ALL = "selectAllEmployeesFetchAll";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    @ManyToOne
    private Department department;

    @OneToOne
    @JoinColumn(name= "employee_info_id")
    private EmployeeInfo employeeInfo;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
}
