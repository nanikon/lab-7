package ru.nanikon.FlatCollection.arguments;

import ru.nanikon.FlatCollection.data.Flat;
import ru.nanikon.FlatCollection.exceptions.NotPositiveNumberException;
import ru.nanikon.FlatCollection.utils.CollectionManager;

/**
 * Checks that the entered value is a natural number and that an element with this id is in the collection
 */
public class IntArg extends AbstractArgument<Integer> {

    /**
     * Checks the entered argument
     * @param value a file read or from the console a line with an argument value
     */
    @Override
    public void setValue(String value) {
        try {
            int result = Integer.parseInt(value);
            if (result <= 0) {
                throw new NotPositiveNumberException("Аргумент id должен быть больше 0");
            }
            this.value = result;
        } catch (NumberFormatException e) {
            if (value.equals("")) {
                throw new NullPointerException("Аргумент id не найден");
            } else {
                throw new NumberFormatException("Аргумент id должен быть числом");
            }
        }
    }
}
