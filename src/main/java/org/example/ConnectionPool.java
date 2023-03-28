package org.example;

import java.sql.*;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.Queue;

public class ConnectionPool implements IConnectionPool {
    final static int max = 4;
    final static Object key = new Object();
    final static Object pool_key = new Object();
    private static ConnectionPool INSTANCE = null;

    Queue<Connection> connections = new LinkedList<Connection>();

    private ConnectionPool() throws SQLException {
        for (int i = 0; i < max; i++) {
            connections.add(DriverManager.getConnection("jdbc:sqlite:C:\\project_java\\hw42\\hw42\\db.db"));
        }
    }

    @Override
    public Connection getConnection() throws InterruptedException {
        synchronized (pool_key) {
            while (connections.size() == 0) {
                pool_key.wait();
                System.out.println("thread is wait");
                // return here after notify
            }
            System.out.println("db is connected");
            Connection conn = (Connection) connections.remove();
            return conn;
        }
    }

    @Override
    public void returnConnection(Connection conn) {
        synchronized (pool_key) {
            connections.add(conn);
            System.out.println("thread make notify");
            pool_key.notifyAll();
        }
    }

    @Override
    public void close() {
        synchronized (pool_key) {
            for (Connection conn : connections) {
                try {
                    conn.close();
                    System.out.println("db closed");
                }
                catch (Exception ex) {

                }
            }
            while (connections.size() > 0) {
                try {
                    connections.remove();
                }
                catch (Exception ex) {

                }
            }
        }
    }

    public static ConnectionPool getInstance() throws SQLException {
        if (INSTANCE == null ){
            synchronized (key) {
                if (INSTANCE == null) {
                    INSTANCE = new ConnectionPool();
                }
            }
        }
        return INSTANCE;
    }
}
