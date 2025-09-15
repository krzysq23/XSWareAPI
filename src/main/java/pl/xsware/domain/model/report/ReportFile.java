package pl.xsware.domain.model.report;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;

@Data
@Builder
public class ReportFile {

    private String filename;
    private MediaType mediaType;
    private byte[] file;
}
