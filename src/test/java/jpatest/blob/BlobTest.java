package jpatest.blob;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test uses JSON datasets since YAML does not seem to work (byte[] reference instead of value is compared)
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.DISABLED)
@Cleanup(phase = TestExecutionPhase.BEFORE)
public class BlobTest {

    // Encoded in base64: AAEDBAU=
    private static final byte[] DUMMY_BYTES = {0, 1, 3, 4, 5};
    
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Document.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    DocumentRepositoryBean documentRepository;

    // Used only for assertions, otherwise we use repository    
    @PersistenceContext
    EntityManager em;

    /**
     * Test showing the query of document list where binary data is not fetched
     */
    @Test
    @UsingDataSet("blob/initial.json")
    public void testFindAllDocuments() {

        // Given
        // Initial dataset

        // When
        List<Document> document = documentRepository.findAllDocuments();

        // Then
        assertEquals("Query returns 2 documents", 2, document.size());
        assertFalse("Document is in NEW state", em.contains(document.get(0)));
    }
    
    /**
     * Test showing update of binary data
     */
    @Test
    @UsingDataSet("blob/initial.json")
    @ShouldMatchDataSet("blob/expected.json")
    public void testUpdateDocument() {

        // Given
        // Initial dataset
        
        // When
        Document document = documentRepository.findDocumentById(1);
        document.setFileName("user_guide2.doc");
        document.setBinaryData(DUMMY_BYTES);

        // Document is detached => merge
        documentRepository.updateDocument(document);

        // Then
        // Expected dataset
        assertFalse("Document is in DETACHED state", em.contains(document));
    }
    
    // Some dummy comment to test Git ...
}