package pl.xsware.domain.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.xsware.api.util.WebClientErrorHandler;
import pl.xsware.domain.model.auth.LoginRequest;
import pl.xsware.domain.model.user.UserRequest;
import pl.xsware.domain.model.user.UserDto;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Value("${app.service.db-connector.endpoints.user}")
    private String getUserPath;
    @Value("${app.service.db-connector.endpoints.userExist}")
    private String userExistPath;
    @Value("${app.service.db-connector.endpoints.usersAll}")
    private String allUsersPath;
    @Value("${app.service.db-connector.endpoints.userCreate}")
    private String createUserPath;
    @Value("${app.service.db-connector.endpoints.userEdit}")
    private String editUserPath;
    @Value("${app.service.db-connector.endpoints.userRemove}")
    private String removeUserPath;

    @Autowired
    private WebClient webClient;

    public UserDto getUserById(Long id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(getUserPath)
                        .build(id)
                )
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nuser ID: {}", request.getMethod(), request.getURI(), id))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public List<UserDto> getAllUsers() {
        return webClient.get()
                .uri(allUsersPath)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}", request.getMethod(), request.getURI()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDto>>() {})
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public boolean existUser(String login) {
        return Boolean.TRUE.equals(
                webClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path(userExistPath)
                                .build(login)
                        )
                        .httpRequest(request ->
                                log.info("\nREQUEST: {} {}, \nlogin: {}", request.getMethod(), request.getURI(), login))
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                        .block()
        );
    }

    public void createUser(UserRequest data) {
        UserDto user = UserDto.fromRegisterRequest(data);
        webClient.post()
                .uri(createUserPath)
                .bodyValue(user)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), user))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void editUser(UserRequest data) {
        UserDto user = UserDto.fromRegisterRequest(data);
        webClient.post()
                .uri(editUserPath)
                .bodyValue(user)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), user))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void removeUser(Long id) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(removeUserPath)
                        .build(id)
                )
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nuser ID: {}", request.getMethod(), request.getURI(), id))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

}
