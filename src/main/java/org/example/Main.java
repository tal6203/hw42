package org.example;

import java.sql.*;
import java.util.Random;

public class Main {
    private static volatile int i = 1;
    final static Object pool_key = new Object();

    public static void print_order() {
        synchronized (pool_key) {
            while (i < 11) {
                if (Thread.currentThread().getName().equals(String.valueOf(i))) {
                    System.out.println(Thread.currentThread().getName());
                    i++;
                    pool_key.notifyAll();
                } else {
                    try {
                        pool_key.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static void print_ten10() {
        synchronized (pool_key) {
            if (!Thread.currentThread().getName().equals("10") && i < 10) {
                System.out.println(Thread.currentThread().getName());
                i++;
                pool_key.notifyAll();
            } else {
                try {
                    pool_key.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        Thread t1 = new Thread(Main::print_order,"1");
//        Thread t2 = new Thread(Main::print_order,"2");
//        Thread t3 = new Thread(Main::print_order,"3");
//        Thread t4 = new Thread(Main::print_order,"4");
//        Thread t5 = new Thread(Main::print_order,"5");
//        Thread t6 = new Thread(Main::print_order,"6");
//        Thread t7 = new Thread(Main::print_order,"7");
//        Thread t8 = new Thread(Main::print_order,"8");
//        Thread t9 = new Thread(Main::print_order,"9");
//        Thread t10 = new Thread(Main::print_order,"10");

        Thread t1 = new Thread(Main::print_ten10, "1");
        Thread t2 = new Thread(Main::print_ten10, "2");
        Thread t3 = new Thread(Main::print_ten10, "3");
        Thread t4 = new Thread(Main::print_ten10, "4");
        Thread t5 = new Thread(Main::print_ten10, "5");
        Thread t6 = new Thread(Main::print_ten10, "6");
        Thread t7 = new Thread(Main::print_ten10, "7");
        Thread t8 = new Thread(Main::print_ten10, "8");
        Thread t9 = new Thread(Main::print_ten10, "9");
        Thread t10 = new Thread(Main::print_ten10, "10");


        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
        t10.start();

        Class.forName("org.sqlite.JDBC");

        Connection connection = null;

        connection = DriverManager.getConnection("jdbc:sqlite:C:\\project_java\\hw42\\hw42\\db.db");


        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        statement.executeUpdate("drop table if exists person");
        statement.executeUpdate("create table person (id integer, name string)");
        statement.executeUpdate("insert into person values(1, 'leo')");
        statement.executeUpdate("insert into person values(2, 'yui')");
        ResultSet rs = statement.executeQuery("select * from person");
        while (rs.next()) {
            System.out.format("name = %s \n", rs.getString("name"));
            System.out.format("id = %d \n", rs.getInt("id"));
        }
        if (connection != null) {
            connection.close();
        }



    }




}

