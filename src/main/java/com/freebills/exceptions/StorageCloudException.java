package com.freebills.exceptions;

public class StorageCloudException extends RuntimeException {

    public StorageCloudException(String message) {
        super(message);
    }

    public StorageCloudException(String message, Throwable cause) {
        super(message, cause);
    }
}
