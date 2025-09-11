package pl.xsware.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.xsware.config.auth.CurrentUserProvider;
import pl.xsware.domain.model.category.CategoryType;
import pl.xsware.domain.model.chart.PieChart;
import pl.xsware.domain.model.chart.Colors;
import pl.xsware.domain.model.dashboard.Dashboard;
import pl.xsware.domain.model.financialGoal.FinancialGoal;
import pl.xsware.domain.model.transaction.Transaction;
import pl.xsware.domain.model.transaction.TransactionRange;
import pl.xsware.domain.model.transaction.TransactionRequest;
import pl.xsware.util.ChartUtils;
import pl.xsware.util.TransactionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Autowired
    private TransactionUtils transactionUtils;
    @Autowired
    private ChartUtils chartUtils;

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
        BigDecimal totalIncome = transactionUtils.calculateTotalIncome(transactions);
        BigDecimal totalExpense = transactionUtils.calculateTotalExpense(transactions);
        dInfo.setTotalIncome(totalIncome);
        dInfo.setTotalExpense(totalExpense);
        dInfo.setBalance(totalIncome.subtract(totalExpense));
        dInfo.setTransactions(transactionUtils.getLimitedTransactions(transactions, 5));
        Map<String, List<Transaction>> groupedByCategory = transactionUtils.groupedByCategory(transactions, CategoryType.EXPENSE);
        dInfo.setChartData(chartUtils.generateChatDataForTransactions(groupedByCategory, totalIncome));
        return dInfo;
    }
}
