package ru.nanikon.FlatCollection.utils;

import ru.nanikon.FlatCollection.commands.Command;

import java.io.*;
import java.net.Socket;

public class Sender {
    private Socket socket;

    public Sender(Socket socket) {
        this.socket = socket;
    }

    public void sendString(String message) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            //oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(Command command) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(command);
            //oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}