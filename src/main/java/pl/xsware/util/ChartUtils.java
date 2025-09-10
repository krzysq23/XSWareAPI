package pl.xsware.util;

import org.springframework.stereotype.Component;
import pl.xsware.domain.model.chart.Colors;
import pl.xsware.domain.model.chart.LinearChat;
import pl.xsware.domain.model.chart.PieChart;
import pl.xsware.domain.model.transaction.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ChartUtils {

    public List<PieChart> generateChatDataForTransactions(Map<String, List<Transaction>> transactionsMap, BigDecimal total) {
        Colors[] colors = Colors.values();
        AtomicInteger index = new AtomicInteger(0);
        return transactionsMap.entrySet().stream()
                .map(entry -> {
                    BigDecimal categorySum = entry.getValue().stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    String color = colors[index.getAndIncrement() % colors.length].name();
                    return PieChart.builder()
                            .label(entry.getKey())
                            .color(color)
                            .percent(
                                    categorySum
                                            .divide(total, 4, RoundingMode.HALF_UP)
                                            .multiply(BigDecimal.valueOf(100))
                                            .doubleValue()
                            )
                            .build();
                })
                .toList();
    }

    public List<LinearChat> generateLinearChartDataForTransactions(Map<String, List<Transaction>> transactionsMap) {
        return transactionsMap.entrySet().stream()
                .map(entry -> {
                    BigDecimal categorySum = entry.getValue().stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    String color = Colors.success.name();
                    return LinearChat.builder()
                            .label(entry.getKey())
                            .color(color)
                            .data(categorySum.doubleValue())
                            .build();
                })
                .toList();
    }
}
