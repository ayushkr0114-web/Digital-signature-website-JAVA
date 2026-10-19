package com.digitalsignature.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521:xe";

    private static final String USER = "system";

    private DBConnection() {
    }

    public static Connection getConnection() throws Exception {

        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalStateException(
                    "DB_PASSWORD environment variable is not set."
            );
        }

        Class.forName("oracle.jdbc.OracleDriver");

        return DriverManager.getConnection(
                URL,
                USER,
                password
        );
    }
}