package pl.xsware.domain.model.chart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinearChat {

    private String label;
    private String color;
    private double data;
}
