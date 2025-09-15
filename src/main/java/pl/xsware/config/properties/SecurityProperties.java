package pl.xsware.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("security")
@Setter
@Getter
public class SecurityProperties {


    private List<AppUser> users = new ArrayList<>();

    @Setter
    @Getter
    public static class AppUser {

        private String username;
        private String password;
        private List<String> roles = new ArrayList<>();
    }

}
