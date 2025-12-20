package cs102groupproject.Server.model.entity;

/**
 * Represents a user record stored in the database. Used internally by the server and never exposed to the client.
 */
public class UserEntity {
    private String id;
    private String email;
    private String passwordHash;
    private int soloCurrency;
    private int groupCurrency;
    private boolean verified;
}
