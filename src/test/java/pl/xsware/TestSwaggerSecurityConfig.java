package pl.xsware;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSwaggerSecurityConfig {

    @Bean(name = "testSwaggerOnly")
    @Primary
    SecurityFilterChain testSwaggerOnly(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**","/v3/api-docs.yaml")
                .authorizeHttpRequests(a -> a.anyRequest().hasRole("SWAGGER"))
                .httpBasic(c -> c.realmName("Swagger"))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }


    @Bean(name = "testUsers")
    @Primary
    UserDetailsService testUsers() {
        return new InMemoryUserDetailsManager(
                User.withUsername("swagger").password("{noop}secret").roles("SWAGGER").build()
        );
    }
}
