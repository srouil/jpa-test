package jpatest.pessimisticlocking;

public class LockingTimeoutException extends Exception {

    public LockingTimeoutException(String message) {
        super(message);
    }

    public LockingTimeoutException() {
        super();
    }
}
