package ru.nanikon.FlatCollection.utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Sender {
    private Socket socket;
    //private ObjectOutputStream os;

    public Sender(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void sendString(String message) throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        os.writeObject(message);
        //os.close();
    }

    public void sendMap(HashMap<?, ?> map) throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        os.writeObject(map);
        //os.close();
    }

    public Socket getSocket() {return socket;}
}
