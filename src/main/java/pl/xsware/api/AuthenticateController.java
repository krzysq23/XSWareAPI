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
import org.springframework.web.client.HttpClientErrorException;
import pl.xsware.domain.model.dto.*;
import pl.xsware.domain.model.entity.auth.User;
import pl.xsware.domain.service.UserService;
import pl.xsware.domain.service.UserService2;
import pl.xsware.util.JsonValidator;
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

            UserDto user = userService.getUserByEmail(loginRequest.getEmail());
            log.info("user: {}", user);


//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String jwt = jwtUtils.generateJwtToken(authentication.getName());

            LoginResponse loginResp = LoginResponse.builder().build();
//            LoginResponse loginResp = LoginResponse.builder()
//                    .token(jwt)
//                    .email(user.getEmail())
//                    .firstName(user.getFirstName())
//                    .lastName(user.getLastName())
//                    .build();
            return ResponseEntity.ok(loginResp);
        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Nieprawidłowy login lub hasło");
        } catch (HttpClientErrorException ex) {
            ErrorResponse error = ErrorResponse.parseResponse(ex.getStatusText(), ex.getStatusCode());
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody RegisterRequest data) {
        try {
//            userService.createUser(data);
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
