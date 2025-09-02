package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.budget.BudgetLimit;
import pl.xsware.domain.model.budget.BudgetRequest;
import pl.xsware.domain.service.BudgetService;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/getBudgets")
    public ResponseEntity<List<BudgetLimit>> getBudgets(@RequestBody @Valid BudgetRequest data) {
        List<BudgetLimit> list = budgetService.getBudgetByPeriodType(data);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addBudget(@RequestBody @Valid BudgetLimit data) {
        budgetService.addBudget(data);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/remove")
    public ResponseEntity<Response> removeBudget(@RequestBody @Valid BudgetLimit data) {
        budgetService.removeBudget(data);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/edit")
    public ResponseEntity<Response> editBudget(@RequestBody @Valid BudgetLimit data) {
        budgetService.editBudget(data);
        return ResponseEntity.ok(Response.create("OK"));
    }
}
