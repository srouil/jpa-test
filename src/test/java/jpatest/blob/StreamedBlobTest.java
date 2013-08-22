package jpatest.blob;

import java.io.InputStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.BEFORE)
public class StreamedBlobTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(LargeDocument.class.getPackage()).addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    /**
     * Test showing 
     * 
     *
     */
    @Test
    public void testCreateDocument() {

        // Given
        // Initial dataset

        // When
        EntityManager em = emf.createEntityManager();
        InputStream imageIs = null;
        try {
            HibernateEntityManager hem = em.unwrap(HibernateEntityManager.class);
            Session session = hem.getSession();

            LargeDocument document = new LargeDocument();
            document.setFileName("insinia.jpg");
            document.setFileName("a small image");

            imageIs = getClass().getClassLoader().getResourceAsStream("images/insinia.jpg");
            document.setBinaryData(Hibernate.getLobCreator(session).createBlob(imageIs, -1));

            // TODO TX handling
            em.persist(document);
        } finally {
            em.close();
            IOUtils.closeQuietly(imageIs);
        }

        // Then
        // Expected dataset
    }
}
