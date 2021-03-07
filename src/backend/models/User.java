package backend.models;

public class User extends JSONizable {
    private int id;
    private String username;
    private String fullName;

    public User(int id, String username, String fullName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
    }
}