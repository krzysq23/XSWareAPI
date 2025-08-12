package pl.xsware.domain.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordRequest {
    @NotBlank
    private String login;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String password;
}
