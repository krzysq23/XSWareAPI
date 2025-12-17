package pl.xsware.domain.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.xsware.domain.model.category.CategoryType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Transaction {

    private Long id;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private CategoryType type;
    private Instant createdAt;
    private Instant updatedAt;
}
