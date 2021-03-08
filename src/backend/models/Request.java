package backend.models;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
public class Request extends JSONizable {
    public String route;
    public Object data;

    public Request() {
    }

    public Request(String route, Object data) {
        this.route = route;
        this.data = data;
    }
}
