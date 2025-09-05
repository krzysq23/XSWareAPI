package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.financialGoal.FinancialGoal;
import pl.xsware.domain.service.FinancialGoalService;

import java.util.List;

@RestController
@RequestMapping("/api/financialGoal")
public class FinancialGoalController {

    @Autowired
    private FinancialGoalService financialGoalService;

    @GetMapping("/all")
    public ResponseEntity<List<FinancialGoal>> getAll() {
        List<FinancialGoal> list = financialGoalService.getAllFinancialGoals();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addFinancialGoal(@RequestBody @Valid FinancialGoal data) {
        financialGoalService.addFinancialGoal(data);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/remove")
    public ResponseEntity<Response> removeFinancialGoal(@RequestBody @Valid FinancialGoal data) {
        financialGoalService.removeFinancialGoal(data);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/edit")
    public ResponseEntity<Response> editFinancialGoal(@RequestBody @Valid FinancialGoal data) {
        financialGoalService.editFinancialGoal(data);
        return ResponseEntity.ok(Response.create("OK"));
    }
}
