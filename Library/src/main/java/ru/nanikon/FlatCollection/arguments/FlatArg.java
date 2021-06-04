package ru.nanikon.FlatCollection.arguments;

import ru.nanikon.FlatCollection.data.Flat;
import ru.nanikon.FlatCollection.data.FlatBuilder;
import ru.nanikon.FlatCollection.data.Transport;
import ru.nanikon.FlatCollection.data.View;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Arrays;

/**
 * Corresponds to the Flat object, contains a convenient way to check the fields and the necessary messages for output
 */
public class FlatArg extends ObjectArgument<Flat> implements Serializable {
    private FlatBuilder builder = new FlatBuilder();
    {
        params.put(new String[]{"1", "имя квартиры", "Это должна быть непустая строка"}, builder::setName);
        params.put(new String[]{"2", "координата x", "Это должно быть целое или вещественное число с точкой в виде десятичной дроби"}, builder::setX);
        params.put(new String[]{"3", "координата y", "Это должно быть целое или вещественное число с точкой в виде десятичной дроби"}, builder::setY);
        params.put(new String[]{"4", "площадь квартиры", "Это должно быть целое число больше нуля, не превышающее " + Long.MAX_VALUE}, builder::setArea);
        params.put(new String[]{"5", "количество комнат в квартире", "Это должно быть целое число больше нуля, не превышающее " + Integer.MAX_VALUE + " или пустая строка"}, builder::setNumberOfRooms);
        params.put(new String[]{"6", "наличие центрального отопления", "Если в квартире есть центральное отопление, введите +, иначе введите -"}, builder::setCentralHeating);
        params.put(new String[]{"7", "вид из окон квартиры", "Это должен быть один из следующих вариантов: " + Arrays.toString(View.values())}, builder::setView);
        params.put(new String[]{"8", "загруженность траспорта рядом с квартирой", "Это должен быть один из следующих вариантов: " + Arrays.toString(Transport.values())}, builder::setTransport);
        params.put(new String[]{"9", "имя дома, в котором находится квартира", "Это должна быть любая строка, в том числе пустая"}, builder::setHouseName);
        params.put(new String[]{"10", "год строительтсва дома", "Это должно быть целое число больше нуля и не превышающее " + Long.MAX_VALUE + "или пустая строка"}, builder::setYear);
        params.put(new String[]{"11", "количество квартир в доме", "Это должно быть целое число больше нуля, не превышающее " + Integer.MAX_VALUE + " или пустая строка"}, builder::setNumberOfFloors);
    }
    /**
     * Sets the argument value as the result of the builder
     * @param value not required in this implementation
     */
    @Override
    public void setValue(String value) {
        builder.setCreationDate(ZonedDateTime.now());
        this.value = builder.getResult();
    }

    /**
     * Clear builder and value
     */
    @Override
    public void clear() {
        builder.reset();
        value = null;
    }

    /**
     * @return The builder of this object argument
     */
    public FlatBuilder getBuilder() { return builder; }

    @Override
    public String getName() {
        return "flat";
    }
}
