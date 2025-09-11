package pl.xsware.domain.model.chart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LinearChart {

    private List<String> labels = new ArrayList<>();;
    private List<LinearChartDataset> datasets = new ArrayList<>();;
}
