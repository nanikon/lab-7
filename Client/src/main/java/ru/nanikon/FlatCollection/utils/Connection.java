package ru.nanikon.FlatCollection.utils;

import ru.nanikon.FlatCollection.commands.Command;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Connection {
    private SocketChannel serverSocketChannel;
    private Selector selector;

    public void startConnection(String host, int serverPort) throws IOException {
        selector = Selector.open();
        serverSocketChannel = SocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_WRITE | SelectionKey.OP_READ);
        serverSocketChannel.connect(new InetSocketAddress(host, serverPort));
        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                if (key.isValid() && key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    return;
                }
            }
        }
    }

    public void sendString(String message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(message);
        ByteBuffer outBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                if (key.isValid() && key.isWritable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.write(outBuffer);
                    if (outBuffer.remaining() < 1) {
                        return;
                    }
                }
            }
        }
    }

    public void sendCommand(Command command) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(command);
        ByteBuffer outBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                if (key.isValid() && key.isWritable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.write(outBuffer);
                    if (outBuffer.remaining() < 1) {
                        return;
                    }
                }
            }
        }
    }

    public HashMap<String, Command> receiveMap() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isValid() && selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(10000000);
                    ByteBuffer outBuffer = ByteBuffer.allocate(10000000);
                    try {
                        while (socketChannel.read(byteBuffer) > 0) {
                            byteBuffer.flip();
                            outBuffer.put(byteBuffer);
                            byteBuffer.compact();
                            try {
                                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(outBuffer.array()));
                                return (HashMap<String, Command>) objectInputStream.readObject();
                            } catch (StreamCorruptedException ignored) {
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if (socketChannel.read(byteBuffer) == -1) { throw new IOException(); }
                    } catch (IOException e) {
                        throw e;
                    }
                }
                iterator.remove();
            }
        }
    }

    public String receive() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isValid() && selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(100000);
                    ByteBuffer outBuffer = ByteBuffer.allocate(100000);
                    try {
                        while (socketChannel.read(byteBuffer) > 0) {
                            byteBuffer.flip();
                            outBuffer.put(byteBuffer);
                            byteBuffer.compact();
                            try {
                                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(outBuffer.array()));
                                return (String) objectInputStream.readObject();
                            } catch (StreamCorruptedException ignored) {
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if (socketChannel.read(byteBuffer) == -1) { throw new IOException(); }
                    } catch (IOException e) {
                        throw e;
                    }
                }
                iterator.remove();
            }
        }
    }

    public void stopConnection() {
        try {
            serverSocketChannel.close();
        } catch (IOException ignored) {
        }
    }
}
