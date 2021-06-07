package ru.nanikon.FlatCollection;

public class App {
    public static void main(String[] args) {
        String host = null;
        int port = 0;
        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Порт должен быть числом");
            System.exit(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Ошибка! Вы не ввели все аргументы: хост и порт");
            System.exit(0);
        }
        Client client = new Client(host, port);
        //Client client = new Client("localhost", 3999);
        client.start();
        client.run();
    }
}
