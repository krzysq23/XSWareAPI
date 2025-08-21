package pl.xsware.domain.model.budget;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class BudgetLimit {

    private Long id;
    private Long userId;
    private Long categoryId;
    private BigDecimal amountLimit;
    private PeriodType periodType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Instant createdAt;
    private Instant updatedAt;
}
