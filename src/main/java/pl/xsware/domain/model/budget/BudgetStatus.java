package pl.xsware.domain.model.budget;

import java.math.BigDecimal;

public enum BudgetStatus {
    OK,
    NEAR_LIMIT,
    EXCEEDED,
    UNKNOWN;

    public static BudgetStatus calculate(BigDecimal percentSpent) {
        if (percentSpent == null) {
            return UNKNOWN;
        }
        if (percentSpent.compareTo(new BigDecimal("80")) < 0) {
            return OK;
        } else if (percentSpent.compareTo(new BigDecimal("100")) > 0) {
            return EXCEEDED;
        } else {
            return NEAR_LIMIT;
        }
    }

}
