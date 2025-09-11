package pl.xsware.domain.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.xsware.api.util.WebClientErrorHandler;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.auth.ChangePasswordRequest;
import pl.xsware.domain.model.auth.AuthRequest;
import pl.xsware.domain.model.user.UserDto;

@Slf4j
@Service
public class AuthService {

    @Value("${app.service.db-connector.endpoints.auth.authenticate}")
    private String authenticatePath;
    @Value("${app.service.db-connector.endpoints.auth.changePassword}")
    private String changePasswordPath;

    @Autowired
    private WebClient webClient;

    public UserDto authenticate(AuthRequest data) {
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

    public void changePassword(@Valid ChangePasswordRequest data) {
        webClient.post()
                .uri(changePasswordPath)
                .bodyValue(data)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), data))
                .retrieve()
                .bodyToMono(Response.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }
}
