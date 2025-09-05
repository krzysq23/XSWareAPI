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
import pl.xsware.config.auth.CurrentUserProvider;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.financialGoal.FinancialGoal;

import java.util.List;

@Slf4j
@Service
public class FinancialGoalService {

    @Value("${app.service.db-connector.endpoints.financialGoal.getAll}")
    private String getAllPath;
    @Value("${app.service.db-connector.endpoints.financialGoal.add}")
    private String addPath;
    @Value("${app.service.db-connector.endpoints.financialGoal.remove}")
    private String removePath;
    @Value("${app.service.db-connector.endpoints.financialGoal.edit}")
    private String editPath;

    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private WebClient webClient;

    public List<FinancialGoal> getAllFinancialGoals() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(getAllPath)
                        .build(currentUserProvider.getCurrentUserId())
                )
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}", request.getMethod(), request.getURI()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<FinancialGoal>>() {})
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void addFinancialGoal(@Valid FinancialGoal data) {
        webClient.post()
                .uri(addPath)
                .bodyValue(data)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), data))
                .retrieve()
                .bodyToMono(Response.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void removeFinancialGoal(@Valid FinancialGoal data) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(removePath)
                        .queryParam("financialGoalId", data.getId())
                        .queryParam("userId", data.getUserId())
                        .build()
                )
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nID: {}, userId: {}", request.getMethod(), request.getURI(), data.getId(), data.getUserId()))
                .retrieve()
                .bodyToMono(Response.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void editFinancialGoal(@Valid FinancialGoal data) {
        webClient.post()
                .uri(editPath)
                .bodyValue(data)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), data))
                .retrieve()
                .bodyToMono(Response.class)
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }
}
