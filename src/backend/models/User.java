package backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"fullName"})
public class User extends JSONizable {
    public int id;
    public String username;
    public String firstName;
    public String lastName;
    
    public User() {
    }

    public User(int id, String username, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

}