package pl.xsware.domain.model.transaction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionRequest {

    private Long userId;
    private String date;
}
