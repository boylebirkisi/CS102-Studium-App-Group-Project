package cs102groupproject.SharedObjects;

import java.util.List;
import java.util.ArrayList;
/**
 * Class for the login response and initial values.
 */
public class LoginResponse {
    private User user;
    private List<User> friends;
    private List<User> allUsers;
    private List<Notification> notifications;
    private List<ChatMessage> messages;
    private List<AppEvent> eventsOfUser;
    private List<Task> tasksOfUser;
    private List<Habit> habits;
    private List<Session> individualSessions;
    private List<GroupSession> allGroupSessions;

    // 
    public LoginResponse() {
        this.friends = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.eventsOfUser = new ArrayList<>();
        this.habits = new ArrayList<>();
        this.individualSessions = new ArrayList<>();
        this.allGroupSessions = new ArrayList<>();
    }

    /**
     * Full constructor.
     * @param user
     * @param friends
     * @param notifications
     * @param messages
     * @param eventsOfUser
     * @param habits
     * @param individualSessions
     */
    public LoginResponse(User user, List<User> friends, List<User> allUsers, List<Notification> notifications, 
                         List<ChatMessage> messages, List<AppEvent> eventsOfUser, List<Task> tasksOfUser,
                         List<Habit> habits, List<Session> individualSessions, List<GroupSession> allGroupSessions) {
        this.user = user;
        this.friends = friends;
        this.allUsers = allUsers;
        this.notifications = notifications;
        this.messages = messages;
        this.eventsOfUser = eventsOfUser;
        this.tasksOfUser = tasksOfUser;
        this.habits = habits;
        this.individualSessions = individualSessions;
        this.allGroupSessions = allGroupSessions;
    }

    // --- GETTERS AND SETTERS ---

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<User> getFriends() { return friends; }
    public void setFriends(List<User> friends) { this.friends = friends; }

    public List<Notification> getNotifications() { return notifications; }
    public void setNotifications(List<Notification> notifications) { this.notifications = notifications; }

    public List<ChatMessage> getMessages() { return messages; }
    public void setMessages(List<ChatMessage> messages) { this.messages = messages; }

    public List<AppEvent> getEventsOfUser() { return eventsOfUser; }
    public void setEventsOfUser(List<AppEvent> eventsOfUser) { this.eventsOfUser = eventsOfUser; }

    public List<Habit> getHabits() { return habits; }
    public void setHabits(List<Habit> habits) { this.habits = habits; }

    public List<Session> getIndividualSessions() { return individualSessions; }
    public void setIndividualSessions(List<Session> individualSessions) { this.individualSessions = individualSessions; }

    public List<GroupSession> getAllGroupSessions() {
        return allGroupSessions;
    }
    public void setAllGroupSessions(List<GroupSession> allGroupSessions) {
        this.allGroupSessions = allGroupSessions;
    }

    public List<Task> getTasksOfUser() {
        return tasksOfUser;
    }

    public void setTasksOfUser(List<Task> tasksOfUser) {
        this.tasksOfUser = tasksOfUser;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }
    

}