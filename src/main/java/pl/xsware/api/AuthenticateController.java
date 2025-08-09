package pl.xsware.api;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.auth.LoginRequest;
import pl.xsware.domain.model.auth.LoginResponse;
import pl.xsware.domain.model.user.UserRequest;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.user.UserDto;
import pl.xsware.domain.service.AuthService;
import pl.xsware.domain.service.UserService;
import pl.xsware.util.JwtUtils;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        UserDto user = authService.authenticate(loginRequest);
        String jwt = jwtUtils.generateJwtToken(user.getEmail());
        return ResponseEntity.ok(LoginResponse.create(user, jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid UserRequest data) {
        userService.createUser(data);
        return ResponseEntity.ok(Response.create("Utowrzono użytkownika"));
    }

    @GetMapping("/token/valid")
    public ResponseEntity<Response> tokenValid(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (!jwtUtils.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.builder().message("INVALID").build());
        }
        String user = jwtUtils.getEmailFromJwtToken(token);
        return ResponseEntity.ok(Response.builder().message("VALID_" + user).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "")
                .build();
    }

}
