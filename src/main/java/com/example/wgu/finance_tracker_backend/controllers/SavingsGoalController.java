package com.example.wgu.finance_tracker_backend.controllers;

import com.example.wgu.finance_tracker_backend.DTOs.SavingsGoalRequest;
import com.example.wgu.finance_tracker_backend.DTOs.SavingsGoalResponse;
import com.example.wgu.finance_tracker_backend.services.interfaces.SavingsGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/savingsgoals")
public class SavingsGoalController {
    private final SavingsGoalService savingsGoalService;

    @Autowired
    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @PostMapping
    public ResponseEntity<SavingsGoalResponse> createSavingsGoal(@RequestBody SavingsGoalRequest savingsGoalRequest) {
        SavingsGoalResponse savingsGoalResponse = savingsGoalService.createSavingsGoal(savingsGoalRequest);
        return new ResponseEntity<>(savingsGoalResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavingsGoalResponse> updateSavingsGoal(@RequestBody SavingsGoalRequest savingsGoalRequest, @PathVariable("id") Long id){
        SavingsGoalResponse savingsGoalResponse = savingsGoalService.updateSavingsGoal(savingsGoalRequest, id);
        return ResponseEntity.ok(savingsGoalResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavingsGoal(@PathVariable("id") Long id) {
        savingsGoalService.deleteSavingsGoal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoalResponse> getSavingsGoalById(@PathVariable("id")Long id){
        Optional<SavingsGoalResponse> savingsGoalResponse = savingsGoalService.getById(id);
        return savingsGoalResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<SavingsGoalResponse> getSavingsGoalsBySavingsAccountId(@RequestParam("savingsAccountId")Long savingsAccountId) {
        Optional<SavingsGoalResponse> savingsGoalResponse = savingsGoalService.getBySavingsAccountId(savingsAccountId);
        return savingsGoalResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
