package com.example.applic_server.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DataBaseHandler {
    private static final Logger loggerDB = LogManager.getLogger("FilesLogger");
    //    PropertiesReader props = new PropertiesReader();
//    String url = props.getMySqlUrl();
//    String username = props.getMySqlUsername();
//    String password = props.getMySqlPass();
    static String url = "jdbc:mysql://localhost:3307/";
    static String name;
    static String login;
    static String password;

    public static void setValuesForConnection(String databaseName, String databaseLogin, String databasePassword) {
        url += databaseName;
        name = databaseName;
        login = databaseLogin;
        password = databasePassword;

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
//            System.out.println(connection);
        } catch (SQLException e) {
            System.out.println("SQLException on connection.");
            loggerDB.error("Error on getting connection");
//            e.printStackTrace();
        }
    }


    public DataBaseHandler() {
    }


    public void update_db(List<ApplicationData> applics) {
        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, login, password);
//            System.out.println(connection);
            Statement statement = connection.createStatement();
            int truncCount = statement.executeUpdate("truncate applications;");
            for (ApplicationData applic : applics) {
                String insert_str = "";
                insert_str = String.format("INSERT INTO applications values('%s','%s','%s','%s','%s');", applic.get_status(), applic.get_name(), applic.get_type(), applic.get_date(), applic.get_text());
                int insertCount = statement.executeUpdate(insert_str);
            }
            loggerDB.info("Applications were inserted into database");
        } catch (SQLException e) {
            System.out.println("Error on updating DB");
//            e.printStackTrace();
        }
//        catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }
}


