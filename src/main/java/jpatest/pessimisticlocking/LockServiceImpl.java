package jpatest.pessimisticlocking;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;

@Stateless
public class LockServiceImpl implements LockService {

    private static Map<String, Object> properties = new HashMap<String, Object>();

    private static final int TIMEOUT = 30;

    static {
        properties.put("javax.persistence.lock.timeout", TIMEOUT * 1000);
    }

    @PersistenceContext
    EntityManager em;

    @Override
    public void lock(String resourceName) throws LockingTimeoutException {

        Resource resource = em.find(Resource.class, resourceName);

        if (resource == null) {

            // Create resource and tries again to lock it
            resource = new Resource();
            resource.setName(resourceName);
            try {
                em.persist(resource);
                em.flush();
            } catch (PersistenceException pe) {
                if ((pe.getCause() != null) && (pe.getCause() instanceof ConstraintViolation)) {

                    // Another transaction has persisted the same resource, ignore it
                } else {
                    throw pe;
                }
            }
        }

        try {
            em.lock(resource, LockModeType.PESSIMISTIC_READ, properties);
        } catch (LockTimeoutException lte) {
            throw new LockingTimeoutException("Cannot lock resource within timeout of " + TIMEOUT + " s.");
        }
    }
}
