package backend.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
public class JSONizable {
    private static ObjectMapper mapper = new ObjectMapper();

    public JSONizable() {
    }

    public String toJSON() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
    public static JSONizable fromJSON(String JSON) throws JsonProcessingException {
        return mapper.readValue(JSON, JSONizable.class);
    }
}
