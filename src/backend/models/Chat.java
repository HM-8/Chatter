package backend.models;

import java.util.ArrayList;

public class Chat extends JSONizable{
    private int id;
    private String title;
    private ArrayList<User> members = new ArrayList<>();
    private String type;
    public Chat() {
    }

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
    public boolean equals( Object o){
        if(o instanceof Chat){
            Chat c = (Chat)o;
            return this.id == c.id;
        }
        return false;
    }

    public String getType() {
        return type;
    }

    public int typeInt() {
        return type.equals("user")? 1 : type.equals("group")? 2: 3;
    }

    public void setType(String type) {
        this.type = type;
    }
}
