package pl.xsware.domain.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRequest {

    private Long id;
    @NotBlank
    private String userName;
    @NotBlank
    private String login;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private List<String> roles;
}
