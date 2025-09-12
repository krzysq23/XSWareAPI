package pl.xsware.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.xsware.domain.model.auth.JwtValidation;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), Map.of(
                "status", 401,
                "error", "Unauthorized",
                "message", JwtValidation.EXPIRED.getLabel(),
                "path", request.getServletPath()
        ));
    }

}
