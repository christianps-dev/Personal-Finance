package org.alunosufg.personalfinancespring.controller;

import jakarta.validation.Valid;
import org.alunosufg.personalfinancespring.dto.transactions.TransactionDTO;
import org.alunosufg.personalfinancespring.dto.transactions.UserTransactionDTO;
import org.alunosufg.personalfinancespring.services.TransactionServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/finance")
@CrossOrigin(origins = "http://localhost:4200")
public class FinanceController {

    private final TransactionServices transactionService;

    public FinanceController(TransactionServices transactionServices) {
        this.transactionService = transactionServices;
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> saveNewTransaction(@Valid @RequestBody UserTransactionDTO newTransaction) {
        String result = transactionService.saveTransaction(newTransaction);
        System.out.println("--- Saving new transaction");
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@RequestParam String email) {
        System.out.println("--- Getting all transactions");
        List<TransactionDTO> transactions = transactionService.getAllTransactions(email);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/last/{qtd}")
    public ResponseEntity<List<TransactionDTO>> lastTransactions(
            @RequestParam String email,
            @PathVariable Integer qtd) {

        if (email == null || email.isEmpty()) {
            System.out.println("--- Email is null");
            throw new RuntimeException("Email null");
        }

        List<TransactionDTO> transactions = transactionService.getLastQtdTransactions(email, qtd);
        System.out.println("--- Fetching last " + qtd + " transactions for: " + email);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/{month}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByMonth(
            @RequestParam String email,
            @PathVariable Integer month) {
        System.out.println("--- Getting all transactions by month");
        List<TransactionDTO> transactions = transactionService.getALlTransactionsByMonth(email, month);
        return ResponseEntity.ok(transactions);
    }
}
