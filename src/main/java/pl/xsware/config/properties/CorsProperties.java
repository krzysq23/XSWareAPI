package pl.xsware.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("app.web.cors")
@Setter
@Getter
public class CorsProperties {
    private List<String> origins;
    private List<String> headers;
}
