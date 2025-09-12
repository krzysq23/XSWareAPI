package pl.xsware.api;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.auth.*;
import pl.xsware.domain.model.user.UserRequest;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.user.UserDto;
import pl.xsware.domain.service.AuthService;
import pl.xsware.domain.service.UserService;
import pl.xsware.util.CookieUtils;
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
    private CookieUtils cookieUtils;

    @Autowired
    private JwtUtils jwtUtils;

    public static final String REFRESH_COOKIE_NAME = "refreshToken";

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        UserDto user = authService.authenticate(authRequest);
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        ResponseCookie refreshCookie = cookieUtils.buildRefreshCookie(REFRESH_COOKIE_NAME, refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(AuthResponse.create(user, accessToken));
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid UserRequest data) {
        userService.createUser(data);
        return ResponseEntity.ok(Response.create("Utowrzono użytkownika"));
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshCookieValue) {
        if (refreshCookieValue == null || refreshCookieValue.isBlank()) {
            throw new io.jsonwebtoken.JwtException(JwtValidation.NONE.getLabel());
        }

        var result = jwtUtils.validateJwtToken(refreshCookieValue, JwtType.REFRESH);
        if (result != JwtValidation.VALID) {
            ResponseCookie delete = cookieUtils.deleteRefreshCookie(REFRESH_COOKIE_NAME);
            return ResponseEntity.status(401)
                    .header(HttpHeaders.SET_COOKIE, delete.toString())
                    .build();
        }

        UserDto user = jwtUtils.getUserFromJwtToken(refreshCookieValue);
        String newAccess = jwtUtils.generateAccessToken(user);
        String newRefresh = jwtUtils.generateRefreshToken(user);

        ResponseCookie newCookie = cookieUtils.buildRefreshCookie(REFRESH_COOKIE_NAME, newRefresh);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newCookie.toString())
                .body(AuthResponse.create(null, newAccess));
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout() {
        ResponseCookie delete = cookieUtils.deleteRefreshCookie(REFRESH_COOKIE_NAME);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, delete.toString())
                .body(Response.create("Wylogowano"));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Response> changePassword(@RequestBody @Valid ChangePasswordRequest data) {
        authService.changePassword(data);
        return ResponseEntity.ok(Response.create("OK"));
    }

}
