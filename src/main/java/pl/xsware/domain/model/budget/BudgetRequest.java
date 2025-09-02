package pl.xsware.domain.model.budget;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BudgetRequest {

    private Long userId;
    private PeriodType periodType;
    private Long categoryId;
}
