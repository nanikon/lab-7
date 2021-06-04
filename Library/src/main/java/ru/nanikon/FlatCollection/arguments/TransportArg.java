package ru.nanikon.FlatCollection.arguments;

import ru.nanikon.FlatCollection.data.Transport;

/**
 * Matches the Transport enum and checks that the entered value is one of the constants
 */
public class TransportArg extends EnumArgument<Transport> {
    /**
     * Checks the entered argument and arranges additional work if necessary
     * @param value a file read or from the console a line with an argument value
     */
    @Override
    public void setValue(String value) {
        try {
            this.value = Transport.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Поле транспорт не может принять значение " + value, e);
        } catch (NullPointerException e) {
            throw new NullPointerException("Поле транспорт не может быть null");
        }
    }

    /**
     * @return Enum values
     */
    @Override
    public String getConstants() {
        StringBuilder result = new StringBuilder();
        for (Transport transport : Transport.values()) {
            result.append(transport.name()).append(" ");
        }
        return result.toString();
    }
}
