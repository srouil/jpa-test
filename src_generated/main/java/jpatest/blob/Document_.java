package jpatest.blob;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Document.class)
public abstract class Document_ {

	public static volatile SingularAttribute<Document, Long> id;
	public static volatile SingularAttribute<Document, String> description;
	public static volatile SingularAttribute<Document, String> fileName;
	public static volatile SingularAttribute<Document, byte[]> binaryData;
	public static volatile SingularAttribute<Document, Integer> version;

}

