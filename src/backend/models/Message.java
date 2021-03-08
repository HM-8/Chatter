package backend.models;

public class Message extends JSONizable {
    public int id;
    public int sentFrom;
    public String message;

    public Message() {
    }

    public Message(int sentFrom, String message) {
        this.sentFrom = sentFrom;
        this.message = message;
    }
}
