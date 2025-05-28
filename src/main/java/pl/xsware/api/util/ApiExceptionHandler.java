package pl.xsware.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import java.util.UUID;

@ControllerAdvice
public class ApiExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<Object> handleUnknownException(Throwable ex, ServerWebExchange exchange) {
        String errorId = UUID.randomUUID().toString();
        String message = "Request error occurred - " + errorId;
        String path = exchange.getRequest().getPath().toString();
        log.error("{} for path: {}", message, path, ex);
        HttpStatusCode status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof ErrorResponse) {
            status = ((ErrorResponse) ex).getStatusCode();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseEntity<>(message, status));
    }
}
