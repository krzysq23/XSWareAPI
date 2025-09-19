package pl.xsware.domain.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequest {
    private String login;
    private String email;
    @NotBlank
    private String password;
}
