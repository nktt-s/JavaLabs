package com.example.car_dealership_client.models;

import com.example.car_dealership_client.admin_controllers.AdmMainController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User {
    Integer total_id;


    public Admin(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        super(socket, "Admin", ois, oos);
    }


    @Override
    public void run() {
        while (socket.isConnected()) {
            List<ApplicationData> applicationsFromServer;
            try {
                List<String> workersFromServer = (List<String>) ois.readObject();
                List<String> applicationsString = (List<String>) ois.readObject();
                applicationsFromServer = converter.list_to_application(applicationsString);
                sortIncomingApplications(applicationsFromServer);
                AdmMainController.update_workers(workersFromServer);
            } catch (IOException e) {
                close_everything(socket, oos, ois);
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }


    public void sendApplicationToServer(ApplicationData applic) {
        try {
            List<String> list = converter.app_to_list(applic);
            oos.writeObject(list);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println("Error sending application to client");
            close_everything(socket, oos, ois);
        }
    }

    public void getApplicationsFromServer() {
//        try {
//            Object b = ois.readObject();
//            oos.writeObject("Gimme");
//            oos.flush();
//            List<ApplicationData> incoming_applics = converter.list_to_application((List<String>) ois.readObject());
//            sortIncomingApplications(incoming_applics);
//            System.out.println("***********************Ultimate applications shower: ");
//            incoming_applics.forEach(ApplicationData::print);
//            System.out.println("***********************End of Ultimate applications shower: ");
//        }
//        catch (IOException e){
//            e.printStackTrace();
//            close_everything(socket,oos,ois);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void sendNameToServer(String name) throws IOException {
        try {
            oos.writeObject(name);
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
            close_everything(socket, oos, ois);
        }
    }

    public void sortIncomingApplications(List<ApplicationData> inc_applics) {
        List<ApplicationData> wait = new ArrayList<>();
        List<ApplicationData> progress = new ArrayList<>();
        List<ApplicationData> rejected = new ArrayList<>();
        List<ApplicationData> finished = new ArrayList<>();
        List<ApplicationData> cancelled = new ArrayList<>();
        for (ApplicationData app : inc_applics) {
//            System.out.println(app.get_status());
            switch (app.get_status()) {
                case "On Wait" -> wait.add(app);
                case "In Progress" -> progress.add(app);
                case "Rejected" -> rejected.add(app);
                case "Finished" -> finished.add(app);
                case "Cancelled" -> cancelled.add(app);
            }
        }
//            System.out.println("In sort incoming_applic = " + wait);
        AdmMainController.update_all_applics(wait, progress, rejected, finished, cancelled);


    }


}