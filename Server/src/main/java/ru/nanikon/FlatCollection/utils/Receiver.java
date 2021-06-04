package ru.nanikon.FlatCollection.utils;

import ru.nanikon.FlatCollection.commands.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Receiver {
    private Socket socket;
    public Receiver(Socket socket) throws IOException {
        this.socket = socket;
    }

    public String receiveString() throws ClassNotFoundException, IOException {
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        String result = (String) is.readObject();
        return result;
    }

    public Command receiveCommand() throws IOException, ClassNotFoundException {
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        Command command = (Command) is.readObject();
        return command;
    }
}
