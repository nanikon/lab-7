package ru.nanikon.FlatCollection.arguments;

import java.util.HashMap;

/**
 * Superclass of all object arguments.
 * @param <T> type of argument value
 */
abstract public class ObjectArgument<T> extends AbstractArgument<T> {
    protected HashMap<String[], ThrowConsumer<String>> params = new HashMap<>();

    /**
     * Checks the entered argument and arranges additional work
     * @param value a file read or from the console a line with an argument value
     */
    abstract public void setValue(String value);

    /**
     * Clear builder
     */
    abstract public void clear();

    /**
     * @return Map of name and value of object's field
     */
    public HashMap<String[], ThrowConsumer<String>> getParams() {
        return params;
    }

    /**
     * @return True
     */
    public boolean isObject() {
        return true;
    }

    /**
     * @return False
     */
    public boolean isEnum() {
        return false;
    }
}
