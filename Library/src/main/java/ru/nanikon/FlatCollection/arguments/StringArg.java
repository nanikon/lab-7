package ru.nanikon.FlatCollection.arguments;

import java.io.IOException;

public class StringArg extends AbstractArgument<String> {
    /**
     * Checks the entered argument and arranges additional work if necessary
     *
     * @param value a file read or from the console a line with an argument value
     * @throws IOException called if the file does not exist, is unreadable, or does not have enough read permissions. Only in arg which work with files
     */
    @Override
    public void setValue(String value) throws IOException {
        this.value = value;
    }
}
