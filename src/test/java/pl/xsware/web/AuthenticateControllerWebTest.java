package pl.xsware.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import pl.xsware.TestMockConfig;
import pl.xsware.api.AuthenticateController;
import pl.xsware.config.properties.AppConstants;
import pl.xsware.domain.model.user.UserDto;
import pl.xsware.domain.service.AuthService;
import pl.xsware.util.cookie.CookieUtils;
import pl.xsware.util.jwt.JwtUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthenticateController.class)
@Import(TestMockConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticateControllerWebTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    CookieUtils cookieUtils;

    @Test
    void authenticateUser_ShouldReturn200_WithTokenAndUser() throws Exception {
        var user = userDto();

        when(authService.authenticate(any())).thenReturn(user);

        when(jwtUtils.generateAccessToken(any())).thenReturn("jwt-abc");
        when(jwtUtils.generateRefreshToken(any())).thenReturn("r-xyz");

        ResponseCookie refreshCookie = ResponseCookie.from(AppConstants.REFRESH_COOKIE_NAME, "r-xyz")
                .httpOnly(true).path("/").build();
        when(cookieUtils.buildRefreshCookie(eq(AppConstants.REFRESH_COOKIE_NAME), eq("r-xyz")))
                .thenReturn(refreshCookie);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"login":"test","password":"s3cret"}
                """))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, org.hamcrest.Matchers.containsString(AppConstants.REFRESH_COOKIE_NAME)))
                .andExpect(jsonPath("$.accessToken").value("jwt-abc"))
                .andExpect(jsonPath("$.user.login").value("test"));
    }

    @Test
    void authenticateUser_ShouldReturn400_WhenMissingPassword() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
              {"login":"test"}
            """))
                .andExpect(status().isBadRequest());
    }

    private UserDto userDto() {
        return UserDto.builder()
                .id(1L)
                .login("test")
                .password("s3cret")
                .email("test@x.com")
                .userName("Kowalski")
                .build();
    }
}
