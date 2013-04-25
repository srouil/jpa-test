package jpatest.hashcodeequals;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    // Set to lazy to test equals comparison between persistent entity and proxy
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    // Implementation using ID
    //
    //    @Override
    //    public boolean equals(Object obj) {
    //        
    //        // Not a complete implementation!
    //        Employee e = (Employee) obj;
    //        return getId().equals(e.getId());
    //    }
    //    
    //    @Override
    //    public int hashCode() {
    //        return System.identityHashCode(getId());
    //    }
}
