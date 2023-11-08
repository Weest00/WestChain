package br.com.west.chain.database;

import br.com.west.chain.Main;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {

    private Connection connection;
    private boolean active;


    public void openConnection() {
        File file = new File(Main.getInstance().getDataFolder(), "database.db");
        String url = "jdbc:sqlite:" + file;
        try {
            active = true;
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public Connection getConnection() {
        if (connection == null || !active)
            openConnection();
            return connection;
        }

        public void disconnect () {
            if (connection != null || active) {
                try {
                    active = false;
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }
        }
    }
