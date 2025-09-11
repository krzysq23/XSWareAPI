package pl.xsware.domain.model.chart;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class LinearChartDataset {

    private String label;
    private String color;
    private List<BigDecimal> data;
}
