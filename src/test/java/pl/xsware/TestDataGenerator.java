package pl.xsware;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import pl.xsware.domain.model.budget.BudgetLimit;
import pl.xsware.domain.model.budget.BudgetStatus;
import pl.xsware.domain.model.budget.PeriodType;
import pl.xsware.domain.model.category.CategoryType;
import pl.xsware.domain.model.transaction.Transaction;
import pl.xsware.domain.service.BudgetService;
import pl.xsware.domain.service.TransactionService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class TestDataGenerator {

    public static void main(String[] args) {


        List<Transaction> transactions =  List.of(
                // Grudzień 2025
                new Transaction(0L, 11L, 7L, "Wynagrodzenie", new BigDecimal("4500"), LocalDate.parse("2025-12-01"), "Wypłata", CategoryType.INCOME, Instant.now(), Instant.now()),
                new Transaction(0L, 11L, 4L, "Żywność", new BigDecimal("200"), LocalDate.parse("2025-12-03"), "Zakupy spożywcze", CategoryType.EXPENSE, Instant.now(), Instant.now()),
                new Transaction(0L, 11L, 3L, "Mieszkanie i opłaty stałe", new BigDecimal("120"), LocalDate.parse("2025-09-05"), "Telefon", CategoryType.EXPENSE, Instant.now(), Instant.now()),
                new Transaction(0L, 11L, 5L, "Transport", new BigDecimal("350"), LocalDate.parse("2025-12-10"), "Paliwo", CategoryType.EXPENSE, Instant.now(), Instant.now()),
                new Transaction(0L, 11L, 9L, "Inne przychody", new BigDecimal("500"), LocalDate.parse("2025-12-15"), "Zwrot podatku", CategoryType.INCOME, Instant.now(), Instant.now()),
                new Transaction(0L, 11L, 2L, "Wydatki", new BigDecimal("80"), LocalDate.parse("2025-12-20"), "Kawa na mieście", CategoryType.EXPENSE, Instant.now(), Instant.now())

                // Październik 2025
//                new Transaction(0L, 11L, 7L, "Wynagrodzenie", new BigDecimal("4500"), LocalDate.parse("2025-10-01"), "Wypłata", CategoryType.INCOME, Instant.now(), Instant.now()),
//                new Transaction(0L, 11L, 4L, "Żywność", new BigDecimal("220"), LocalDate.parse("2025-10-02"), "Zakupy spożywcze", CategoryType.EXPENSE, Instant.now(), Instant.now()),
//                new Transaction(0L, 11L, 3L, "Mieszkanie i opłaty stałe", new BigDecimal("450"), LocalDate.parse("2025-10-12"), "Rachunek za prąd", CategoryType.EXPENSE, Instant.now(), Instant.now()),
//                new Transaction(0L, 11L, 2L, "Wydatki", new BigDecimal("500"), LocalDate.parse("2025-10-18"), "Ubrania", CategoryType.EXPENSE, Instant.now(), Instant.now()),
//                new Transaction(0L, 11L, 9L, "Inne przychody", new BigDecimal("250"), LocalDate.parse("2025-10-25"), "Sprzedaż rzeczy używanych", CategoryType.INCOME, Instant.now(), Instant.now()),
//
//                // Listopad 2025
//                new Transaction(0L, 11L, 7L, "Wynagrodzenie", new BigDecimal("4500"), LocalDate.parse("2025-11-03"), "Wypłata", CategoryType.INCOME, Instant.now(), Instant.now()),
//                new Transaction(0L, 11L, 4L, "Żywność", new BigDecimal("300"), LocalDate.parse("2025-11-05"), "Zakupy spożywcze", CategoryType.EXPENSE, Instant.now(), Instant.now()),
//                new Transaction(0L, 11L, 3L, "Mieszkanie i opłaty stałe", new BigDecimal("60"), LocalDate.parse("2025-11-10"), "Streaming", CategoryType.EXPENSE, Instant.now(), Instant.now()),
//                new Transaction(0L, 11L, 5L, "Transport", new BigDecimal("180"), LocalDate.parse("2025-11-15"), "Paliwo", CategoryType.EXPENSE, Instant.now(), Instant.now()),
//                new Transaction(0L, 11L, 2L, "Wydatki", new BigDecimal("150"), LocalDate.parse("2025-11-20"), "Prezenty świąteczne", CategoryType.EXPENSE, Instant.now(), Instant.now()),
//                new Transaction(0L, 11L, 8L, "Działalność gospodarcza", new BigDecimal("700"), LocalDate.parse("2025-11-22"), "Premia", CategoryType.INCOME, Instant.now(), Instant.now())
        );
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        TransactionService transactionService = ctx.getBean(TransactionService.class);

        for (Transaction t : transactions) {
            System.out.println("Przetwarzam: " + t.getId() + " - " + t.getDescription());

            try {
                transactionService.addTransaction(t);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }


//        List<BudgetLimit> budgets = List.of(
//                // 1️⃣ Budżet miesięczny — Żywność — OK
//                new BudgetLimit(
//                        0L, 11L, 4L,
//                        new BigDecimal("800"),      // limit
//                        new BigDecimal("350"),      // spent
//                        new BigDecimal("450"),      // remaining
//                        PeriodType.MONTHLY,
//                        BudgetStatus.OK,
//                        44,
//                        "Oszczędności w tym miesiącu",
//                        LocalDate.of(2025, 11, 1),
//                        LocalDate.of(2025, 11, 30),
//                        Instant.now(),
//                        Instant.now()
//                ),
//
//                // 2️⃣ Budżet miesięczny — Transport — blisko limitu
//                new BudgetLimit(
//                        0L, 11L, 5L,
//                        new BigDecimal("300"),
//                        new BigDecimal("260"),
//                        new BigDecimal("40"),
//                        PeriodType.MONTHLY,
//                        BudgetStatus.NEAR_LIMIT,
//                        87,
//                        null,
//                        LocalDate.of(2025, 11, 1),
//                        LocalDate.of(2025, 11, 30),
//                        Instant.now(),
//                        Instant.now()
//                ),
//
//                // 3️⃣ Budżet miesięczny — Mieszkanie i opłaty stałe — wyczerpany
//                new BudgetLimit(
//                        0L, 11L, 3L,
//                        new BigDecimal("600"),
//                        new BigDecimal("620"),
//                        new BigDecimal("-20"),
//                        PeriodType.MONTHLY,
//                        BudgetStatus.EXCEEDED,
//                        100,
//                        "Prąd rachunek był większy",
//                        LocalDate.of(2025, 11, 1),
//                        LocalDate.of(2025, 11, 30),
//                        Instant.now(),
//                        Instant.now()
//                ),
//
//                // 4️⃣ Budżet roczny — Transport — w trakcie
//                new BudgetLimit(
//                        0L, 11L, 5L,
//                        new BigDecimal("3500"),
//                        new BigDecimal("1200"),
//                        new BigDecimal("2300"),
//                        PeriodType.YEARLY,
//                        BudgetStatus.OK,
//                        34,
//                        "Roczny budżet na paliwo",
//                        LocalDate.of(2025, 1, 1),
//                        LocalDate.of(2025, 12, 31),
//                        Instant.now(),
//                        Instant.now()
//                ),
//
//                // 5️⃣ Jednorazowy — remont pokoju
//                new BudgetLimit(
//                        0L, 11L, 3L,
//                        new BigDecimal("5000"),
//                        new BigDecimal("4800"),
//                        new BigDecimal("200"),
//                        PeriodType.ONE_TIME,
//                        BudgetStatus.NEAR_LIMIT,
//                        96,
//                        "Remont pokoju dziecięcego",
//                        LocalDate.of(2025, 9, 15),
//                        LocalDate.of(2025, 12, 15),
//                        Instant.now(),
//                        Instant.now()
//                )
//        );

//        BudgetService budgetService = ctx.getBean(BudgetService.class);
//
//
//        for (BudgetLimit item : budgets) {
//            System.out.println("Przetwarzam: " + item.getId() + " - " + item.getNote());
//
//            try {
//                budgetService.addBudget(item);
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                throw new RuntimeException(e);
//            }
//        }



    }
}
