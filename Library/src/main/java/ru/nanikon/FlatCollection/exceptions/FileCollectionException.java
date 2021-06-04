package ru.nanikon.FlatCollection.exceptions;

/**
 * Indicates that the collection contained in the file is invalid
 */

public class FileCollectionException extends Exception{
    public FileCollectionException(String message) {
        super(message);
    }
}
