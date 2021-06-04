package ru.nanikon.FlatCollection.exceptions;

/**
 * Indicates that the number is non-positive. Many fields have a requirement to be greater than 0
 */

public class NotPositiveNumberException extends RuntimeException {
    public NotPositiveNumberException(String message) {
        super(message);
    }
}
