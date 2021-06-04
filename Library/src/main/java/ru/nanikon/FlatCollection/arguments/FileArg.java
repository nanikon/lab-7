package ru.nanikon.FlatCollection.arguments;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Checks that the file with the name that the user entered exists and can be opened for reading
 */
public class FileArg extends AbstractArgument<String> {
    /**
     * Checks the entered argument
     * @param value a file read or from the console a line with an argument value
     * @throws IOException called if the file does not exist, is unreadable, or does not have enough read permissions
     */
    @Override
    public void setValue(String value) throws IOException {
        try (Scanner scr = new Scanner(new File(value))) {
            this.value = value;
        } catch (IOException e) {
            throw new IOException("Файл с таким именем не удается найти или прочитать. Проверьте его и попробуйте ещё раз");
        }
    }
}
