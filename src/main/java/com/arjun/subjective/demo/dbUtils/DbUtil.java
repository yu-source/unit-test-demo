package com.arjun.subjective.demo.dbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtil {

    private static String driverClass = "dm.jdbc.driver.DmDriver";
    private static String url = "jdbc:dm://172.16.1.16:5236/QH_POT_ADMIN_TEST";
    private static String username = "QH_POT_ADMIN_TEST";
    private static String password = "tellhow2020";

    /**
     * 创建数据库连接
     *
     * @return
     */
    public static Connection createConnection() {
        Connection connection = null;
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 执行插入操作
     *
     * @param connection
     * @param insertSql
     * @throws SQLException
     */
    public static void executeInsert(Connection connection, String insertSql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(insertSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}