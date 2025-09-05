package pl.xsware.domain.model.transaction;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TransactionRequest {

    private Long userId;
    private String dateRange;
    private LocalDate startDate;
    private LocalDate endDate;
}
