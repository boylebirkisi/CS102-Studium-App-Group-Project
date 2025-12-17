package cs102groupproject.server;

import java.sql.*;

public class DBManager {
     // 1. Connection Details, database URL, username and password
    private static final String DB_URL = "jdbc:postgresql://34.59.31.201:5432/studium";
    private static final String USER = "postgres";
    private static final String PASS = "P3c,YKR[a~}THbM9";
    
    /**
     * Establishes a database connection.
     * @return A valid Connection object, or null on failure.
     */
    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to the database!"); // Uncomment for testing
        } catch (SQLException e) {
            System.err.println("Connection failed! Check credentials and network. Error: " + e.getMessage());
            // In a real application, you would log this error.
        }
        return conn;
    }

    /**
     * Inserts a new user into the users table.
     * @param name The user's name.
     * @param email The user's email.
     * @param password The user's hashed password.
     * @return true if the insert was successful, false otherwise.
     */
    public boolean insertUser(String name, String email, String password) {
        // Use PreparedStatement for security (prevents SQL Injection)
        String sqlQuery = "INSERT INTO users(name, email, password) VALUES(?, ?, ?)"; 

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            if (conn == null) return false; // Connection failed
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            int rowsAffected = pstmt.executeUpdate();
            // If at least one row was affected, that means the insertion was successful.
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        DBManager db = new DBManager();
        String hashedPassword = new PasswordHasher().hashPassword("password123");
        boolean success = db.insertUser("delfin", "deryilmaz06@gmail.com", hashedPassword);
        if (success) System.out.println("Insert successful.");
    }
}
