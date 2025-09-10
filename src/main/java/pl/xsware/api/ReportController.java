package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.xsware.domain.model.report.Report;
import pl.xsware.domain.model.report.ReportRequest;
import pl.xsware.domain.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/filter")
    public ResponseEntity<Report> filter(@RequestBody @Valid ReportRequest data) {
        Report report = reportService.filter(data);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/generate")
    public ResponseEntity<Report> generate(@RequestBody @Valid ReportRequest data) {
        Report report = reportService.generateReport(data);
        return ResponseEntity.ok(report);
    }
}
