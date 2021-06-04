package ru.nanikon.FlatCollection.data;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Сlass whose instance collection is managed by the collection
 */

public class Flat implements Comparable<Flat>, Serializable {
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

    Flat(int id, String name, Coordinates coordinates, java.time.ZonedDateTime creationDate, long area, Integer numberOfRooms, boolean centralHeating, View view, Transport transport, House house) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.centralHeating = centralHeating;
        this.view = view;
        this.transport = transport;
        this.house = house;
        this.creationDate = creationDate;
    }

    @Override
    public int compareTo(Flat o) {
        return id - o.getId();
    }

    public String getName() { return name; }

    public int getId() {
        return id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public double getX() { return getCoordinates().getX(); }
    public Double getY() { return getCoordinates().getY(); }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public long getArea() {
        return area;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public boolean isCentralHeating() {
        return centralHeating;
    }

    public View getView() {
        return view;
    }

    public Transport getTransport() {
        return transport;
    }

    public House getHouse() {
        return house;
    }

    public String getHouseName() { return getHouse().getName(); }
    public Long getYear() { return getHouse().getYear(); }
    public Integer getNumberOfFloors() { return getHouse().getNumberOfFloors(); }

    public String toLongString() {
        StringBuilder info = new StringBuilder();
        info.append("Id: " + getId());
        info.append(", Название: " + getName());
        info.append(", Координата Х:" + getCoordinates().getX());
        info.append(", Координата Y:" + getCoordinates().getY());
        if (getCreationDate() == null) {
            info.append(", Дата и время добавления в коллекцию: " + getCreationDate());
        } else {
            info.append(", Дата и время добавления в коллекцию: " + getCreationDate().toString().substring(0, 10) + " " + getCreationDate().toString().substring(11, 19));
        }
        info.append(", Площадь: " + getArea());
        info.append(", Количество комнат: " + getNumberOfRooms());
        if (isCentralHeating()) {
            info.append(", Центральное отопление: есть");
        } else {
            info.append(", Центральное отопление: нет");
        };
        info.append(", Вид: " + getView().name());
        info.append(", Уровень транспортного шума: " + getTransport().name());
        info.append(", Находится в доме " + getHouse().getName() + " " + getHouse().getYear() + " года постройки, в котором всего " + getHouse().getNumberOfFloors() + " квартир");
        return info.toString();
    }
}
