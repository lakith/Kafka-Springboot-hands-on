package com.kafkadeveloper.ws.EmailNotificationService.error;

public class NotRetryableException extends RuntimeException{
    public NotRetryableException(String message) {
        super(message);
    }

    public NotRetryableException(Throwable cause) {
        super(cause);
    }
}
