package pl.xsware.domain.model.chart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PieChart {

    private String label;
    private String color;
    private double percent;
}
