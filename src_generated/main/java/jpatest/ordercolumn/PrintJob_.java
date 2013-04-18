package jpatest.ordercolumn;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PrintJob.class)
public abstract class PrintJob_ {

	public static volatile SingularAttribute<PrintJob, String> content;
	public static volatile SingularAttribute<PrintJob, Long> id;
	public static volatile SingularAttribute<PrintJob, PrintQueue> queue;

}

