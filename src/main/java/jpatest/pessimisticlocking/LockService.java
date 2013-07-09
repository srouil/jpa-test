package jpatest.pessimisticlocking;

public interface LockService {

    void lock(String resourceName);
}
