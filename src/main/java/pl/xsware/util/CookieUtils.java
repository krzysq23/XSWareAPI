package pl.xsware.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${jwt.refreshExpirationMs}")
    private long refreshExpirationMs;
    @Value("${jwt.cookie.secure}")
    private boolean COOKIE_SECURE;
    @Value("${jwt.cookie.path}")
    private String REFRESH_COOKIE_PATH;
    @Value("${jwt.cookie.sameSite}")
    private String COOKIE_SAME_SITE;

    public ResponseCookie buildRefreshCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(COOKIE_SECURE)
                .path(REFRESH_COOKIE_PATH)
                .sameSite(COOKIE_SAME_SITE)
                .maxAge(refreshExpirationMs)
                .build();
    }

    public ResponseCookie deleteRefreshCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(COOKIE_SECURE)
                .path(REFRESH_COOKIE_PATH)
                .sameSite(COOKIE_SAME_SITE)
                .maxAge(0)
                .build();
    }
}
