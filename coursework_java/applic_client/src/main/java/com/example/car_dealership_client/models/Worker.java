package com.example.car_dealership_client.models;

//import com.example.applic_client.admin_controllers.AdmMainController;
import com.example.car_dealership_client.seller_controllers.WorkerMainController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Worker extends User{
    Integer total_id;


    public Worker(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        super(socket, "Worker", ois, oos);
    }


    @Override
    public void run(){
        while(socket.isConnected()){
            List<ApplicationData> applicationsFromServer;
            try {
//                System.out.println("At least I was in run()");
                List<String> applicationsString = (List<String>) ois.readObject();
                applicationsFromServer = converter.list_to_application(applicationsString);
//                sortIncomingApplications(applicationsFromServer);
                WorkerMainController.update_all_applications(applicationsFromServer);
            } catch (IOException e) {
                close_everything(socket,oos,ois);
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

    public void getApplicationsFromServer(){
        try {
            Object b = ois.readObject();
            oos.writeObject("Gimme");
            oos.flush();
            List<ApplicationData> incoming_applics = converter.list_to_application((List<String>) ois.readObject());
            sortIncomingApplications(incoming_applics);
//            System.out.println("***********************Ultimate applications shower: ");
            incoming_applics.forEach(ApplicationData::print);
//            System.out.println("***********************End of Ultimate applications shower: ");
        }
        catch (IOException e){
            e.printStackTrace();
            close_everything(socket,oos,ois);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
//        List<ApplicationData> progress = new ArrayList<>();
//        WorkerMainController.update_all_applications(progress);
    }
}
