package com.example.car_dealership_client.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public abstract class User implements Runnable {
    protected ListToApplicationConverter converter = new ListToApplicationConverter();
    protected Socket socket;
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;


    public abstract void run();

    public User(Socket socket, String user_type, ObjectInputStream ois, ObjectOutputStream oos) {
        try {
            this.socket = socket;
            this.oos = oos;
            oos.writeObject(user_type);
            this.ois = ois;
        } catch (IOException e) {
//            System.out.println("Error creating server");
            e.printStackTrace();
            close_everything(socket, oos, ois);
        }
    }

    public abstract void sendApplicationToServer(ApplicationData applic);

    public abstract void getApplicationsFromServer();

    public abstract void sendNameToServer(String name) throws IOException;

    public abstract void sortIncomingApplications(List<ApplicationData> inc_apllics);

    public void stop_connection() {
        close_everything(socket, oos, ois);
    }

    public void close_everything(Socket socket, ObjectOutputStream bufferedWriter, ObjectInputStream bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
//                System.out.println("I'M CLOSING");
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
