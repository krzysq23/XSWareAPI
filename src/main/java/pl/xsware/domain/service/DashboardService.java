package pl.xsware.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.xsware.config.auth.CurrentUserProvider;
import pl.xsware.domain.model.dashboard.Dashboard;

@Slf4j
@Service
public class DashboardService
{
    @Autowired
    private CurrentUserProvider currentUserProvider;

    public Dashboard getDashboardInfo() {
        return Dashboard.builder().build();
    }
}
