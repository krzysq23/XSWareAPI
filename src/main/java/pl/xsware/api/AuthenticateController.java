package pl.xsware.api;

import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import pl.xsware.config.properties.AppConstants;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.auth.*;
import pl.xsware.domain.model.user.UserDto;
import pl.xsware.domain.model.user.UserRequest;
import pl.xsware.domain.service.AuthService;
import pl.xsware.domain.service.UserService;
import pl.xsware.util.cookie.CookieUtils;
import pl.xsware.util.jwt.JwtUtils;

import static pl.xsware.domain.model.auth.JwtValidation.EXPIRED;
import static pl.xsware.domain.model.auth.JwtValidation.VALID;

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

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid AuthRequest authRequest) {
        UserDto user = authService.authenticate(authRequest);
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        ResponseCookie refreshCookie = cookieUtils.buildRefreshCookie(AppConstants.REFRESH_COOKIE_NAME, refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(AuthResponse.builder().user(user).accessToken(accessToken).build());
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid UserRequest data) {
        userService.createUser(data);
        return ResponseEntity.ok(Response.create("Utowrzono użytkownika"));
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@CookieValue(name = AppConstants.REFRESH_COOKIE_NAME, required = false) String refreshCookieValue) {
        if (refreshCookieValue == null || refreshCookieValue.isBlank()) {
            throw new JwtException(JwtValidation.NONE.getLabel());
        }

        Token token = authService.createNewTokens(refreshCookieValue);
        ResponseCookie newCookie = cookieUtils.buildRefreshCookie(AppConstants.REFRESH_COOKIE_NAME, token.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newCookie.toString())
                .body(new TokenResponse(token.getAccessToken()));
    }

    @GetMapping("/status")
    public ResponseEntity<TokenResponse> status(
            @CookieValue(name = AppConstants.REFRESH_COOKIE_NAME, required = false) String refreshCookieValue,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader
    ) {
        if (refreshCookieValue == null || refreshCookieValue.isBlank()) {
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                JwtValidation valid = jwtUtils.validateJwtToken(jwt, JwtType.ACCESS);
                if(valid.equals(VALID) || valid.equals(EXPIRED)) {
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            } else {
                return ResponseEntity.ok().build();
            }
        }

        Token token = authService.createNewTokens(refreshCookieValue);
        ResponseCookie newCookie = cookieUtils.buildRefreshCookie(AppConstants.REFRESH_COOKIE_NAME, token.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newCookie.toString())
                .body(new TokenResponse(token.getAccessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout() {
        ResponseCookie delete = cookieUtils.deleteRefreshCookie(AppConstants.REFRESH_COOKIE_NAME);
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
