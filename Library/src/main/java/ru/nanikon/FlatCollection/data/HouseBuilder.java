package ru.nanikon.FlatCollection.data;

import ru.nanikon.FlatCollection.exceptions.NotPositiveNumberException;

import java.io.Serializable;

/**
 * Builder House class. Checks the fields and creates an object
 */

public class HouseBuilder implements Serializable {
    private String name; //Поле может быть null
    private Long year; //Поле может быть null, Значение поля должно быть больше 0
    private Integer numberOfFloors; //Поле может быть null, Значение поля должно быть больше 0

    /**
     * sets default fields values
     */
    public void reset() {
        name = null;
        year = null;
        numberOfFloors = null;
    }

    /**
     * name = null if value == "" else name = value
     * @param name value read from file or console
     */
    public void setName(String name) {
        if (name == null) {
            this.name = name;
        } else if (name.equals("")) {
            this.name = null;
        } else {
            this.name = name;
        }
   }

    /**
     * Checks that value is a natural number or value is empty
     * @param year value read from file or console
     * @throws NotPositiveNumberException called if value &lt; 0
     */
    public void setYear(String year) throws NotPositiveNumberException {
       try {
           Long result = Long.valueOf(year);
           if (result <= 0) {
               throw new NotPositiveNumberException("Год основания дома должен быть целым числом больше нуля");
           }
           this.year = result;
       } catch (NumberFormatException e) {
           if (year == null) {
               this.year = null;
           } else if (year.equals("")){
               this.year = null;
           } else {
               throw new NumberFormatException("Поле год основания дома должно быть целым числом");
           }
       }
   }

    /**
     * Checks that value is a natural number or value is empty
     * @param numberOfFloors value read from file or console
     * @throws NotPositiveNumberException called if value &lt; 0
     */
    public void setNumberOfFloors(String numberOfFloors) throws NotPositiveNumberException {
       try {
           Integer result = Integer.valueOf(numberOfFloors);
           if (result <= 0) {
               throw new NotPositiveNumberException("Число квартир в доме должно быть целым числом больше нуля");
           }
           this.numberOfFloors = result;
       } catch (NumberFormatException e) {
           if (numberOfFloors == null) {
               this.numberOfFloors = null;
           } else if (numberOfFloors.equals("")){
               this.numberOfFloors = null;
           } else {
               throw e;
           }
       }
   }

    /**
     * @return Flat with set values
     */
    public House getResult() {
        return new House(this.name, this.year, this.numberOfFloors);
   }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return year
     */
    public Long getYear() {
        return year;
    }

    /**
     * @return numberOf Floors
     */
    public Integer getNumberOfFloors() {
        return numberOfFloors;
    }
}
