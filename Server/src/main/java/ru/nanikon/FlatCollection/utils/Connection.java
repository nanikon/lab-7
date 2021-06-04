package ru.nanikon.FlatCollection.utils;

import ru.nanikon.FlatCollection.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
    private ServerSocket ss;
    private Sender sender;
    private Receiver receiver;

    public boolean startConnection (ServerSocket ss) {
        try {
            this.ss = ss;
            Socket s = ss.accept();
            sender = new Sender(s);
            receiver = new Receiver(s);
            Server.logger.info("Клиент успешно подключился " + s);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Sender getSender() {return sender;}

    public Receiver getReceiver() {
        return receiver;
    }
}
