package com.example.wgu.finance_tracker_backend.controllers;

import com.example.wgu.finance_tracker_backend.DTOs.AccountRequest;
import com.example.wgu.finance_tracker_backend.DTOs.AccountResponse;
import com.example.wgu.finance_tracker_backend.services.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest, Principal principal) {
        // Use principal.getName() to get the authenticated username
        String username = principal.getName();
        AccountResponse accountResponse = accountService.createAccount(accountRequest, username);
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long id, @RequestBody AccountRequest accountRequest, Principal principal) {
        // You'll need to modify the service layer to verify ownership via principal.getName()
        AccountResponse accountResponse = accountService.updateAccount(accountRequest, id, principal.getName());
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id, Principal principal) {
        // You'll need to modify the service layer to verify ownership via principal.getName()
        accountService.deleteAccount(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id, Principal principal) {
        // You'll need to modify the service layer to verify ownership via principal.getName()
        Optional<AccountResponse> accountResponse = accountService.getAccountById(id, principal.getName());
        return accountResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
//
//    @GetMapping
//    // FIX: Removed @RequestParam("userId") and replaced with Principal
//    public ResponseEntity<List<AccountResponse>> getAccountsByUserId(Principal principal) {
//        // The service layer must now accept the username (string) or look up the ID internally
//        if (principal == null) {
//            // Should be handled by SecurityConfig, but as a safeguard:
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//
//
//        List<AccountResponse> accountResponses = accountService.getAccountsByUsername(principal.getName());
//        return ResponseEntity.ok(accountResponses);
//    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccountsByUsername(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<AccountResponse> accounts = accountService.getAccountsByUsername(principal.getName());
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
}
