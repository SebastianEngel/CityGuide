package com.github.sebastianengel.cityguide.domain;

/**
 * Exception thrown when there was a problem while interacting with the service / domain layer.
 *
 * @author Sebastian Engel
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

}