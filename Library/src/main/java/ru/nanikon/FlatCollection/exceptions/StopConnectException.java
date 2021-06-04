package ru.nanikon.FlatCollection.exceptions;

public class StopConnectException extends RuntimeException {
    public String getAnswer() {
        return "Работа программы завершена. Коллекция была сохранена";
    }
}
