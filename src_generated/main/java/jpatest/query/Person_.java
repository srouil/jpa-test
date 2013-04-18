package jpatest.query;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Person.class)
public abstract class Person_ {

	public static volatile SingularAttribute<Person, Long> id;
	public static volatile SingularAttribute<Person, String> lastName;
	public static volatile SetAttribute<Person, Category> categories;
	public static volatile SingularAttribute<Person, BigDecimal> salary;
	public static volatile SingularAttribute<Person, String> firstName;

}

