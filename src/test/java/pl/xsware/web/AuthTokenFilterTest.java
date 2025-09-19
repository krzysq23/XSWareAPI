package pl.xsware.web;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.xsware.config.auth.AuthTokenFilter;
import pl.xsware.domain.model.auth.JwtType;
import pl.xsware.domain.model.auth.JwtValidation;
import pl.xsware.util.jwt.JwtUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @Mock
    JwtUtils jwtUtils;
    @InjectMocks
    AuthTokenFilter filter;

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void noAuthorizationHeader_doesNotAuthenticate() throws Exception {

        lenient().when(jwtUtils.validateJwtToken(any(), any()))
                .thenReturn(JwtValidation.VALID);

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/api/info");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, res, chain);

        verifyNoInteractions(jwtUtils);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void validBearer_setsAuthentication() throws Exception {

        when(jwtUtils.validateJwtToken(isNull(), eq(JwtType.ACCESS)))
                .thenReturn(JwtValidation.VALID);

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/api/info");
        req.addHeader("Authorization", "Bearer valid.jwt.token");
        MockHttpServletResponse res = new MockHttpServletResponse();

        filter.doFilter(req, res, (r, s) -> {});

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    }
}
