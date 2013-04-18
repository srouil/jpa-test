package jpatest.mapkeybyentity;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Company.class)
public abstract class Company_ {

	public static volatile SingularAttribute<Company, Long> id;
	public static volatile SingularAttribute<Company, String> name;
	public static volatile MapAttribute<Company, Department, Employee> departmentResponsibles;

}

