package org.example.utils;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final int DEFAULT_POOL_SIZE = 10;
    private static final String POOL_SIZE = "db.pool.size";
    private static BlockingQueue<Connection> pool;
    static {
        loadDriver();
        initConnectionPool();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initConnectionPool() {
        String poolSize = PropertyUtils.get(POOL_SIZE);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            var proxyConnection = (Connection)Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close") ?
                    pool.add((Connection) proxy) :
                    method.invoke(connection, args));
            pool.add(proxyConnection);
        }
    }

    private static Connection open(){
        try{
            return DriverManager.getConnection(
                    PropertyUtils.get(URL_KEY),
                    PropertyUtils.get(USERNAME_KEY),
                    PropertyUtils.get(PASSWORD_KEY));
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static Connection get(){
        try{
            return pool.take();
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private ConnectionManager() {
    }
}
