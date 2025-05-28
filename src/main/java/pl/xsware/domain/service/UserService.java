package pl.xsware.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.xsware.domain.model.dto.UserDto;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Value("${app.service.db-connector.endpoints.createUser}")
    private String createUserPath;
    @Value("${app.service.db-connector.endpoints.getUserByEmail}")
    private String getUserByEmailPath;

    @Autowired
    private WebClient webClient;

    public UserDto getUserByEmail(String email) {
        UserDto user = UserDto.builder().email(email).build();
        return webClient.post()
                .uri(getUserByEmailPath)
                .bodyValue(user)
                .httpRequest(request -> log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), user))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("\nRESPONSE ERROR: {} \nmessage: {}", ex.getMessage(), ex.getResponseBodyAsString());
                    return Mono.error(new HttpClientErrorException(ex.getStatusCode(), ex.getResponseBodyAsString()));
                })
                .block();
    }

}
