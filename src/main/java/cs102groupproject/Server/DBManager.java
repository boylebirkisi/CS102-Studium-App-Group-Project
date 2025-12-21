package cs102groupproject.Server;
import cs102groupproject.SharedObjects.*;
import java.sql.*;

public class DBManager {
     // Connection Details
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
            System.out.println("Connected to the database!"); 
        } catch (SQLException e) {
            System.err.println("Connection failed! Check credentials and network. Error: " + e.getMessage());
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

    public User getUserByEmail(String email) {
        String sqlQuery = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            if (conn == null) return null; // Connection failed
            
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("id"), rs.getString("name"), rs.getString("email"),
                    rs.getString("google_id"), rs.getInt("soloCurrency"), rs.getInt("groupCurrency"),
                    rs.getBoolean("isVerified"), rs.getString("avatar"));
            } else {
                return null; // No user found
            }
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return null;
        }
    }

    public User getUserByGoogleID(String googleId) {
        String sqlQuery = "SELECT * FROM users WHERE google_id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            if (conn == null) return null; // Connection failed
            
            pstmt.setString(1, googleId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Assuming User is a class with a constructor that takes id, name, email, password
                return new User(rs.getString("id"), rs.getString("name"), rs.getString("email"),
                    rs.getString("google_id"), rs.getInt("soloCurrency"), rs.getInt("groupCurrency"),
                    rs.getBoolean("isVerified"), rs.getString("avatar"));
            } else {
                return null; // No user found
            }
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get user by ID.
     * @param id
     * @return
     */
    public User getUserByID(int id) {
        String sqlCommand = "SELECT * FROM users WHERE id = ?";

        ResultSet rs = getObject(sqlCommand);
        if (rs != null) {
            try {
                return new User(rs.getString("id"), rs.getString("name"), rs.getString("email"),
                    rs.getString("google_id"), rs.getInt("soloCurrency"), rs.getInt("groupCurrency"),
                    rs.getBoolean("isVerified"), rs.getString("avatar"));
            } catch (SQLException e) {
                System.err.println("Database operation failed: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * Create a friendship between two users.
     * @param userID
     * @param friendID
     * @return
     */
    public boolean addFriend(int userID, int friendID) {        
        String sqlCommand = "INSERT INTO friends(user_id, friend_id) VALUES(?, ?)";

        return executeOneSqlCommand(sqlCommand, userID, friendID);
    }   

    public boolean removeFriend(int userID, int friendID) {
        String sqlCommand = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

        return executeOneSqlCommand(sqlCommand, userID, friendID);
    }

    // Helper method to get specific object
    private ResultSet getObject(String sqlCommand) {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            if (conn == null) return null; 

            // Executes the query
            ResultSet rs = stmt.executeQuery(sqlCommand);
            if (rs.next()) {
                return rs;
            } else {
                return null; 
            }

        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return null;
        }
    }

    private boolean executeTransaction(String[] sqlCommands, Object[][] parameters) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = connect();
            if (connection == null) return false;

            // Starts transaction
            connection.setAutoCommit(false);

            for (int i = 0; i < sqlCommands.length; i++) {
                String sqlCommand = sqlCommands[i];
                Object[] params = parameters[i];

                pstmt = connection.prepareStatement(sqlCommand);

                // Passing the parameters
                for (int j = 0; j < params.length; j++) {
                    pstmt.setObject(j + 1, params[j]);
                }

                pstmt.executeUpdate();
            }

            // Commits the transaction to database
            connection.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Transaction failed: " + e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                    System.out.println("Transaction rolled back.");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Failed to rollback transaction: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.setAutoCommit(true); 
                if (connection != null) connection.close();

            } catch (SQLException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
        }
    }

    /**
     * Executes a single SQL command.
     * @param sqlCommand
     * @param paramaters
     * @return true if operation is successful.
     */
    private boolean executeOneSqlCommand(String sqlCommand, Object... paramaters) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = connect();
            if (connection == null) return false;

            pstmt = connection.prepareStatement(sqlCommand);

            return executeCoreLogic(connection, sqlCommand, paramaters);
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.close();

            } catch (SQLException e) {
                System.err.println("Failed to close PreparedStatement: " + e.getMessage());
            }
        }
    }

    /**
     * Core logic for executing SQL commands for both single operation and transactions.
     * @param connection
     * @param sqlCommand
     * @param parameters
     * @return true if operation is successful
     * @throws SQLException
     */
    private boolean executeCoreLogic(Connection connection, String sqlCommand, Object... parameters) throws SQLException {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlCommand);

            // Passing the parameters
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            throw e; 
        }
    }

    public static void main(String[] args) {
        DBManager db = new DBManager();
        String hashedPassword = new PasswordHasher().hashPassword("password123");
        boolean success = db.insertUser("delffafin", "deryilmaz06@gmail.com", hashedPassword);
        if (success) System.out.println("Insert successful.");
    }
}
