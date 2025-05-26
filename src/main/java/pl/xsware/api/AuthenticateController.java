package pl.xsware.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.dto.LoginRequest;
import pl.xsware.domain.model.dto.LoginResponse;
import pl.xsware.domain.model.dto.RegisterRequest;
import pl.xsware.domain.model.dto.Response;
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
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication.getName());
            return ResponseEntity.ok(LoginResponse.builder().token(jwt).name(loginRequest.getEmail()).build());
        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Nieprawidłowy login lub hasło");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody RegisterRequest data) {
        try {
            userService.createUser(data);
            return ResponseEntity.ok(Response.builder().message("Utowrzono użytkownika").build());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().message(ex.getMessage()).build());
        }
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
