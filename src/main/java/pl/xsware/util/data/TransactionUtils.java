package pl.xsware.util.data;

import org.springframework.stereotype.Component;
import pl.xsware.domain.model.category.CategoryType;
import pl.xsware.domain.model.transaction.Transaction;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransactionUtils {

    public BigDecimal calculateBalance(List<Transaction> transactions) {
        return transactions.stream()
                .map(t -> switch (t.getType()) {
                    case INCOME -> t.getAmount();
                    case EXPENSE -> t.getAmount().negate();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType() == CategoryType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalExpense(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType() == CategoryType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Transaction> getLimitedTransactions(List<Transaction> transactions, int limit) {
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, List<Transaction>> groupedByCategory(List<Transaction> transactions, CategoryType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.groupingBy(Transaction::getCategoryName));
    }
}
