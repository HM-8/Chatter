package backend.models;

public class Message extends JSONizable {
    public int id;
    public int from;
    public String message;
    public int to;

    public Message() {
    }

    public Message(int from, String message) {
        this.from = from;
        this.message = message;
    }

    public Message(int id, int from, String message, int to) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.to = to;
    }
}
