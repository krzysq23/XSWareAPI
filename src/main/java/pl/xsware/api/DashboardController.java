package pl.xsware.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.xsware.config.auth.CurrentUserProvider;
import pl.xsware.domain.model.auth.CustomUserDetails;
import pl.xsware.domain.model.dashboard.Dashboard;
import pl.xsware.domain.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private CurrentUserProvider currentUserProvider;

    @GetMapping("/info")
    public ResponseEntity<Dashboard> getInfo(Authentication authentication) {
        System.out.println(currentUserProvider.getCurrentUser());
        Dashboard info = dashboardService.getDashboardInfo(currentUserProvider.getCurrentUserId());
        return ResponseEntity.ok(info);
    }
}
