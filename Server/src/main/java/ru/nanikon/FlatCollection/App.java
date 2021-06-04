package ru.nanikon.FlatCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nanikon.FlatCollection.db.DBManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не найден!");
            System.exit(-1);
        }
        try (Scanner scr = new Scanner(new FileReader("config"))) {
            String url = scr.nextLine().trim();
            String login = scr.nextLine().trim();
            String password = scr.nextLine().trim();
            DBManager manager = new DBManager(url, login, password);
            int port = Integer.parseInt("3999");
            Server server = new Server(port, manager, logger);
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    server.stop();
                    manager.close();
                }
            }));
            server.start();
        } catch (FileNotFoundException e) {
            System.out.println("Не найден конфигуционный файл");
            System.exit(-1);
        } catch (NoSuchElementException e) {
            System.out.println("В конфигуционном файле не хватает строк.");
            System.exit(-1);
        } catch (NumberFormatException e) {
            logger.error("Ошибка! Порт должен быть числом");
            System.exit(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Не хватает аргументов. Первый - название конфигуционного файла для БД, второй - номер порта сервера.");
        }
    }
}
