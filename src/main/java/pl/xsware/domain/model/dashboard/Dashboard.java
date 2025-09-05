package pl.xsware.domain.model.dashboard;

import lombok.Builder;
import lombok.Data;
import pl.xsware.domain.model.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class Dashboard {

    private BigDecimal balance;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private List<Transaction> transactions;
}
