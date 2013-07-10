package jpatest.pessimisticlocking;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class SomeServiceImpl implements SomeService {

    @EJB
    LockService lockService;

    @Inject
    Logger logger;

    @Override
    public void doSomething(long duration) {

        try {
            lockService.lock("resource1");
        } catch (LockingTimeoutException lte) {

            logger.info("Lock timeout elapsed, I could eventually retry");
        }

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
