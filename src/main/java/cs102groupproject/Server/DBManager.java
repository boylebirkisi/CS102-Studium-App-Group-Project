package cs102groupproject.Server;
import cs102groupproject.App;
import cs102groupproject.SharedObjects.*;
import javafx.scene.Group;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
     // Connection Details
    private static final String DB_URL = "jdbc:postgresql://34.59.31.201:5432/studium";
    private static final String USER = "postgres";
    private static final String PASS = "P3c,YKR[a~}THbM9";
    
    /**
     * Establishes a database connection.
     * @return A Connection object, or null on failure.
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
    public int insertUser(String name, String email, String password, String googleId, String department) {
        // Use PreparedStatement for security (prevents SQL Injection)
        String sqlQuery = "INSERT INTO users(username, email, password_hash, google_id, department) VALUES(?, ?, ?, ?, ?)"; 

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            if (conn == null) return -1; // Connection failed
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, googleId);
            pstmt.setString(5, department);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Returns the new INT id
                }
            }
        }
        return -1;
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Gets user by its username.
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {
        String sqlQuery = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            if (conn == null) return null; // Connection failed
            
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("department"),
                    rs.getString("email"), rs.getString("google_id"), rs.getInt("solo_currency"),
                    rs.getInt("group_currency"), rs.getBoolean("is_verified"), rs.getString("avatar"));
            } else {
                return null; // No user found
            }
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets user by its email.
     * @param email
     * @return
     */
    public User getUserByEmail(String email) {
        String sqlQuery = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            if (conn == null) return null; // Connection failed
            
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("department"),
                    rs.getString("email"), rs.getString("google_id"), rs.getInt("solo_currency"),
                    rs.getInt("group_currency"), rs.getBoolean("is_verified"), rs.getString("avatar"));
            } else {
                return null; // No user found
            }
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets user by its googleID.
     * @param googleId
     * @return
     */
    public User getUserByGoogleID(String googleId) {
        String sqlQuery = "SELECT * FROM users WHERE google_id = ?";

        try (Connection conn = connect(); 
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            
            pstmt.setString(1, googleId); // This replaces the '?' with the actual ID
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"), 
                        rs.getString("username"), 
                        rs.getString("department"),
                        rs.getString("email"), 
                        rs.getString("google_id"), 
                        rs.getInt("solo_currency"),
                        rs.getInt("group_currency"), 
                        rs.getBoolean("is_verified"), 
                        rs.getString("avatar")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage()); 
        }
        return null; // Return null if no user exists
    }

    /**
     * Get user by ID.
     * @param id
     * @return
     */
    public User getUserByID(int id) {
        String sqlCommand = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = connect(); 
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand)) {
            
            pstmt.setInt(1, id); // This replaces the '?' with the actual ID
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"), 
                        rs.getString("username"), 
                        rs.getString("department"),
                        rs.getString("email"), 
                        rs.getString("google_id"), 
                        rs.getInt("solo_currency"),
                        rs.getInt("group_currency"), 
                        rs.getBoolean("is_verified"), 
                        rs.getString("avatar")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage()); 
        }
        return null; // Return null if no user exists
    }

    /**
     * Get password of the user.
     * @param username
     * @return
     */
    public String getPasswordByUsername(String username) {
        String sqlQuery = "SELECT password_hash FROM users WHERE username = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            if (conn == null) return null; // Connection failed
            
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password_hash");
            } else {
                return null; // No user found
            }
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Update user avatar path.
     * @param userID
     * @param avatarPath
     * @return
     */
    public boolean updateUserAvatar(int userID, String avatarPath) {
        String sqlCommand = "UPDATE users SET avatar = ? WHERE id = ?";

        return executeSqlCommand(sqlCommand, avatarPath, userID);
    }

    /**
     * Updates user's currency.
     * @param userID
     * @param currency
     * @param isGroup
     * @return
     */
    public boolean updateUserCurrency(int userID, int currency, boolean isGroup) {
        String sqlCommand = "";
        if (isGroup) {
            sqlCommand = "UPDATE users SET group_currency = ? WHERE id = ?";
        } else {
            sqlCommand = "UPDATE users SET solo_currency = ? WHERE id = ?";
        }
        return executeSqlCommand(sqlCommand, currency, userID);
    }

    /**
     * Create a friendship between two users.
     * @param userID
     * @param friendID
     * @return
     */
    public boolean addFriend(int userID, int friendID) {        
        String sqlCommand = "INSERT INTO friendships(user_id, friend_id) VALUES(?, ?)";

        return executeSqlCommand(sqlCommand, Math.min(userID, friendID), Math.max(userID, friendID));
    }   

    /**
     * Remove a friendship between two users.
     * @param userID
     * @param friendID
     * @return
     */
    public boolean removeFriend(int userID, int friendID) {
        String sqlCommand = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";

        return executeSqlCommand(sqlCommand, Math.min(userID, friendID), Math.max(userID, friendID));
    }

    /**
     * Get friends of a user. (Working)
     * @param currentUserID
     * @return
     */
    public List<User> getFriends(int currentUserID) {
        List<User> friends = new ArrayList<>();
        String sql = "SELECT u.* FROM users u " +
                    "JOIN friendships f ON (u.id::integer = f.friend_id OR u.id::integer = f.user_id) " +
                    "WHERE (f.user_id = ? OR f.friend_id = ?) " +
                    "AND u.id::integer != ?";
        
        try (Connection conn = connect(); 
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, currentUserID);
            pstmt.setInt(2, currentUserID);
            pstmt.setInt(3, currentUserID);
            
            // Fix: Removed 'sql' from executeQuery()
            ResultSet rs = pstmt.executeQuery(); 
            
            while (rs.next()) {
                friends.add(new User(
                    rs.getInt("id"), rs.getString("username"), rs.getString("department"),
                    rs.getString("email"), rs.getString("google_id"), rs.getInt("solo_currency"),
                    rs.getInt("group_currency"), rs.getBoolean("is_verified"), rs.getString("avatar")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch bidirectional friends: " + e.getMessage());
        }
        return friends;
    }

    public List<User> getAllUsers() {
        String sqlCommand = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            if (conn == null) return users; 
            
            ResultSet rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("department"),
                    rs.getString("email"), rs.getString("google_id"), rs.getInt("solo_currency"),
                    rs.getInt("group_currency"), rs.getBoolean("is_verified"), rs.getString("avatar"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        }
        return users;
    }

    //! Habit, Task, Event Operations

    /**
     * Adds a habit for a user.
     * @param name
     * @param userID
     * @return
     */
    public Habit addHabit(String name, int userID) {
        String sqlCommand = "INSERT INTO habits(name, user_id) VALUES(?, ?)";
        int id = insertAndGetID(sqlCommand, name, userID);
        return getHabitByID(id);
    }

    /**
     * Removes a habit by its ID.
     * @param habitID
     * @return
     */
    public boolean removeHabit(int habitID) {
        String sqlCommand = "DELETE FROM habits WHERE id = ?";

        return executeSqlCommand(sqlCommand, habitID);
    }

    /**
     * Updates the habit completion string and compeletence status.
     * @param habitID
     * @param completed_arr_data
     * @param isCompleted
     * @return
     */
    public boolean updateHabitCompletionString(int habitID, String completed_arr_data, boolean isCompleted) {
        String sqlCommand = "UPDATE habits SET completed_arr_data = ?, is_completed = ? WHERE id = ?";

        return executeSqlCommand(sqlCommand, completed_arr_data, isCompleted, habitID);
    }

    /**
     * Get habit by ID.
     * @param habitID
     * @return
     */
    public Habit getHabitByID(int habitID) {
        String sqlCommand = "SELECT * FROM habits WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlCommand)) {

            if (conn == null) return null; // Connection failed
            
            pstmt.setInt(1, habitID);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Habit(rs.getString("name"), rs.getInt("id"), rs.getInt("user_id"), rs.getString("completed_arr_data"));
            } else {
                return null; // No user found
            }
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get all habits of the user.
     * @return
     */
    public List<Habit> getAllHabits(int userID) {
        String sqlCommand = "SELECT * FROM habits WHERE user_id = ?";
        List<Habit> habits = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlCommand)) {

            pstmt.setInt(1, userID);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Habit habit = new Habit(rs.getString("name"), rs.getInt("id"), rs.getInt("user_id"), rs.getString("completed_arr_data"));
                habits.add(habit);
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        }
        return habits;
    }

    /**
     * Inserts a task for a user.
     * @param name
     * @param color
     * @param importance
     * @param userId
     * @param googleCalendarID
     * @return
     */
    public int addTask(String name, String color, int importance, int userId, String googleCalendarID, LocalDate dueDate) {
        String sqlCommand = "INSERT INTO tasks(name, color, importance, user_id, google_calendar_id, due_date) VALUES(?, ?, ?, ?, ?, *)";

        return insertAndGetID(sqlCommand, name, color, importance, userId, googleCalendarID, dueDate);
    }

    /**
     * Deletes a task by its ID.
     * @param taskID
     * @return
     */
    public boolean deleteTask(int taskID) {
        String sqlCommand = "DELETE FROM tasks WHERE id = ?";

        return executeSqlCommand(sqlCommand, taskID);
    }

    /**
     * Updates the completion status of a task.
     * @param taskID
     * @param isCompleted
     * @return
     */
    public boolean updateTaskStatus(int taskID, boolean isCompleted) {
        String sqlCommand = "UPDATE tasks SET is_completed = ? WHERE id = ?";

        return executeSqlCommand(sqlCommand, isCompleted, taskID);
    }

    /**
     * Get task by ID.
     * @param taskID
     * @return
     */
    public Task getTaskByID(int taskID) {
        String sqlCommand = "SELECT * FROM tasks WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlCommand)) {

            if (conn == null) return null; 
            
            pstmt.setInt(1, taskID);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Task(rs.getString("name"), rs.getString("color"), rs.getInt("importance"),
                    rs.getInt("user_id"), rs.getInt("id"), rs.getDate("due_date").toLocalDate() ,rs.getString("google_calendar_id"), rs.getBoolean("is_completed"));
            } else {
                return null; 
            }
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get all tasks. (Working)
     * @return
     */
    public List<Task> getAllTasks(int userId) {
        String sqlCommand = "SELECT * FROM tasks WHERE user_id = ?";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sqlCommand)) {
            stmt.setInt(1, userId);
            if (conn == null) return tasks; 
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Task task = new Task(rs.getString("name"), rs.getString("color"), rs.getInt("importance"),
                    rs.getInt("user_id"), rs.getInt("id"), rs.getDate("due_date").toLocalDate(), rs.getString("google_calendar_id"), rs.getBoolean("is_completed"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Inserts an event for a user.
     * @param name
     * @param color
     * @param start
     * @param finish
     * @param userId
     * @param importance
     * @param googleCalendarID
     * @return the eventID.
     */
    public int addEvent(String name, String color, LocalDateTime start, LocalDateTime finish, int userId, int importance, String googleCalendarID) {
        String sqlCommand = "INSERT INTO app_events(name, color, start_time, finish_time, user_id, importance, google_calendar_id) VALUES(?, ?, ?, ?, ?, ?, ?)";

        return insertAndGetID(sqlCommand, name, color, start, finish, userId, importance, googleCalendarID);
    }

    /**
     * Deletes an event by its ID.
     * @param eventID
     * @return
     */
    public boolean deleteEvent(int eventID) {
        String sqlCommand = "DELETE FROM app_events WHERE id = ?";

        return executeSqlCommand(sqlCommand, eventID);
    }

    /**
     * Updates dates of an event.
     * @param eventID
     * @param newStart
     * @param newFinish
     * @return
     */
    public boolean updateEventDates(int eventID, LocalDateTime newStart, LocalDateTime newFinish) {
        String sqlCommand = "UPDATE app_events SET start_time = ?, finish_time = ? WHERE id = ?";

        return executeSqlCommand(sqlCommand, newStart, newFinish, eventID);
    }

    /**
     * Get event by ID.
     * @param eventID
     * @return
     */
    public AppEvent getEventByID(int eventID) {
        String sqlCommand = "SELECT * FROM app_events WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlCommand)) {

            if (conn == null) return null; 
            
            pstmt.setInt(1, eventID);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new AppEvent(
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getTimestamp("start_time").toLocalDateTime(),  
                    rs.getTimestamp("finish_time").toLocalDateTime(), 
                    rs.getInt("user_id"),
                    rs.getInt("importance"),
                    rs.getInt("id"),
                    rs.getString("google_calendar_id")
                );
            } else {
                return null; 
            }
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get all events.
     * @return
     */
    public List<AppEvent> getAllEvents(int userId) {
        String sqlCommand = "SELECT * FROM app_events WHERE user_id = ?";
        List<AppEvent> events = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sqlCommand)) {

            stmt.setInt(1, userId);
            if (conn == null) return events; 
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AppEvent event = new AppEvent(
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getTimestamp("start_time").toLocalDateTime(),  
                    rs.getTimestamp("finish_time").toLocalDateTime(), 
                    rs.getInt("user_id"),
                    rs.getInt("importance"),
                    rs.getInt("id"),
                    rs.getString("google_calendar_id")
                );
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        }
        return events;
    }

    //! Notification and Session Operations

    public int addNotification(String title, String message, int userId) {
        String sqlCommand = "INSERT INTO notifications(title, message, user_id) VALUES(?, ?, ?)";

        return insertAndGetID(sqlCommand, title, message, userId);
    }
    
    public List<Notification> getAllNotifications() {
        String sqlCommand = "SELECT * FROM notifications";
        List<Notification> notifications = new ArrayList<>();

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            if (conn == null) return notifications; 
            
            ResultSet rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                Notification notification = new Notification(
                    rs.getInt("user_id"),
                    rs.getInt("reference_id"),
                    rs.getString("message"),
                    rs.getString("title"),
                    rs.getString("png"),
                    Notification.Type.valueOf(rs.getString("type"))
                ); 
                notifications.add(notification);
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        }
        return notifications;
    }

    /**
     * Add session to the db.
     * @param session
     * @return
     */
    public int addSession(Session session) {
        String sqlCommand = "INSERT INTO sessions(owner_id, name, type, no, length, break_length, start_date) VALUES(?, ?, ?, ?, ?, ?, ?)";

        return insertAndGetID(sqlCommand, session.getOwner().getId(), session.getName(), session.getType(),
            session.getNo(), session.getLength(), session.getBreakLength(), session.getStartDate());
    }

    /**
     * Get all indiviudal session of the user.
     * @param userId
     * @return
     */
    public List<Session> getAllIndividualSessions(int userId) {
        String sqlCommand = "SELECT * FROM sessions WHERE owner_id = ?";
        List<Session> sessions = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sqlCommand)) {

            stmt.setInt(1, userId);
            if (conn == null) return sessions; 
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Session session = new Session(
                    getUserByID(rs.getInt("owner_id")),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getInt("no"),
                    rs.getInt("length"),
                    rs.getInt("break_length"),
                    rs.getTimestamp("start_date").toLocalDateTime()
                ); 
                sessions.add(session);
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        }
        return sessions;
    }

    /**
     * Get individual group session of a user.
     * @param sessionID
     * @return
     */
    public Session getIndividualSessionByID(int sessionID) {
        String sqlCommand = "SELECT * FROM sessions WHERE id = ?";

        ResultSet rs = getObject(sqlCommand);
        if (rs != null) {
            try {
                return new Session(
                    getUserByID(rs.getInt("owner_id")),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getInt("no"),
                    rs.getInt("length"),
                    rs.getInt("break_length"),
                    rs.getTimestamp("start_date").toLocalDateTime()
                );
            } catch (SQLException e) {
                System.err.println("Database operation failed: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Get all group sessions.
     * @return
     */
    public List<GroupSession> getAllGroupSessions() {
        String sqlCommand = "SELECT * FROM group_sessions";
        List<GroupSession> sessions = new ArrayList<>();

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            if (conn == null) return sessions; 
            
            ResultSet rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                Session session = getIndividualSessionByID(rs.getInt("session_id"));

                ResultSet participantRS = getObject("SELECT * FROM session_participants WHERE session_id = " + rs.getInt("session_id"));
                ArrayList<User> participants = new ArrayList<>();
                while (participantRS.next()) {
                    participants.add(getUserByID(participantRS.getInt("user_id")));
                }
                GroupSession groupSession = new GroupSession(session, participants, rs.getBoolean("is_public"));
                sessions.add(groupSession);
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        }
        return sessions;
    }

    /**
     * Insert verfication code, it it exists replace it.
     * @param email
     * @param code
     * @param expiryTime
     * @return
     */
    public boolean insertVerificationCode(String email, String code, long expiryTime) {
        String sqlCommand = "INSERT INTO verification_codes (email, stored_code, expiry_time) " +
         "VALUES (?, ?, ?) ON CONFLICT (email) DO UPDATE SET " +
         "stored_code = EXCLUDED.stored_code, expiry_time = EXCLUDED.expiry_time";

        return executeSqlCommand(sqlCommand, email, code, expiryTime);
    }

    /**
     * Get verification code.
     * @param email
     * @return
     */
    public VerificationCode getVerificationCode (String email) {
        String sqlCommand = "SELECT * FROM verification_codes WHERE email = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlCommand)) {

            if (conn == null) {
                System.err.println("CRITICAL: Connection is null in getVerificationCode!");
                return null;
            }
            
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                VerificationCode code = new VerificationCode(rs.getString("stored_code"), rs.getString("email"), rs.getLong("expiry_time"));
                System.out.println(code.getStoredCode());
                return code;
            } else {
                return null; 
            }
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Inserts a new ChatMesssage.
     * @param senderID
     * @param receiverID
     * @param text
     * @param timestamp
     * @return
     */
    public int insertChatMessage(int senderID, int receiverID, String text, LocalDateTime timestamp) {
        String sqlCommand = "INSERT INTO chat_messages (sender_id, receiver_id, message_text, timestamp)" +
                            "VALUES (?, ?, ?, ?)";

        return insertAndGetID(sqlCommand, senderID, receiverID, text, timestamp);        
    }

    public List<ChatMessage> getChatMessages(int userID) {
        String sqlCommand = "SELECT * FROM chat_messages WHERE sender_id = ? OR receiver_id = ? ORDER BY timestamp ASC";
        List<ChatMessage> messages = new ArrayList<>();

        try (Connection conn = connect(); 
         PreparedStatement pstmt = conn.prepareStatement(sqlCommand)) {

        if (conn == null) return messages; 
        
        pstmt.setInt(1, userID);
        pstmt.setInt(2, userID);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // Pull the actual data from the database columns
                int senderId = rs.getInt("sender_id");
                int receiverId = rs.getInt("receiver_id");
                String content = rs.getString("message_text"); // Adjust column name to match your DB
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

                // Create the object using the database values
                messages.add(new ChatMessage(senderId, receiverId, content, timestamp));
            }
        }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        }
        return messages;
    }

    /**
     * Get object by SQL command.
     * @param sqlCommand
     * @return
     */
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

    /**
     * Inserts a new object and returns the generated ID.
     * @param sqlCommand
     * @param parameters
     * @return
     */
    private int insertAndGetID(String sqlCommand, Object... parameters) {
        try (Connection connection = connect()) {
            if (connection == null) return -1;

            try (PreparedStatement pstmt = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < parameters.length; i++) {
                    pstmt.setObject(i + 1, parameters[i]);
                }

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1); 
                        }
                    }
                }
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return -1;
        }
    }

    private boolean executeTransaction(String[] sqlCommands, Object[][] parameters) {
        try (Connection connection = connect()) {
            if (connection == null) return false;

            connection.setAutoCommit(false);
            try {
                for (int i = 0; i < sqlCommands.length; i++) {
                    executeCoreLogic(connection, sqlCommands[i], parameters[i]);
                }
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Transaction failed, rolled back: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Executes a single SQL command for updating, deleting.
     * @param sqlCommand
     * @param paramaters
     * @return true if operation is successful.
     */
    private boolean executeSqlCommand(String sqlCommand, Object... paramaters) {
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
        try (PreparedStatement pstmt = connection.prepareStatement(sqlCommand)) {
        for (int i = 0; i < parameters.length; i++) {
            pstmt.setObject(i + 1, parameters[i]);
        }
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
        }
    }

    public static void main(String[] args) {
        DBManager dbManager = new DBManager();
        System.out.println(dbManager.getVerificationCode("deryilmaz06@gmail.com").getStoredCode());
    }
}