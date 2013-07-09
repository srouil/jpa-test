package jpatest.pessimisticlocking;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;

@Stateless
public class LockServiceImpl implements LockService {

    @PersistenceContext
    EntityManager em;

    @Override
    public void lock(String resourceName) {

        Resource resource = em.find(Resource.class, resourceName, LockModeType.PESSIMISTIC_READ);

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

            em.find(Resource.class, resourceName, LockModeType.PESSIMISTIC_READ);
        }
    }
}
