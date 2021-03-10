package backend.models;

public class ErrorMessage extends JSONizable {
    public String message;

    public ErrorMessage() {}

    public ErrorMessage(String message) {
        this.message = message;
    }
}
