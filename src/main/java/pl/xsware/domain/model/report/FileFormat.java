package pl.xsware.domain.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum FileFormat {

    PFD("pdf", MediaType.APPLICATION_PDF),
    CSV("csv", MediaType.valueOf("text/csv")),
    XLSX("xlsx", MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

    private final String format;
    private final MediaType mediaType;

    public static FileFormat fromFormat(String fileFormat) {
        for (FileFormat f : values()) {
            if (Objects.equals(f.format, fileFormat)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Brak formatu pliku: " + fileFormat);
    }
}
