package ru.nanikon.FlatCollection.arguments;

import java.io.IOException;
import java.io.Serializable;

/**
 * Superclass of all arguments. It and its heirs are required to validate user-entered arguments before passing them to commands.
 * @param <T> type of argument value
 */
abstract public class AbstractArgument<T> implements Serializable {
    protected T value;

    /**
     * @return argument value
     */
    public T getValue() {
        return value;
    }

    /**
     * Checks the entered argument and arranges additional work if necessary
     * @param value a file read or from the console a line with an argument value
     * @throws IOException called if the file does not exist, is unreadable, or does not have enough read permissions. Only in arg which work with files
     */
    abstract public void setValue(String value) throws IOException;

    /**
     * @return True if this argument value is Object, false if no
     */
    public boolean isObject() {
        return false;
    }

    /**
     * @return True if this argument value is Object, false if no
     */
    public boolean isEnum() {
        return false;
    }

    public String getName() {
        return "name";
    }
}
