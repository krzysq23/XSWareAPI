package pl.xsware.domain.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtValidation {
    VALID("TOKEN_VALID"),
    EXPIRED("TOKEN_EXPIRED"),
    INVALID("TOKEN_NOT_VALID"),
    NONE("NO_AUTHENTICATION_TOKEN");

    private final String label;
}
