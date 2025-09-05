package pl.xsware.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.xsware.config.auth.CurrentUserProvider;
import pl.xsware.domain.model.category.CategoryType;
import pl.xsware.domain.model.dashboard.Colors;
import pl.xsware.domain.model.dashboard.Dashboard;
import pl.xsware.domain.model.dashboard.DashboardChart;
import pl.xsware.domain.model.financialGoal.FinancialGoal;
import pl.xsware.domain.model.transaction.Transaction;
import pl.xsware.domain.model.transaction.TransactionRange;
import pl.xsware.domain.model.transaction.TransactionRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DashboardService
{
    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private FinancialGoalService financialGoalService;

    public Dashboard getDashboardInfo() {
        Dashboard dInfo = Dashboard.builder().build();
        List<Transaction> transactions = transactionService.getTransactions(
                TransactionRequest.builder()
                        .dateRange(TransactionRange.LAST_MONTH.getValue())
                        .build()
        );
        List<FinancialGoal> financialGoals = financialGoalService.getAllFinancialGoals();
        dInfo.setFinancialGoals(
                financialGoals.stream().limit(3).collect(Collectors.toList())
        );
        BigDecimal balance = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dInfo.setBalance(balance);
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == CategoryType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dInfo.setTotalIncome(totalIncome);
        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == CategoryType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dInfo.setTotalExpense(totalExpense);
        dInfo.setTransactions(
                transactions.stream()
                        .sorted(Comparator.comparing(Transaction::getDate).reversed())
                        .limit(5)
                        .collect(Collectors.toList())
        );
        Map<String, List<Transaction>> groupedByCategory = transactions.stream()
                .filter(t -> t.getType() == CategoryType.EXPENSE)
                .collect(Collectors.groupingBy(Transaction::getCategoryName));
        Colors[] colors = Colors.values();
        AtomicInteger index = new AtomicInteger(0);
        List<DashboardChart> chartData = groupedByCategory.entrySet().stream()
                .map(entry -> {
                    BigDecimal categorySum = entry.getValue().stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    String color = colors[index.getAndIncrement() % colors.length].name();
                    return DashboardChart.builder()
                            .categoryName(entry.getKey())
                            .color(color)
                            .percent(
                                    categorySum
                                            .divide(totalIncome, 4, RoundingMode.HALF_UP)
                                            .multiply(BigDecimal.valueOf(100))
                                            .doubleValue()
                            )
                            .build();
                })
                .toList();
        dInfo.setChartData(chartData);
        return dInfo;
    }
}
