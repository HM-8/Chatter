package backend.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Chat extends JSONizable{
    private int id;
    private String title;
    private ArrayList<User> members = new ArrayList<>();

    public Chat(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Chat(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<User> getMembers() {
        return members;
    }
    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }
    public void addMember(User member){
        this.members.add(member);
    }
    @Override
    public boolean equals(@NotNull Object o){
        if(o instanceof Chat){
            Chat c = (Chat)o;
            return this.id == c.id;
        }
        return false;
    }
}
