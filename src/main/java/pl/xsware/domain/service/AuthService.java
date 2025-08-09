package pl.xsware.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.xsware.api.util.WebClientErrorHandler;
import pl.xsware.domain.model.auth.LoginRequest;
import pl.xsware.domain.model.user.UserDto;

@Slf4j
@Service
public class AuthService {

    @Value("${app.service.db-connector.endpoints.userAuthenticate}")
    private String authenticatePath;

    @Autowired
    private WebClient webClient;

    public UserDto authenticate(LoginRequest data) {
        return webClient.post()
                .uri(authenticatePath)
                .bodyValue(data)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), data))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

}
