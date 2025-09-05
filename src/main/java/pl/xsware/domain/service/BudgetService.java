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
import pl.xsware.domain.model.budget.BudgetLimit;
import pl.xsware.domain.model.budget.BudgetRequest;
import pl.xsware.domain.model.budget.PeriodType;
import pl.xsware.domain.model.user.UserDto;

import java.util.List;

@Slf4j
@Service
public class BudgetService {

    @Value("${app.service.db-connector.endpoints.budget.getBudgets}")
    private String getBudgetsPath;
    @Value("${app.service.db-connector.endpoints.budget.add}")
    private String addPath;
    @Value("${app.service.db-connector.endpoints.budget.remove}")
    private String removePath;
    @Value("${app.service.db-connector.endpoints.budget.edit}")
    private String editPath;

    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private WebClient webClient;

    public List<BudgetLimit> getBudgetByPeriodType(BudgetRequest data) {
        data.setUserId(currentUserProvider.getCurrentUserId());
        return webClient.post()
                .uri(getBudgetsPath)
                .bodyValue(data)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), data))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BudgetLimit>>() {})
                .map(list -> {
                    list.forEach(BudgetLimit::calculateBudgetStatus);
                    return list;
                })
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void addBudget(@Valid BudgetLimit data) {
        data.setUserId(currentUserProvider.getCurrentUserId());
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

    public void removeBudget(@Valid BudgetLimit data) {
        data.setUserId(currentUserProvider.getCurrentUserId());
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(removePath)
                        .queryParam("budgetId", data.getId())
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

    public void editBudget(@Valid BudgetLimit data) {
        data.setUserId(currentUserProvider.getCurrentUserId());
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
