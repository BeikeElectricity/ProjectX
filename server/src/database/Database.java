package database;

/**
 * A simple class that calls the database.
 *
 * Created by alex on 9/18/15.
 */

import java.sql.*;
import java.util.ArrayList;

public class Database {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/ProjectX";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    //Replace with connection pool if needed.
    private Connection conn;
    private Statement stmt;

    public Database() {
        try {
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //Create a statement
            stmt = conn.createStatement();

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

    }

    /**
     * @return A string with a to tep scoreboard.
     */
    public String getGolbalTopTen() {
        try {
            ResultSet rs = stmt.executeQuery("CALL getGlobalTopTen;");
            StringBuilder sb = new StringBuilder();
            for (int i = 1; rs.next(); i++) {
                sb.append(i + ". ");
                sb.append(rs.getString("nickname"));
                sb.append(" ");
                sb.append(rs.getInt("score"));
                sb.append("\n");
            }
            //Close the statement. TODO: Check if really needed.
            rs.close();
            stmt.close();
            return sb.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            //Well this is not best practices...
            return "";
        }
    }

    public void closeConnection() {
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}


