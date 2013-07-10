package jpatest.inheritance;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;

//@formatter:off
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NamedQuery( 
        name = Subject.SELECT_SUBJECTS_BY_NAME_LIKE, 
        query = "select s from Subject s where lower(name) like :namePattern")
//@formatter:on
public abstract class Subject {

    public static final String SELECT_SUBJECTS_BY_NAME_LIKE = "selectSubjectsByNameLike";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    // Further common attributes: addresses, ...
}
