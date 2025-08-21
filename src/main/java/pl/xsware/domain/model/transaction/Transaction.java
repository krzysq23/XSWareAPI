package pl.xsware.domain.model.transaction;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class Transaction {

    private Long id;
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private Instant date;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
