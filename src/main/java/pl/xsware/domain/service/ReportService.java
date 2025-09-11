package pl.xsware.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.xsware.config.auth.CurrentUserProvider;
import pl.xsware.domain.model.category.CategoryType;
import pl.xsware.domain.model.report.Report;
import pl.xsware.domain.model.report.ReportRequest;
import pl.xsware.domain.model.transaction.Transaction;
import pl.xsware.domain.model.transaction.TransactionRequest;
import pl.xsware.util.ChartUtils;
import pl.xsware.util.TransactionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportService {

    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionUtils transactionUtils;
    @Autowired
    private ChartUtils chartUtils;

    public Report filter(ReportRequest data) {
        if (data.getDateStart() == null || data.getDateEnd() == null) {
            throw new IllegalArgumentException("Data nie może być pusta.");
        }
        Report report = new Report();
        List<Transaction> transactions = transactionService.getTransactions(
                TransactionRequest.builder()
                        .startDate(data.getDateStart())
                        .endDate(data.getDateEnd())
                        .build()
        );
        BigDecimal totalIncome = transactionUtils.calculateTotalIncome(transactions);
        BigDecimal totalExpense = transactionUtils.calculateTotalExpense(transactions);
        report.setTransactions(transactions);
        report.setTotalIncome(totalIncome);
        report.setTotalExpense(totalExpense);
        report.setBalance(totalIncome.subtract(totalExpense));
        Map<String, List<Transaction>> groupedByCategory = transactionUtils.groupedByCategory(transactions, CategoryType.EXPENSE);
        report.setPieChartData(chartUtils.generateChatDataForTransactions(groupedByCategory, totalIncome));
        report.setLinearChartData(chartUtils.generateLinearChartDataForTransactions(transactions, data.getDateStart(), data.getDateEnd(), data.getTransactionType()));
        return report;
    }

    public Report generateReport(ReportRequest data) {
        return new Report();
    }
}
