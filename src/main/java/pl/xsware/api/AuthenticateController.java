package pl.xsware.api;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.auth.*;
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
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        UserDto user = authService.authenticate(authRequest);
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        return ResponseEntity.ok(AuthResponse.create(user, accessToken, refreshToken));
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new io.jsonwebtoken.JwtException(JwtValidation.NONE.getLabel());
        }
        String refreshToken = authHeader.substring(7);
        var result = jwtUtils.validateJwtToken(refreshToken, JwtType.REFRESH);
        if (result != JwtValidation.VALID) {
            throw new io.jsonwebtoken.JwtException(result.getLabel());
        }
        UserDto user = jwtUtils.getUserFromJwtToken(refreshToken);
        String newAccess = jwtUtils.generateAccessToken(user);
        String newRefresh = jwtUtils.generateRefreshToken(user);
        return ResponseEntity.ok(AuthResponse.create(null, newAccess, newRefresh));
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid UserRequest data) {
        userService.createUser(data);
        return ResponseEntity.ok(Response.create("Utowrzono użytkownika"));
    }

    @GetMapping("/token/valid")
    public ResponseEntity<Response> tokenValid(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (!jwtUtils.validateJwtToken(token, JwtType.ACCESS).equals(JwtValidation.VALID)) {
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

    @PostMapping("/changePassword")
    public ResponseEntity<Response> changePassword(@RequestBody @Valid ChangePasswordRequest data) {
        authService.changePassword(data);
        return ResponseEntity.ok(Response.create("OK"));
    }

}
