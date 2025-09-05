package pl.xsware.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.xsware.domain.model.dashboard.Dashboard;

@Slf4j
@Service
public class DashboardService
{
    public Dashboard getDashboardInfo(Long userId) {
        return Dashboard.builder().build();
    }
}
