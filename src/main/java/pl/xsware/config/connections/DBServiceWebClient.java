package pl.xsware.config.connections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class DBServiceWebClient {

    @Value("${app.service.db-connector.url}")
    private String url;
    @Value("${app.service.db-connector.auth.user}")
    private String username;
    @Value("${app.service.db-connector.auth.password}")
    private String password;

    private final Logger log = LoggerFactory.getLogger(DBServiceWebClient.class);

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.setBasicAuth(username, password);
                    httpHeaders.add("Content-Type", "application/json");
                })
                .filter(logResponse())
                .filter(errorHandlingFilter())
                .build();
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            response.bodyToMono(String.class)
                    .flatMap(body -> {
                        log.info("RESPONSE: {}, body: {}", response.statusCode(), body);
                        return Mono.just(response);
                    });
            return Mono.just(response);
        });
    }

    private ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(this::handleErrors);
    }

    private Mono<ClientResponse> handleErrors(ClientResponse response) {
        if (response.statusCode().is4xxClientError()) {
            // TODO:
        } else if (response.statusCode().is5xxServerError()) {
            // TODO:
        }
        return Mono.just(response);
    }
}
