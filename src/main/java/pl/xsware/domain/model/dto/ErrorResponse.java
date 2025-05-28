package pl.xsware.domain.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import pl.xsware.util.JsonValidator;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String timestamp;
    private int status;
    private String error;
    private String message;

    public static ErrorResponse parseResponse(String response, HttpStatusCode statusCode) {
        return JsonValidator.isValidJson(response)
                ? JsonValidator.parseErrorResponse(response)
                : ErrorResponse.builder()
                    .timestamp(LocalDateTime.now().toString())
                    .status(statusCode.hashCode())
                    .error(statusCode.toString())
                    .message(response)
                    .build();
    }
}
