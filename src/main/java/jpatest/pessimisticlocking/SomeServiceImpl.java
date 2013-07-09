package jpatest.pessimisticlocking;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class SomeServiceImpl implements SomeService {

    @EJB
    LockService lockService;

    @Override
    public void doSomething(long duration) {

        try {
            lockService.lock("resource1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
