package ru.nanikon.FlatCollection.exceptions;

/**
 * Indicates an error when entering when you need to answer a Yes/No question
 */
public class BooleanInputException extends Exception {
    public BooleanInputException(String message) {
        super(message);
    }
}
