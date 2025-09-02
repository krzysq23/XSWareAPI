package pl.xsware.domain.model.budget;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
@Builder

public class BudgetResponse {

    private List<BudgetLimit> budgets;
    private BigDecimal totalLimit;
    private BigDecimal totalSpent;
    private BigDecimal totalRemaining;

    public void calculate() {
        this.totalLimit = budgets.stream()
                .map(BudgetLimit::getAmountLimit)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalSpent = budgets.stream()
                .map(BudgetLimit::getAmountSpent)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalRemaining = budgets.stream()
                .map(BudgetLimit::getAmountRemaining)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
