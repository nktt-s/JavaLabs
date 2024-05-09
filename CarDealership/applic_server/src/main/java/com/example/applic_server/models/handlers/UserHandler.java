package com.example.applic_server.models.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.example.applic_server.models.ListToApplicationConverter;

abstract public class UserHandler implements Runnable {
    protected static final Logger server_logger = LogManager.getLogger("MainLogger");
    ListToApplicationConverter converter = new ListToApplicationConverter();
    public static ArrayList<UserHandler> userHandlers = new ArrayList<>();
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;
    protected Socket socket;
    protected String name = null;

    public UserHandler() {

    }

    public UserHandler(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
        userHandlers.add(this);
        server_logger.info("New client was connected");
    }

    public void closeEverything(Socket socket, ObjectInputStream bufferedWriter, ObjectOutputStream bufferedReader) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
            server_logger.info("Socket, output and input streams were closed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void removeClientHandler() {
        userHandlers.remove(this);
    }

    @Override
    public abstract void run();

    public abstract void send_applications();

}
