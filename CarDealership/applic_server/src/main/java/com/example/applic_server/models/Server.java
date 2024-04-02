package com.example.applic_server.models;

import com.example.applic_server.models.handlers.LoginHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable {
    private static final Logger server_logger = LogManager.getLogger("MainLogger");
    private List<ApplicationData> waiting_applics;
    private ServerSocket serverSocket;
    //    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    boolean logged_in = false;


    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        server_logger.info("Server was initialized");

    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            Thread thread;
            Socket socket = new Socket();
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            LoginHandler loginHandler = new LoginHandler(socket, ois, oos, serverSocket);
            thread = new Thread(loginHandler);
            thread.start();

        }
    }


}


//    public void closeServerSocket(){
//        try{
//            if(serverSocket!=null){
//                serverSocket.close();
//            }
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    public void close_everything(Socket socket, ObjectInputStream bufferedWriter, ObjectOutputStream bufferedReader){
//        try {
//            if (bufferedReader != null) {
//                bufferedReader.close();
//            }
//            if (bufferedWriter != null) {
//                bufferedWriter.close();
//            }
//            if (socket != null) {
//                socket.close();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
//    public ApplicationData list_to_application(List<String> str_list){
//        return new ApplicationData(str_list.get(0), str_list.get(1),str_list.get(2),
//                str_list.get(3),str_list.get(4));
//    }
//
//    public List<String> app_to_list(ApplicationData applic){
//        List<String> list = new ArrayList<>();
//        list.add(applic.get_status());
//        list.add(applic.get_name());
//        list.add(applic.get_type());
//        list.add(applic.get_date());
//        list.add(applic.get_text());
//        return list;
//    }
//    public void sendApplicationsToClient(ApplicationData applic){
//       try{
//           oos.writeObject(app_to_list(applic));
//           oos.flush();
//       }
//       catch(IOException e){
//           e.printStackTrace();
//           System.out.println("Error sending application to client");
//           close_everything(socket,ois,oos);
//       }
//    }

//    public void receiveApplicationsFromClient(List<ApplicationData> waiting_applics){
//       new Thread(new Runnable() {
//           @Override
//           public void run() {
//               while(socket.isConnected()){
//                   ApplicationData applicationFromClient;
//                   try {
//                       applicationFromClient = list_to_application((List<String>) ois.readObject());
//                       System.out.println(applicationFromClient);
//                       SerMainController.add_application(waiting_applics, applicationFromClient);
//                       waiting_applics.get(waiting_applics.size()-1).print();
//                   }
//                   catch (IOException e) {
//                       e.printStackTrace();
//                       close_everything(socket,ois,oos);
//                       break;
//                   } catch (ClassNotFoundException e) {
//                       throw new RuntimeException(e);
//                   }
//
//               }
//           }
//       }).start();

//    }
