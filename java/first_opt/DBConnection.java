package myz.myDBLoading;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
    private Connection connection = null;

    public DBConnection(String propPlace) {
        openDB(loadProperties(propPlace),true);
    }
    public DBConnection(String propPlace, boolean noAutoCommit) {
        openDB(loadProperties(propPlace),noAutoCommit);
    }

    private void openDB(Properties prop,boolean noAutoCommit){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }

        String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");

        try {
            connection = DriverManager.getConnection(url, prop);
            if (connection != null) {
                System.out.println("Successfully connected to the database "
                        + prop.getProperty("database") + " as " + prop.getProperty("user"));
                if (noAutoCommit) {
                    connection.setAutoCommit(false);
                    //禁用自动提交事务 直到使用connection.commit()再提交事务
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    private static Properties loadProperties(String propPlace){
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(Files.newInputStream(Paths.get(propPlace))));
        }catch (IOException e){
            System.err.println("can not find db user file");
            throw new RuntimeException(e);
        }
        return prop;
    }



    public void closeDB() {
        if (connection != null) {
            try {
                connection.commit();
                connection.close();
                connection = null;
                System.out.println("DataBase closed");
            } catch (Exception e) {
            }
        }
    }


    public Connection getConnection() {
        return connection;
    }

}
