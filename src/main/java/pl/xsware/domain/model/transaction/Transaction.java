package pl.xsware.domain.model.transaction;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class Transaction {

    private Long id;
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
