package pl.xsware.domain.model.report;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ReportRequest {

    private LocalDate dateStart;
    private LocalDate dateEnd;
    private List<Long> categories;
    private String transactionType;
}
