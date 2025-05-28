package pl.xsware.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
