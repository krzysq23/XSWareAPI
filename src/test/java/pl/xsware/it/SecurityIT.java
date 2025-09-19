package pl.xsware.it;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.xsware.Application;
import pl.xsware.TestMockConfig;
import pl.xsware.TestSwaggerSecurityConfig;
import pl.xsware.util.jwt.JwtUtils;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { Application.class, TestSwaggerSecurityConfig.class })
@AutoConfigureMockMvc
@Import(TestMockConfig.class)
@ActiveProfiles("dev")
public class SecurityIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void swagger_requiresBasicAuth() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", Matchers.containsString("Basic")));
    }

    @Test
    void swagger_withRoleSWAGGER_ok() throws Exception {
        mockMvc.perform(get("/v3/api-docs").with(httpBasic("swagger", "swagger")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void api_requiresUserRole_ok() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk());
    }

    @Test
    void api_withoutAuth_401() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isUnauthorized());
    }

}
