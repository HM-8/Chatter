package backend.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
@JsonSubTypes.Type(value = Chat.class, name = "chat")
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
    public static JSONizable[] fromJSONArray(String JSONarray, Class c) throws JsonProcessingException {
        return (JSONizable[]) mapper.readValue(JSONarray, c);
    }
}
