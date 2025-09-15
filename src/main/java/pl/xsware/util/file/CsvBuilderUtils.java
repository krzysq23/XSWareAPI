package pl.xsware.util.file;

import org.springframework.stereotype.Component;
import pl.xsware.config.properties.AppConstants;
import pl.xsware.domain.model.report.Report;
import pl.xsware.domain.model.transaction.Transaction;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Component
public class CsvBuilderUtils {

    public byte[] generateReportFileForTransactions(Report report, LocalDate dateStart, LocalDate dateEnd) {

        StringBuilder sb = new StringBuilder();

        sb.append("Raport finansowy dla zakresu: ").append(dateStart).append(" — ").append(dateEnd).append("\n\n");
        sb.append("DATA,KATEGORIA,TYP,KWOTA,OPIS\n");

        for (Transaction t : report.getTransactions()) {
            sb.append(csv(t.getDate() != null ? t.getDate().format(AppConstants.DATE_FMT) : ""))
                    .append(',').append(csv(t.getCategoryName()))
                    .append(',').append(csv(t.getType().name()))
                    .append(',').append(csv(t.getAmount() != null ? t.getAmount().toPlainString() : "0"))
                    .append(',').append(csv(t.getDescription()))
                    .append('\n');
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String csv(String s) {
        if (s == null) return "";
        String val = s.replace("\"", "\"\"");
        if (val.contains(",") || val.contains("\n") || val.contains("\"")) {
            return "\"" + val + "\"";
        }
        return val;
    }
}
