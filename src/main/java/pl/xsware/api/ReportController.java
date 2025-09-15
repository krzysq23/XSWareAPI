package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.report.Report;
import pl.xsware.domain.model.report.ReportFile;
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

    @PostMapping("/generate/{format}")
    public ResponseEntity<byte[]> generate(@RequestBody @Valid ReportRequest data, @PathVariable String format) {
        ReportFile reportFile = reportService.generateFileReport(data, format);
        return ResponseEntity.ok()
                .contentType(reportFile.getMediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + reportFile.getFilename() + "\"")
                .cacheControl(CacheControl.noCache())
                .body(reportFile.getFile());
    }
}
