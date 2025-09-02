package pl.xsware.domain.model.budget;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class BudgetLimit {

    private Long id;
    private Long userId;
    private Long categoryId;
    private BigDecimal amountLimit;
    private BigDecimal amountSpent;
    private BigDecimal amountRemaining;
    private PeriodType periodType;
    private BudgetStatus status;
    private Integer percentSpent;
    private String note;
    private LocalDate startDate;
    private LocalDate endDate;
    private Instant createdAt;
    private Instant updatedAt;

    public void calculateBudgetStatus() {
        if (amountLimit == null || amountSpent == null) {
            this.status = BudgetStatus.UNKNOWN;
            this.percentSpent = 0;
            return;
        }
        BigDecimal percentSpent = amountSpent
                .multiply(new BigDecimal("100"))
                .divide(amountLimit, 2, RoundingMode.HALF_UP);
        if (percentSpent.compareTo(new BigDecimal("80")) < 0) {
            this.status = BudgetStatus.OK;
        } else if (percentSpent.compareTo(new BigDecimal("100")) > 0) {
            this.status = BudgetStatus.EXCEEDED;
        } else {
            this.status = BudgetStatus.NEAR_LIMIT;
        }
        this.percentSpent = percentSpent.intValue();
    }

}
