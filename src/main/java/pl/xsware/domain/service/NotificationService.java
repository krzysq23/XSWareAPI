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
import pl.xsware.domain.model.notification.Notification;
import pl.xsware.domain.model.user.UserDto;

import java.util.List;

@Slf4j
@Service
public class NotificationService {

    @Value("${app.service.db-connector.endpoints.notification.getAll}")
    private String getAllPath;
    @Value("${app.service.db-connector.endpoints.notification.add}")
    private String addPath;
    @Value("${app.service.db-connector.endpoints.notification.remove}")
    private String removePath;
    @Value("${app.service.db-connector.endpoints.notification.changeAsRead}")
    private String changeAsReadPath;

    @Autowired
    private WebClient webClient;

    public List<Notification> getAllNotifications(Long userId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(getAllPath)
                        .build(userId)
                )
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}", request.getMethod(), request.getURI()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Notification>>() {})
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void removeNotification(@Valid Notification data) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(removePath)
                        .build(data.getId(), data.getUserId())
                )
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nID: {}, userId: {}", request.getMethod(), request.getURI(), data.getId(), data.getUserId()))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void changeNotificationAsRead(@Valid List<Notification> data) {
        webClient.post()
                .uri(changeAsReadPath)
                .bodyValue(data)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), data))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }
}
