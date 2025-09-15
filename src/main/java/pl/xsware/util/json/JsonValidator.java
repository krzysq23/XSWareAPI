package pl.xsware.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.xsware.domain.model.ErrorResponse;

public class JsonValidator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(JsonValidator.class);

    public static boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ErrorResponse parseErrorResponse(String json) {
        try {
            return objectMapper.readValue(json, ErrorResponse.class);
        } catch (Exception e) {
            log.error("ERROR: Nie można sparsować elementu: {}", json);
            return ErrorResponse.builder().build();
        }
    }
}
