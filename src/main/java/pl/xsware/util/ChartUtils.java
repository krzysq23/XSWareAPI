package pl.xsware.util;

import org.springframework.stereotype.Component;
import pl.xsware.domain.model.category.CategoryType;
import pl.xsware.domain.model.chart.*;
import pl.xsware.domain.model.transaction.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    public LinearChart generateLinearChartDataForTransactions(
            List<Transaction> transactions, LocalDate dateStart, LocalDate dateEnd, String transactionType
    ) {

        final boolean wantIncome  = "all".equalsIgnoreCase(transactionType) || CategoryType.INCOME.name().equalsIgnoreCase(transactionType);
        final boolean wantExpense = "all".equalsIgnoreCase(transactionType) || CategoryType.EXPENSE.name().equalsIgnoreCase(transactionType);

        long days = ChronoUnit.DAYS.between(dateStart, dateEnd) + 1;
        boolean daily = days <= 31;

        LinearChart chart = new LinearChart();

        if (daily) {

            List<LocalDate> daysSeq = dateRange(dateStart, dateEnd);
            Map<LocalDate, BigDecimal> income = wantIncome
                    ? daysSeq.stream().collect(Collectors.toMap(d -> d, d -> BigDecimal.ZERO, (a, b)->a, LinkedHashMap::new)) : null;
            Map<LocalDate, BigDecimal> expense = wantExpense
                    ? daysSeq.stream().collect(Collectors.toMap(d -> d, d -> BigDecimal.ZERO, (a,b)->a, LinkedHashMap::new)) : null;

            for (LocalDate d : daysSeq) {
                String label = d.getDayOfMonth() + " " + MonthName.fromNumber(d.getMonthValue()).getShortName();
                chart.getLabels().add(label);
            }

            for (Transaction t : transactions) {
                LocalDate d = t.getDate();
                if (d == null || d.isBefore(dateStart) || d.isAfter(dateEnd)) continue;

                if (t.getType() == CategoryType.INCOME && wantIncome && income.containsKey(d)) {
                    income.put(d, income.get(d).add(t.getAmount()));
                } else if (t.getType() == CategoryType.EXPENSE && wantExpense && expense.containsKey(d)) {
                    expense.put(d, expense.get(d).add(t.getAmount()));
                }
            }

            if (wantIncome) {
                chart.getDatasets().add(
                        LinearChartDataset.builder()
                                .label(CategoryType.INCOME.getLabel())
                                .color(Colors.success.name())
                                .data(income.values().stream().toList())
                                .build()
                );
            }
            if (wantExpense) {
                chart.getDatasets().add(
                        LinearChartDataset.builder()
                                .label(CategoryType.EXPENSE.getLabel())
                                .color(Colors.primary.name())
                                .data(expense.values().stream().toList())
                                .build()
                );
            }

        } else {

            List<YearMonth> montSeq = monthRange(YearMonth.from(dateStart), YearMonth.from(dateEnd));
            Map<YearMonth, BigDecimal> income  = wantIncome
                    ? montSeq.stream().collect(Collectors.toMap(m -> m, m -> BigDecimal.ZERO, (a,b)->a, LinkedHashMap::new)) : null;
            Map<YearMonth, BigDecimal> expense = wantExpense
                    ? montSeq.stream().collect(Collectors.toMap(m -> m, m -> BigDecimal.ZERO, (a,b)->a, LinkedHashMap::new)) : null;

            for (YearMonth ym : montSeq) {
                chart.getLabels().add(MonthName.fromNumber(ym.getMonthValue()).getShortName());
            }

            for (Transaction t : transactions) {
                LocalDate d = t.getDate();
                if (d == null || d.isBefore(dateStart) || d.isAfter(dateEnd)) continue;

                YearMonth key = YearMonth.from(d);
                if (t.getType() == CategoryType.INCOME && wantIncome && income.containsKey(key)) {
                    income.put(key, income.get(key).add(t.getAmount()));
                } else if (t.getType() == CategoryType.EXPENSE && wantExpense && expense.containsKey(key)) {
                    expense.put(key, expense.get(key).add(t.getAmount()));
                }
            }

            if (wantIncome) {
                chart.getDatasets().add(
                        LinearChartDataset.builder()
                                .label(CategoryType.INCOME.getLabel())
                                .color(Colors.success.name())
                                .data(income.values().stream().toList())
                                .build()
                );
            }
            if (wantExpense) {
                chart.getDatasets().add(
                        LinearChartDataset.builder()
                                .label(CategoryType.EXPENSE.getLabel())
                                .color(Colors.primary.name())
                                .data(expense.values().stream().toList())
                                .build()
                );
            }
        }
        return chart;
    }

    private List<LocalDate> dateRange(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        List<LocalDate> res = new ArrayList<>((int) days + 1);
        LocalDate d = start;
        while (!d.isAfter(end)) {
            res.add(d);
            d = d.plusDays(1);
        }
        return res;
    }

    private List<YearMonth> monthRange(YearMonth start, YearMonth end) {
        List<YearMonth> res = new ArrayList<>();
        YearMonth ym = start;
        while (!ym.isAfter(end)) {
            res.add(ym);
            ym = ym.plusMonths(1);
        }
        return res;
    }
}
