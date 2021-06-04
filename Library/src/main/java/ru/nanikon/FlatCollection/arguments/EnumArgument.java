package ru.nanikon.FlatCollection.arguments;

/**
 * The superclass of all of the arguments-enum
 * @param <T> type of argument value
 */
abstract public class EnumArgument<T extends Enum<?>> extends AbstractArgument<T> {
    /**
     * @return False
     */
    public boolean isObject() {
        return false;
    }

    /**
     * @return True
     */
    public boolean isEnum() {
        return true;
    }

    /**
     * Checks the entered argument and arranges additional work if necessary
     * @param value a file read or from the console a line with an argument value
     */
    abstract public void setValue(String value);


    /**
     * @return Enum values
     */
    abstract public String getConstants();
}
