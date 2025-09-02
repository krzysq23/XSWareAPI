package pl.xsware.domain.model.financialGoal;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class FinancialGoal {

    private Long id;
    private Long userId;
    private Long categoryId;
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;
    private LocalDate deadline;
    private Instant createdAt;
    private Instant updatedAt;
}
