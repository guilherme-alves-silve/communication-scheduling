package br.com.communication.scheduling.domain.exception;

public class NotFoundExceptionDomain extends RuntimeException {

    public NotFoundExceptionDomain() {
    }

    public NotFoundExceptionDomain(String message) {
        super(message);
    }
}
