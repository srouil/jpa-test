package jpatest.ordercolumn;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PrintQueue.class)
public abstract class PrintQueue_ {

	public static volatile SingularAttribute<PrintQueue, Long> id;
	public static volatile ListAttribute<PrintQueue, PrintJob> jobs;
	public static volatile SingularAttribute<PrintQueue, String> name;

}

