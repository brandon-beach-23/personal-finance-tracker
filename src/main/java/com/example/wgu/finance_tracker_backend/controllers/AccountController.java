package com.example.wgu.finance_tracker_backend.controllers;

import com.example.wgu.finance_tracker_backend.DTOs.AccountRequest;
import com.example.wgu.finance_tracker_backend.DTOs.AccountResponse;
import com.example.wgu.finance_tracker_backend.services.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long id, @RequestBody AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.updateAccount(accountRequest, id);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        Optional<AccountResponse> accountResponse = accountService.getAccountById(id);
        return accountResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccountsByUserId(@RequestParam("userId") Long userId) {
        List<AccountResponse> accountResponses = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accountResponses);
    }
}
