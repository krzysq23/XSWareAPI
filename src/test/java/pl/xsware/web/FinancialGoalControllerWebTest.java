package pl.xsware.web;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import pl.xsware.api.FinancialGoalController;

@WebMvcTest(controllers = FinancialGoalController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FinancialGoalControllerWebTest {
}
