package ru.nanikon.FlatCollection.arguments;

import ru.nanikon.FlatCollection.data.View;

/**
 * Matches the View enum and checks that the entered value is one of the constants
 */
public class ViewArg extends EnumArgument<View> {
    /**
     * Checks the entered argument and arranges additional work if necessary
     * @param value a file read or from the console a line with an argument value
     */
    @Override
    public void setValue(String value) {
        try {
            this.value = View.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Поле вид не может принять значение " + value, e);
        } catch (NullPointerException e) {
            throw new NullPointerException("Поле вид не может быть null");
        }
    }

    /**
     * @return Enum values
     */
    @Override
    public String getConstants() {
        StringBuilder result = new StringBuilder();
        for (View view : View.values()) {
            result.append(view.name()).append(" ");
        }
        return result.toString();
    }
}
