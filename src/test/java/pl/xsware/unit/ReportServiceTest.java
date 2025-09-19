package pl.xsware.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xsware.domain.model.category.CategoryType;
import pl.xsware.domain.model.report.ReportFile;
import pl.xsware.domain.model.report.ReportRequest;
import pl.xsware.domain.model.transaction.Transaction;
import pl.xsware.domain.model.transaction.TransactionRequest;
import pl.xsware.domain.service.ReportService;
import pl.xsware.domain.service.TransactionService;
import pl.xsware.util.data.TransactionUtils;
import pl.xsware.util.file.CsvBuilderUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    TransactionService transactionService;
    @InjectMocks
    ReportService reportService;
    @Mock
    TransactionUtils transactionUtils;
    @Spy
    CsvBuilderUtils csvBuilderUtils;

    @Test
    void generatesCsvWithHeaderAndRows() {

        when(transactionService.getTransactions(any(TransactionRequest.class)))
                .thenReturn(List.of(
                        txn(1L, 100.50, LocalDate.parse("2025-09-01"), CategoryType.EXPENSE),
                        txn(2L, 50.00, LocalDate.parse("2025-09-02"), CategoryType.INCOME)
                ));

        when(transactionUtils.calculateTotalIncome(anyList())).thenReturn(BigDecimal.TEN);
        when(transactionUtils.calculateTotalExpense(anyList())).thenReturn(BigDecimal.ONE);

        ReportRequest req = ReportRequest.builder().dateStart(LocalDate.now()).dateEnd(LocalDate.now()).build();

        ReportFile csv = reportService.generateFileReport(req, "csv");

        String s = new String(csv.getFile(), StandardCharsets.UTF_8);
        assertThat(s).startsWith("Raport finansowy");
        assertThat(s).contains("2025-09-01", "100.5");
    }

    private Transaction txn(Long id, double amount, LocalDate date, CategoryType type) {
        return Transaction.builder()
                .id(id)
                .amount(BigDecimal.valueOf(amount))
                .date(date)
                .type(type)
                .build();
    }
}
