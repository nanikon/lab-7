package ru.nanikon.FlatCollection;

import ru.nanikon.FlatCollection.commands.Command;
import ru.nanikon.FlatCollection.utils.Receiver;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

public class ReadingTask implements Runnable {
    private Receiver receiver;
    private ExecutorService processThreads;
    private ProcessingTask processingTask;
    private SendingTask sendingTask;
    private HashMap<?, ?> map;
    public ReadingTask(Receiver receiver, ExecutorService processThreads, ProcessingTask processingTask, SendingTask sendingTask, HashMap<?, ?> map) {
        this.receiver = receiver;
        this.processingTask = processingTask;
        this.processThreads = processThreads;
        this.sendingTask = sendingTask;
        this.map = map;
    }
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            Command command = receiver.receiveCommand();
            Server.logger.info("Команда принята");
            System.out.println("Команда принята");
            processingTask.setCommand(command);
            Server.logger.info("Команда принята, начинает исполнятся");
            processThreads.execute(processingTask);
        } catch (ClassCastException | ClassNotFoundException e) {
            sendingTask.setSendMap(map);
            Server.logger.info("Первая итерация, сейчас будет отправляться мапа");
            new Thread(sendingTask).start();
        } catch (IOException e) {
            Server.logger.error("Клиент отвалился на чтении");
        }
    }
}
