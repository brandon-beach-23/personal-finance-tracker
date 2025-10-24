package com.example.wgu.finance_tracker_backend.controllers;

import com.example.wgu.finance_tracker_backend.DTOs.SavingsGoalRequest;
import com.example.wgu.finance_tracker_backend.DTOs.SavingsGoalResponse;
import com.example.wgu.finance_tracker_backend.services.interfaces.SavingsGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/savings-goals")
public class SavingsGoalController {
    private final SavingsGoalService savingsGoalService;

    @Autowired
    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @PostMapping
    public ResponseEntity<SavingsGoalResponse> createSavingsGoal(@RequestBody SavingsGoalRequest savingsGoalRequest, Principal principal) {
        SavingsGoalResponse savingsGoalResponse = savingsGoalService.createSavingsGoal(savingsGoalRequest, principal);
        if (savingsGoalResponse == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(savingsGoalResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavingsGoalResponse> updateSavingsGoal(@RequestBody SavingsGoalRequest savingsGoalRequest, @PathVariable("id") Long id, Principal principal) {
        SavingsGoalResponse savingsGoalResponse = savingsGoalService.updateSavingsGoal(savingsGoalRequest, id, principal);
        return ResponseEntity.ok(savingsGoalResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavingsGoal(@PathVariable Long id, Principal principal) {
        savingsGoalService.deleteSavingsGoal(id, principal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoalResponse> getSavingsGoalById(@PathVariable("id")Long id){
        Optional<SavingsGoalResponse> savingsGoalResponse = savingsGoalService.getById(id);
        return savingsGoalResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/account/{accountId}")
    public ResponseEntity<SavingsGoalResponse> getSavingsGoalsBySavingsAccountId(@PathVariable("accountId")Long accountId) {
        System.out.println("GET savings goal for accountId: " + accountId);

        Optional<SavingsGoalResponse> savingsGoalResponse = savingsGoalService.getBySavingsAccountId(accountId);
        return savingsGoalResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
