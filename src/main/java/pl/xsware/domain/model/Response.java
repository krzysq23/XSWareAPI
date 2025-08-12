package pl.xsware.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private ResponseStatus status;

    public static Response create(String message) {
        return Response.builder()
                .message(message)
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    public static Response create(String message, ResponseStatus status) {
        return Response.builder()
                .message(message)
                .status(status)
                .build();
    }
}
