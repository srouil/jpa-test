package jpatest.updateid;

import javax.inject.Inject;

import jpatest.Resources;
import jpatest.updateid.Attachment;
import jpatest.updateid.Document;
import jpatest.updateid.DocumentServiceBean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class DocumentUpdateTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addClasses(Document.class, Attachment.class, DocumentServiceBean.class, Resources.class)
				.addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	DocumentServiceBean documentService;

	@Test
	public void testUpdateDocumentId() throws Exception {
		Document d = documentService.createDocument();
		
		documentService.updateDocumentId(d);
	}

}
