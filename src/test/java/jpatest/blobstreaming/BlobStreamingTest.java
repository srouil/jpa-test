package jpatest.blobstreaming;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
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
public class BlobStreamingTest {

    // testable = false to tell Arquillian that tests must be executed in client mode
    @Deployment(testable = false)
    public static Archive<?> createTestArchive() {
        //@formatter:off
        // Do dot add package but single classes, otherwise it adds this class to the war and this test has a dependency to Apache HTTP Client
        return ShrinkWrap.create(WebArchive.class, "test.war").addClass(LargeDocument.class).addClass(DocumentUploadServlet.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        //@formatter:on
    }

    /**
     * Test uploading a document to DocumentUploadServlet
     */
    @Test
    public void testUploadDocument() throws Exception {

        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        HttpPost post = new HttpPost("http://localhost:8080/test/document_upload");
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        // For File parameters
        File file = new File("src/test/resources/documents/persistence-2_0-final-spec.pdf");
        FileBody fileBody = new FileBody(file, "application/pdf");
        entity.addPart("file", fileBody);

        // For usual String parameters
        entity.addPart("description", new StringBody("A test document", "text/plain", Charset.forName("UTF-8")));

        post.setEntity(entity);
        String response = EntityUtils.toString(client.execute(post).getEntity(), "UTF-8");

        client.getConnectionManager().shutdown();
    }
}
