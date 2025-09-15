package pl.xsware.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.xsware.config.auth.CurrentUserProvider;
import pl.xsware.domain.model.category.CategoryType;
import pl.xsware.domain.model.report.FileFormat;
import pl.xsware.domain.model.report.Report;
import pl.xsware.domain.model.report.ReportFile;
import pl.xsware.domain.model.report.ReportRequest;
import pl.xsware.domain.model.transaction.Transaction;
import pl.xsware.domain.model.transaction.TransactionRequest;
import pl.xsware.util.chart.ChartUtils;
import pl.xsware.util.data.TransactionUtils;
import pl.xsware.util.file.CsvBuilderUtils;
import pl.xsware.util.file.PdfBuilderUtils;

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
    @Autowired
    private PdfBuilderUtils pdfBuilderUtils;
    @Autowired
    private CsvBuilderUtils csvBuilderUtils;

    public Report filter(ReportRequest data) {
        Report report = getReportData(data);
        Map<String, List<Transaction>> groupedByCategory = transactionUtils.groupedByCategory(report.getTransactions(), CategoryType.EXPENSE);
        report.setPieChartData(chartUtils.generateChatDataForTransactions(groupedByCategory, report.getTotalIncome()));
        report.setLinearChartData(chartUtils.generateLinearChartDataForTransactions(report.getTransactions(), data.getDateStart(), data.getDateEnd(), data.getTransactionType()));
        return report;
    }

    public ReportFile generateFileReport(ReportRequest data, String format) {
        FileFormat fileFormat = FileFormat.fromFormat(format);
        String fileName = "raport." + fileFormat.getFormat();
        ReportFile reportFile = ReportFile.builder()
                .filename(fileName)
                .mediaType(fileFormat.getMediaType())
                .build();
        Report report = getReportData(data);
        switch (fileFormat) {
            case PFD -> reportFile.setFile(
                    pdfBuilderUtils.generateReportFileForTransactions(report, data.getDateStart(), data.getDateEnd()));
            case CSV -> reportFile.setFile(
                    csvBuilderUtils.generateReportFileForTransactions(report, data.getDateStart(), data.getDateEnd()));
            default ->  reportFile.setFile(new byte[0]);
        }
        return reportFile;
    }


    private Report getReportData(ReportRequest data) {
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
        return report;
    }
}
