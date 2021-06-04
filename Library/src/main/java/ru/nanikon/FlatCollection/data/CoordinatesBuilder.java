package ru.nanikon.FlatCollection.data;

import java.io.Serializable;

/**
 * Builder Coordinates class. Checks the fields and creates an object
 */
public class CoordinatesBuilder implements Serializable {
    private double x;
    private Double y;

    /**
     * Checks that value if Float
     * @param x value read from file or console
     */
    public void setX(String x) {
        try {
            this.x = Double.parseDouble(x);
        } catch (NumberFormatException e) {
            if (x.equals("")) {
                throw new NullPointerException("Поле координата х не может быть пустым");
            } else {
                throw new NumberFormatException("Поле координата х должно быть вещественным числом");
            }
        }
    }

    /**
     * Checks that value if Float
     * @param y value read from file or console
     */
    public void setY(String y) {
        try {
            this.y = Double.valueOf(y);
        } catch (NumberFormatException e) {
            if (y.equals("")) {
                throw new NullPointerException("Поле координата y не может быть пустым");
            } else {
                throw new NumberFormatException("Поле координата y должно быть вещественным числом");
            }
        }
    }

    /**
     * @return Coordinates with set values
     */
    public Coordinates getResult() {
        return new Coordinates(x, y);
    }

    /**
     * sets default fields values
     */
    public void reset() {
        x = 0;
        y = null;
    }

    /**
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * @return y
     */
    public Double getY() {
        return y;
    }
}
