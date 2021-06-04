package ru.nanikon.FlatCollection;

import ru.nanikon.FlatCollection.SendingTask;
import ru.nanikon.FlatCollection.commands.Command;
import ru.nanikon.FlatCollection.db.DBManager;

public class ProcessingTask implements Runnable {
    private Command command;
    private DBManager manager;
    private SendingTask sendingTask;

    public ProcessingTask(DBManager manager, SendingTask sendingTask) {
        this.manager = manager;
        this.sendingTask = sendingTask;
    }

    public void setCommand(Command command) {
        this.command = command;
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
        String answer = command.execute(manager);
        Server.logger.info("Команда " + command.getName() + " выполнилась с ответом: " + answer);
        sendingTask.setSendString(answer);
        new Thread(sendingTask).start();
    }
}
