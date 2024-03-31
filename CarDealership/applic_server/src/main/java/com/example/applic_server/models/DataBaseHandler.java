package com.example.applic_server.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DataBaseHandler {
    private static final Logger file_logger = LoggerFactory.getLogger("file_data");
    PropertiesReader props = new PropertiesReader();
    String url = props.getMySqlUrl();
    String username = props.getMySqlUsername();
    String password = props.getMySqlPass();


    public DataBaseHandler() {
    }


    public void update_db(List<ApplicationData> applics) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            int truncCount = statement.executeUpdate("truncate applications;");
            for (ApplicationData applic : applics) {
                String insert_str = "";
                insert_str = String.format("INSERT INTO applications values('%s','%s','%s','%s','%s');", applic.get_status(), applic.get_name(), applic.get_type(), applic.get_date(), applic.get_text());
                int insertCount = statement.executeUpdate(insert_str);
            }
            file_logger.info("Applications were inserted into database");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}


