package pl.xsware.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.LoginRequest;
import pl.xsware.domain.model.LoginResponse;
import pl.xsware.domain.model.RegisterRequest;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.user.UserDto;
import pl.xsware.domain.service.UserService;
import pl.xsware.util.JwtUtils;

@RestController
@RequestMapping("/auth")
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    private final Logger log = LoggerFactory.getLogger(AuthenticateController.class);

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        UserDto user = userService.authenticate(loginRequest);
        String jwt = jwtUtils.generateJwtToken(user.getEmail());
        return ResponseEntity.ok(LoginResponse.create(user, jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody RegisterRequest data) {
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
