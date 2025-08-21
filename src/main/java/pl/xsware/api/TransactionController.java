package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.category.Category;
import pl.xsware.domain.model.transaction.Transaction;
import pl.xsware.domain.service.CategoryService;
import pl.xsware.domain.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Transaction>> getAll(@PathVariable Long userId) {
        List<Transaction> list = transactionService.getAllTransactions(userId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addTransaction(@RequestBody @Valid Transaction data) {
        transactionService.addTransaction(data);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/remove")
    public ResponseEntity<Response> removeTransaction(@RequestBody @Valid Transaction data) {
        transactionService.removeTransaction(data);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/edit")
    public ResponseEntity<Response> editTransaction(@RequestBody @Valid Transaction data) {
        transactionService.editTransaction(data);
        return ResponseEntity.ok(Response.create("OK"));
    }
}
