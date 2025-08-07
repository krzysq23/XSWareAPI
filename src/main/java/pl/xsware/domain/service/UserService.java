package pl.xsware.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.xsware.domain.model.LoginRequest;
import pl.xsware.domain.model.RegisterRequest;
import pl.xsware.domain.model.user.UserDto;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Value("${app.service.db-connector.endpoints.userAuthenticate}")
    private String authenticatePath;
    @Value("${app.service.db-connector.endpoints.userCreate}")
    private String createUserPath;
    @Value("${app.service.db-connector.endpoints.userExist}")
    private String userExistPath;

    @Autowired
    private WebClient webClient;

    public UserDto authenticate(LoginRequest data) {
        return webClient.post()
                .uri(authenticatePath)
                .bodyValue(data)
                .httpRequest(request -> log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), data))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("\nRESPONSE ERROR: {} \nmessage: {}", ex.getMessage(), ex.getResponseBodyAsString());
                    return Mono.error(new HttpClientErrorException(ex.getStatusCode(), ex.getResponseBodyAsString()));
                })
                .block();
    }

    public void createUser(RegisterRequest data) {
        UserDto user = UserDto.fromRegisterRequest(data);
        webClient.post()
                .uri(createUserPath)
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
