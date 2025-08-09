package pl.xsware.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientErrorHandler {

    public static <T> Mono<T> handle(WebClientResponseException ex) {
        log.error("\nRESPONSE ERROR: {} \nmessage: {}", ex.getMessage(), ex.getResponseBodyAsString());
        return Mono.error(new HttpClientErrorException(ex.getStatusCode(), ex.getResponseBodyAsString()));
    }
}
