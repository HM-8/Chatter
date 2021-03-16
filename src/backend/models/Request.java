package backend.models;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Request extends JSONizable {
    public String route;
    public ArrayList<String> data = new ArrayList<>();

    public Request() {
    }

    public Request(String route, String[] data) {
        this.route = route;
        this.data.addAll(Arrays.asList(data));
    }
}
