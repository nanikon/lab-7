package ru.nanikon.FlatCollection;

public class App {
    public static void main(String[] args) {
        String host = null;
        int port = 0;
        String filename = null;
        /*try {
            host = args[0];
            port = Integer.parseInt(args[1]);
            filename = args[2];
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Порт должен быть числом");
            System.exit(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Ошибка! Вы не ввели все аргументы: хост, порт и имя файла");
            System.exit(0);
        }
        Client client = new Client(host, port, filename);*/
        Client client = new Client("localhost", 3999, "example.json");
        client.start();
        client.run();
    }
}
