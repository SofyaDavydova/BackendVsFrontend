package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private SQLHelper() {

    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
    }


    @SneakyThrows
    public static String getVerificationCodeFromDataBase() {
        var codeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        try (var conn = getConnection()) {
            return runner.query(conn, codeSQL, new ScalarHandler<>());

        }
    }

    @SneakyThrows
    public static void cleanDatabase(){
        try (var conn = getConnection()) {
            runner.execute(conn, "DELETE FROM auth_codes");
            runner.execute(conn, "DELETE FROM card_transactions");
            runner.execute(conn, "DELETE FROM cards");
            runner.execute(conn, "DELETE FROM users");
        }
    }

    @SneakyThrows
    public static void cleanAuthCode(){
        try (var conn = getConnection()) {
            runner.execute(conn, "DELETE FROM auth_codes");
        }

    }
}

