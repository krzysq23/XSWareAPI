package pl.xsware.domain.model.report;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.xsware.domain.model.chart.LinearChart;
import pl.xsware.domain.model.chart.PieChart;
import pl.xsware.domain.model.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class Report {

    private BigDecimal balance;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private List<Transaction> transactions;
    private List<PieChart> pieChartData;
    private LinearChart linearChartData;
}