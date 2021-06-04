package ru.nanikon.FlatCollection.data;

import ru.nanikon.FlatCollection.exceptions.BooleanInputException;
import ru.nanikon.FlatCollection.exceptions.NotPositiveNumberException;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;

/**
 * Builder Flat class. Checks the fields and creates an object. Contains the Coordinates and House builders
 */

public class FlatBuilder implements Serializable {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long area; //Значение поля должно быть больше 0
    private Integer numberOfRooms; //Поле может быть null, Значение поля должно быть больше 0
    private boolean centralHeating;
    private View view; //Поле не может быть null
    private Transport transport; //Поле не может быть null
    private House house; //Поле не может быть null
    private CoordinatesBuilder coordsBuilder = new CoordinatesBuilder();
    private HouseBuilder houseBuilder = new HouseBuilder();
    private HashSet<String> changed = new HashSet<>();

    /**
     * Checks that the entered value is not empty and a natural number. Use only in work with file
     * @param value value read from file
     * @throws NotPositiveNumberException if id &lt; 1
     */
    public void setId(String value) throws NotPositiveNumberException {
        try {
            int id = Integer.parseInt(value);
            if (id < 1) {
                throw new NotPositiveNumberException("Поле id должно быть целым положительным числом");
            }
            this.id = id;
            changed.add("id");
        } catch (NumberFormatException e) {
            if (value.equals("")) {
                throw new NullPointerException("Поле id не может быть пустым");
            } else {
                throw new NumberFormatException("Поле id должно быть целым положительным числом");
            }
        }
    }

    /**
     * Checked that the ntered value is not empty
     * @param name value read from file or console
     */
    public void setName(String name) {
        if (name.equals("")) {
            throw new NullPointerException("Поле имя квартиры не может быть пустым");
        } else {
            this.name = name;
            changed.add("name");
        }
    }

    /**
     * implemented in CoordinatiesBuilder
     * @param value value read from file or console
     */
    public void setX(String value) {
        coordsBuilder.setX(value);
        changed.add("x");
    }

    /**
     * implemented in CoordinatiesBuilder
     * @param value value read from file or console
     */
    public void setY(String value) {
        coordsBuilder.setY(value);
        changed.add("y");
    }

    /**
     * Checks that the value is a natural number
     * @param value value read from file or console
     * @throws NotPositiveNumberException if value &lt; 0
     */
    public void setArea(String value) throws NotPositiveNumberException {
        try {
            long result = Long.parseLong(value);
            if (result <= 0) {
                throw new NotPositiveNumberException("Площадь квартиры должна быть целым числом больше нуля");
            } else {
                this.area = result;
                changed.add("area");
            }
        } catch (NumberFormatException e) {
            if (value.equals("")) {
                throw new NullPointerException("Поле площадь квартиры не может быть пустым");
            } else {
                throw new NumberFormatException("Поле площадь квартиры быть целым числом");
            }
        }
    }

    /**
     * Checks that the value is a natural number
     * @param value value read from file or console
     * @throws NotPositiveNumberException if value &lt; 0
     */
    public void setNumberOfRooms(String value) throws NotPositiveNumberException {
        try {
            Integer result = Integer.valueOf(value);
            if (result <= 0) {
                throw new NotPositiveNumberException("Число комнат в квартире должно быть целым положительным числом");
            } else {
                this.numberOfRooms = result;
                changed.add("numberOfRooms");
            }
        } catch (NumberFormatException e) {
            if (value.equals("")) {
                this.numberOfRooms = null;
                changed.add("numberOfRooms");
            } else {
                throw new NumberFormatException("Поле число комнат должно быть целым числом");
            }
        }
    }

    /**
     * Checks that value is + or -
     * @param value value read from file or console
     * @throws BooleanInputException if value not + or -
     */
    public void setCentralHeating(String value) throws BooleanInputException {
        if (value.equals("+")) {
            centralHeating = true;
            changed.add("centralHeating");
        } else if (value.equals("-")) {
            centralHeating = false;
            changed.add("centralHeating");
        } else {
            throw new BooleanInputException("Ожидалось +/-, встречено " + value);
        }
    }

    /**
     * Checks that value in Enum's values
     * @param value value read from file or console
     */
    public void setView(String value) {
        try {
            this.view = View.valueOf(value);
            changed.add("view");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Поле вид не может принять значение " + value, e);
        } catch (NullPointerException e) {
            throw new NullPointerException("Поле вид не может быть null");
        }
    }

    /**
     * Checks that value in Enum's values
     * @param value value read from file or console
     */
    public void setTransport(String value) {
        try {
            this.transport = Transport.valueOf(value);
            changed.add("transport");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Поле транспорт не может принять значение " + value, e);
        } catch (NullPointerException e) {
            throw new NullPointerException("Поле транспорт не может быть null");
        }
    }

    /**
     * implemented in HouseBuilder
     * @param value value read from file or console
     */
    public void setHouseName(String value) {
        houseBuilder.setName(value);
        changed.add("nameHouse");
    }

    /**
     * implemented in HouseBuilder
     * @param value value read from file or console
     */
    public void setYear(String value) throws NotPositiveNumberException {
        houseBuilder.setYear(value);
        changed.add("year");
    }

    /**
     * implemented in HouseBuilder
     * @param value value read from file or console
     */
    public void setNumberOfFloors(String value) throws NotPositiveNumberException {
        houseBuilder.setNumberOfFloors(value);
        changed.add("numberOfFloors");
    }

    /**
     * set entered creationDate. Use in work with file and in update
     * @param creationDate date and time when object was created
     */
    public void setCreationDate(java.time.ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * sets default fields values
     */
    public void reset() {
        id = 0;
        name = null;
        coordinates = null;
        creationDate = null;
        area = 0;
        numberOfRooms = 0;
        centralHeating = false;
        view = null;
        transport = null;
        house = null;
        coordsBuilder.reset();
        houseBuilder.reset();
        changed = new HashSet<>();
    }

    /**
     * @return Flat with set values
     */
    public Flat getResult() { return new Flat(id, name, coordsBuilder.getResult(), creationDate, area, numberOfRooms, centralHeating, view, transport, houseBuilder.getResult()); }

    /**
     * @return CoordinatesBuilder which link with this flat
     */
    public CoordinatesBuilder getCoordsBuilder() { return coordsBuilder; }

    /**
     * @return HouseBuilder which link with this flat
     */
    public HouseBuilder getHouseBuilder() { return houseBuilder; }

    /**
      * @return Map from field names and true, if field is modifed, else false
     */
    public HashSet<String> getChange() {
        return changed;
    }

    /**
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Date and time when this object create
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * @return Area
     */
    public long getArea() {
        return area;
    }

    /**
     * @return number of Tooms
     */
    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    /**
     * @return true if centralHeating, else false
     */
    public boolean isCentralHeating() {
        return centralHeating;
    }

    /**
     * @return View
     */
    public View getView() {
        return view;
    }

    /**
     * @return Transport
     */
    public Transport getTransport() {
        return transport;
    }

    /**
     * @return House
     */
    public House getHouse() {
        return house;
    }

    /**
     * @return x from coordinates
     */
    public double getX() { return coordsBuilder.getX(); }

    /**
     * @return y from coordinates
     */
    public Double getY() { return coordsBuilder.getY(); }

    /**
     * @return name from house
     */
    public String getHouseName() { return houseBuilder.getName(); }

    /**
     * @return year from house
     */
    public Long getYear() { return houseBuilder.getYear(); }

    /**
     * @return numberOfFloors from house
     */
    public Integer getNumberOfFloors() { return houseBuilder.getNumberOfFloors(); }
}
