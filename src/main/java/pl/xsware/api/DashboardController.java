package pl.xsware.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.xsware.domain.model.dashboard.Dashboard;
import pl.xsware.domain.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/info")
    public ResponseEntity<Dashboard> getInfo() {
        Dashboard info = dashboardService.getDashboardInfo();
        return ResponseEntity.ok(info);
    }
}
