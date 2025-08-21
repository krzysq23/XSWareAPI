package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.budget.BudgetLimit;
import pl.xsware.domain.service.BudgetService;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<BudgetLimit>> getAll(@PathVariable Long userId) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addBudget(@RequestBody @Valid BudgetLimit data) {
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/remove")
    public ResponseEntity<Response> removeBudget(@RequestBody @Valid BudgetLimit data) {
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/edit")
    public ResponseEntity<Response> editBudget(@RequestBody @Valid BudgetLimit data) {
        return ResponseEntity.ok(Response.create("OK"));
    }
}
