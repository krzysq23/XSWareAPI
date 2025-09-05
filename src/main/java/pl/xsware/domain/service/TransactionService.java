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
import pl.xsware.domain.model.transaction.Transaction;
import pl.xsware.domain.model.transaction.TransactionRequest;
import pl.xsware.domain.model.user.UserDto;

import java.util.List;

@Slf4j
@Service
public class TransactionService {

    @Value("${app.service.db-connector.endpoints.transaction.getAll}")
    private String getTransactionsPath;
    @Value("${app.service.db-connector.endpoints.transaction.add}")
    private String addPath;
    @Value("${app.service.db-connector.endpoints.transaction.remove}")
    private String removePath;
    @Value("${app.service.db-connector.endpoints.transaction.edit}")
    private String editPath;

    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private WebClient webClient;

    public List<Transaction> getTransactions(TransactionRequest data) {
        data.setUserId(currentUserProvider.getCurrentUserId());
        return webClient.post()
                .uri(getTransactionsPath)
                .bodyValue(data)
                .httpRequest(request ->
                        log.info("\nREQUEST: {} {}, \nbody: {}", request.getMethod(), request.getURI(), data))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Transaction>>() {})
                .onErrorResume(WebClientResponseException.class, WebClientErrorHandler::handle)
                .block();
    }

    public void addTransaction(@Valid Transaction data) {
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

    public void removeTransaction(@Valid Transaction data) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(removePath)
                        .queryParam("transactionId", data.getId())
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

    public void editTransaction(@Valid Transaction data) {
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
