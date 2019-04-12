package net.lviv.intoeat.testng.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

public class TestUtils {

    public static final int UNEXISTING_ENTITY_ID = 100;

    public static final String JSON_MIME_TYPE = "application/json;charset=UTF-8";

    public static final String NULL_ID_ERROR_MESSAGE = "The given id must not be null!";
    public static final String NOTHING_TO_DELETE_ERROR_MESSAGE = "Object id is null. Nothing to delete.";
    public static final String MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE = "One or more mandatory parameters are missing.";
    public static final String MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE = "Mandatory parameter 'name' is missing.";

    public static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static String convertToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
