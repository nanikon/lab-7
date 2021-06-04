package ru.nanikon.FlatCollection;


import org.slf4j.Logger;
import ru.nanikon.FlatCollection.commands.*;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.exceptions.FileCollectionException;
import ru.nanikon.FlatCollection.exceptions.StopConnectException;
import ru.nanikon.FlatCollection.utils.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Server {
    private ServerSocket ss;
    private String filename;
    public static Logger logger;
    private DBManager manager;
    private boolean isStopped = false;

    public Server(int port, DBManager manager, Logger logger) {
        this.logger = logger;
        try {
            this.ss = new ServerSocket(port);
            logger.info("Сервер создан");
            this.manager = manager;
            manager.connectToDatabase();
            manager.initialCollection();
            logger.info("Соединение установлено");
        } catch (IOException e) {
            logger.error("Невозможно было создать сервер", e);
            System.exit(0);
        } catch (SQLException throwables) {
            logger.error("Не удается подключится к БД", throwables);
            System.exit(0);
        } catch (Exception e) {
            logger.error("БД уронили", e);
            System.exit(0);
        }
    }

    public void start() {
        ExecutorService readThreads = Executors.newFixedThreadPool(5);
        ForkJoinPool processThreads = ForkJoinPool.commonPool();
        while (!isStopped) {
            Connection connection = new Connection();
            while (!connection.startConnection(ss));
            SendingTask sendingTask = new SendingTask(connection.getSender());
            ProcessingTask processingTask = new ProcessingTask(manager, sendingTask);
            ReadingTask readingTask = new ReadingTask(connection.getReceiver(), processThreads, processingTask, sendingTask, loadCommand());
            readThreads.execute(readingTask);
            logger.info("Начинается прием команды");
        }
        try {
            readThreads.shutdown();
            readThreads.awaitTermination(3, TimeUnit.SECONDS);
            processThreads.shutdown();
            processThreads.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Не получается закрыть потоки");
        }
    }

    public void run() {
        String message = null;
        offer:
        while (true) {
            try {
                Socket s = ss.accept();
                logger.info("Клиент успешно подключился " + s);
                Sender sender = new Sender(s);
                logger.info("Создан отправитель для клиента");
                Receiver receiver = new Receiver(s);
                logger.info("Создан приёмщик для клиента");
                CollectionManager collectionManager = null;
                try {
                    filename = receiver.receiveString();
                    logger.info("От клиента получено имя файла {}", filename);
                    JsonLinkedListParser parser = new JsonLinkedListParser(filename);
                    collectionManager = new CollectionManager(parser);
                    logger.info("Создан менеджер коллекции");
                    HashMap<String, Command> commands = loadCommand();
                    sender.sendMap(commands);
                    logger.info("Клиенту отправлена мапа доступных ему команд");
                } catch (FileCollectionException | IOException e) {
                    sender.sendString(e.getMessage() + ". Проверьте его и запустите программу ещё раз.");
                    logger.error(e.getMessage() + " Работа завершена");
                    e.printStackTrace();
                    stopConnection(s);
                    continue offer;
                }
                boolean work = true;
                while (work) {
                    try {
                        Command command = receiver.receiveCommand();
                        logger.info("От клинета получена комманда {}", command.getName());
                        logger.info("Логин клиента: " + command.getLogin());
                        String answer = command.execute(manager);
                        logger.info("Команда исполнена и получен ответ {}", answer);
                        sender.sendString(answer);
                        logger.info("Ответ отправлен клиенту");
                    } catch (StopConnectException e) {
                        sender.sendString(e.getAnswer());
                        logger.info("Была получена команда о завершении. Клиенту "+ s +" отправлен сигнал о завершении {}", e.getAnswer());
                        work = false;
                        stopConnection(s);
                        logger.info("Соединение с клиентом завершено");
                    } catch (SocketException e) {
                        work = false;
                        logger.warn("Клиент отвалился " + s);
                        stopConnection(s);
                        logger.info("Соединение с клиентом завершено");
                    }
                }
            } catch (IOException e) {
                logger.warn("Клиент отвалился. Работа с ним завершена");
            } catch (ClassNotFoundException e) {
                logger.error("При десериализации не смог найти класс ", e);
            }
        }
    }

    public HashMap<String, Command> loadCommand() {
        HashMap<String, Command> commands = new HashMap<>();
        commands.put("help", new HelpCommand(commands));
        commands.put("show", new ShowCommand());
        commands.put("add", new AddCommand());
        commands.put("update", new UpdateCommand());
        commands.put("insert_at", new InsertCommand());
        commands.put("remove_by_id", new RemoveCommand());
        commands.put("exit", new ExitCommand());
        commands.put("average_of_number_of_rooms", new AverageOfNumberOfRoomsCommand());
        commands.put("clear", new ClearCommand());
        commands.put("filter_less_than_view", new FilterLessThanViewCommand());
        commands.put("info", new InfoCommand());
        commands.put("remove_any_by_transport", new RemoveAnyByTransportCommand());
        commands.put("sort", new SortCommand());
        commands.put("history", new HistoryCommand());
        commands.put("register", new RegisterCommand());
        commands.put("log_in", new LogInCommand());
        commands.put("log_out", new LogOutCommand());
        return commands;
    }

    public void stopConnection(Socket s) {
        try {
            s.close();
        } catch (IOException ignored) {
        }
    }

    public void stop() {
        try {
            ss.close();
        } catch (IOException ignored) {
        }
    }
}
