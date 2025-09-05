package pl.xsware.domain.model.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardChart {

    private String categoryName;
    private String color;
    private double percent;
}
