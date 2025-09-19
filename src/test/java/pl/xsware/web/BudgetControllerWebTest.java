package pl.xsware.web;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import pl.xsware.api.BudgetController;

@WebMvcTest(controllers = BudgetController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BudgetControllerWebTest {
}
