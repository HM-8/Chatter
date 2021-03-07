package backend.models;

public class User extends JSONizable {
    public int id;
    public String username;
    public String fullName;

    public User(int id, String username, String fullName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
    }
}