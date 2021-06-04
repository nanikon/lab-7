package ru.nanikon.FlatCollection;

import ru.nanikon.FlatCollection.utils.Sender;

import java.io.IOException;
import java.util.HashMap;

public class SendingTask implements Runnable {
    private Sender sender;
    private String answer_String;
    private boolean isString;
    private HashMap<?, ?> map;


    public SendingTask(Sender sender) {
        this.sender = sender;
    }

    public void setSendString(String answer) {
        answer_String = answer;
        isString = true;
    }

    public void setSendMap(HashMap<?, ?> map) {
        this.map = map;
        isString = false;
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
            if (isString) {
                sender.sendString(answer_String);
                Server.logger.info("Отправлен ответ команды");
            } else {
                sender.sendMap(map);
                Server.logger.info("Отправлена мапа команд");
            }
            //sender.getSocket().close();
        } catch (IOException e) {
            Server.logger.warn("Проблемы со связью");
        }
    }
}
