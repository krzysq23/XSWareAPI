package pl.xsware;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.reactive.function.client.WebClient;
import pl.xsware.config.auth.CurrentUserProvider;
import pl.xsware.domain.service.*;
import pl.xsware.util.chart.ChartUtils;
import pl.xsware.util.cookie.CookieUtils;
import pl.xsware.util.data.TransactionUtils;
import pl.xsware.util.file.CsvBuilderUtils;
import pl.xsware.util.file.PdfBuilderUtils;
import pl.xsware.util.json.JsonValidator;
import pl.xsware.util.jwt.JwtUtils;

@TestConfiguration
public class TestMockConfig {

    /* ===== WebClient ===== */

    @Bean
    WebClient testWebClient() {
        return WebClient.builder().baseUrl("http://localhost").build();
    }

    /* ===== Services ===== */

    @Bean
    public AuthService authService() {
        return Mockito.mock(AuthService.class);
    }

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    public BudgetService budgetService() {
        return Mockito.mock(BudgetService.class);
    }

    @Bean
    public CategoryService categoryService() {
        return Mockito.mock(CategoryService.class);
    }

    @Bean
    public DashboardService dashboardService() {
        return Mockito.mock(DashboardService.class);
    }

    @Bean
    public FinancialGoalService financialGoalService() {
        return Mockito.mock(FinancialGoalService.class);
    }

    @Bean
    public NotificationService notificationService() {
        return Mockito.mock(NotificationService.class);
    }

    @Bean
    public ReportService reportService() {
        return Mockito.mock(ReportService.class);
    }

    @Bean
    public TransactionService transactionService() {
        return Mockito.mock(TransactionService.class);
    }

    /* ===== Utils ===== */

    @Bean
    public ChartUtils chartUtils() {
        return Mockito.mock(ChartUtils.class);
    }

    @Bean
    public CookieUtils cookieUtils() {
        return Mockito.mock(CookieUtils.class);
    }

    @Bean
    public TransactionUtils transactionUtils() {
        return Mockito.mock(TransactionUtils.class);
    }

    @Bean
    public CsvBuilderUtils csvBuilderUtils() {
        return Mockito.mock(CsvBuilderUtils.class);
    }

    @Bean
    public PdfBuilderUtils pdfBuilderUtils() {
        return Mockito.mock(PdfBuilderUtils.class);
    }

    @Bean
    public JsonValidator jsonValidator() {
        return Mockito.mock(JsonValidator.class);
    }

    @Bean
    public JwtUtils jwtUtils() {
        return Mockito.mock(JwtUtils.class);
    }

    /* ===== Components ===== */

    @Bean
    public CurrentUserProvider currentUserProvider() {
        return Mockito.mock(CurrentUserProvider.class);
    }

    @Bean(name = "testAuthenticationManager")
    @Primary
    public AuthenticationManager testAuthenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }

}
