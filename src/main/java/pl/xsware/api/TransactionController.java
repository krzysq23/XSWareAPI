package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.transaction.Transaction;
import pl.xsware.domain.model.transaction.TransactionRequest;
import pl.xsware.domain.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/getByDate")
    public ResponseEntity<List<Transaction>> getAll(@RequestBody @Valid TransactionRequest data) {
        List<Transaction> list = transactionService.getTransactions(data);
        return ResponseEntity.ok(list);
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
