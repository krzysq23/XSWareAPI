package pl.xsware.domain.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

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
    private List<String> roles;

    public static UserDto fromRegisterRequest(UserRequest request) {
        return UserDto.builder()
                .id(request.getId())
                .userName(request.getUserName())
                .login(request.getLogin())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .build();
    }
}
