package jpatest.nplus1queries;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee_info")
public class EmployeeInfo {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String additionalInfo;
    
}
