package pl.xsware.util.file;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import pl.xsware.domain.model.report.Report;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Component
public class PdfBuilderUtils {

    public byte[] generateReportFileForTransactions(Report report, LocalDate dateStart, LocalDate dateEnd) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font h1 = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font p  = new Font(Font.HELVETICA, 11, Font.NORMAL);

            doc.add(new Paragraph("Raport finansowy", h1));
            doc.add(new Paragraph("Zakres: " + dateStart + " — " + dateEnd, p));
            doc.add(new Paragraph("Wygenerowano: " + LocalDate.now(), p));
            doc.add(Chunk.NEWLINE);

            Table table = new Table(4);
            table.setWidth(100);
            table.setPadding(4);
            table.addCell("Kategoria");
            table.addCell("Opis");
            table.addCell("Typ");
            table.addCell("Kwota");

            report.getTransactions().forEach(t -> {
                table.addCell(t.getCategoryName());
                table.addCell(t.getDescription());
                table.addCell(t.getType().getLabel());
                table.addCell(t.getAmount() + " zł");
            });

            doc.add(table);
        } catch (Exception e) {
            throw new IllegalStateException("Błąd generowania PDF", e);
        } finally {
            doc.close();
        }
        return baos.toByteArray();
    }
}
