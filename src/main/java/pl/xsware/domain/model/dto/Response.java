package pl.xsware.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    @JsonProperty("message")
    private String message;
}
