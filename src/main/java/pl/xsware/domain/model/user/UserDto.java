package pl.xsware.domain.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import pl.xsware.domain.model.RegisterRequest;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String userName;
    private String login;
    private String email;
    private String password;
    private List<RoleDto> roles;

    public static UserDto fromRegisterRequest(RegisterRequest request) {
        return UserDto.builder()
                .userName(request.getUserName())
                .login(request.getLogin())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}
