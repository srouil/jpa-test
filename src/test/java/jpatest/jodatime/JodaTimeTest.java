package jpatest.jodatime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
@Cleanup(phase = TestExecutionPhase.BEFORE)
public class JodaTimeTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

        //@formatter:off
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsLibraries(resolver.artifact("joda-time:joda-time:2.1").resolveAsFiles())
                .addAsLibraries(resolver.artifact("org.jadira.usertype:usertype.core:3.0.0.GA").resolveAsFiles())
                .addPackage(Event.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")                
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        //@formatter:on
    }

    @PersistenceContext(unitName = "jpaTestPU")
    EntityManager em;

    /**
     */
    @Test
    @ShouldMatchDataSet("jodatime/expected.yml")
    public void testPersist() {

        // Given

        // When
        Event e = new Event();
        e.setText("Hello world !!!");
        e.setCreateTs(new DateTime(2013, 05, 16, 17, 0));
        em.persist(e);

        // Then
        // Expected dataset
    }

}
