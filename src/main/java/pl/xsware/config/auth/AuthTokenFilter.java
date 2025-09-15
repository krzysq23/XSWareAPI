package pl.xsware.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.xsware.domain.model.auth.CustomUserDetails;
import pl.xsware.domain.model.auth.JwtType;
import pl.xsware.domain.model.auth.JwtValidation;
import pl.xsware.domain.service.UserService;
import pl.xsware.util.jwt.JwtUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return PATH_MATCHER.match("/auth/**", path)
                || PATH_MATCHER.match("/public/**", path)
                || PATH_MATCHER.match("/error", path)
                || "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException
    {

        String jwt = parseJwt(request);
        JwtValidation valid = jwtUtils.validateJwtToken(jwt, JwtType.ACCESS);
        switch (valid) {
            case VALID -> {
                String email = jwtUtils.getEmailFromJwtToken(jwt);
                Long userId = jwtUtils.getUserIdFromJwtToken(jwt);
                List<String> roles = jwtUtils.getRolesFromJwtToken(jwt);

                List<GrantedAuthority> authorities = new ArrayList<>(
                        roles.stream().map(SimpleGrantedAuthority::new).toList()
                );

                CustomUserDetails userDetails = new CustomUserDetails(
                        userId,
                        email,
                        null,
                        authorities
                );

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
            case EXPIRED -> {
                writeUnauthorized(response, JwtValidation.EXPIRED.getLabel());
                return;
            }
            case INVALID, NONE -> {
                writeUnauthorized(response, JwtValidation.INVALID.getLabel());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        if (response.isCommitted()) return;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                "Bearer error=\"invalid_token\", error_description=\"Invalid token\"");
        new ObjectMapper().writeValue(response.getOutputStream(), Map.of(
                "status", 401,
                "error", "Unauthorized",
                "message", message
        ));
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

}
