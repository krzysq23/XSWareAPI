package pl.xsware.domain.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.xsware.api.util.WebClientErrorHandler;
import pl.xsware.domain.model.category.Category;
import pl.xsware.domain.model.user.UserDto;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class CategoryService {

    @Value("${app.service.db-connector.endpoints.category.getAll}")
    private String getAllPath;
    @Value("${app.service.db-connector.endpoints.category.add}")
    private String addPath;
    @Value("${app.service.db-connector.endpoints.category.remove}")
    private String removePath;
    @Value("${app.service.db-connector.endpoints.category.edit}")
    private String editPath;

    @Autowired
    private WebClient webClient;

    public List<Category> getAllCategory(Long userId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(getAllPath)
                        .build(userId)
                )
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}", request.getMethod(), request.getURI()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Category>>() {})
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void addCategory(@Valid Category data) {
        data.setCreatedAt(Instant.now());
        webClient.post()
                .uri(addPath)
                .bodyValue(data)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), data))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void removeCategory(@Valid Category data) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(removePath)
                        .queryParam("categoryId", data.getId())
                        .queryParam("userId", data.getUserId())
                        .build()
                )
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nID: {}, userId: {}", request.getMethod(), request.getURI(), data.getId(), data.getUserId()))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void editCategory(@Valid Category data) {
        webClient.post()
                .uri(editPath)
                .bodyValue(data)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), data))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }
}
