package com.example.wgu.finance_tracker_backend.controllers;

import com.example.wgu.finance_tracker_backend.DTOs.AccountRequest;
import com.example.wgu.finance_tracker_backend.DTOs.AccountResponse;
import com.example.wgu.finance_tracker_backend.DTOs.TransactionRequest;
import com.example.wgu.finance_tracker_backend.DTOs.TransactionResponse;
import com.example.wgu.finance_tracker_backend.services.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest transactionRequest, Principal principal) {
        String username = principal.getName();
        TransactionResponse transactionResponse = transactionService.createTransaction(transactionRequest, username);
        return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest, Principal principal) {
        String username = principal.getName();
        TransactionResponse transactionResponse = transactionService.updateTransaction(id, transactionRequest, username);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        transactionService.deleteTransaction(id, username);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
//        Optional<TransactionResponse> transactionResponse = transactionService.getById(id);
//        return transactionResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactionByAccountId(@RequestParam("accountId") Long accountId, Principal principal) {
        String username = principal.getName();
        List<TransactionResponse> transactionResponses = transactionService.getTransactionsByAccountId(accountId, username);
        return ResponseEntity.ok(transactionResponses);
    }
}
