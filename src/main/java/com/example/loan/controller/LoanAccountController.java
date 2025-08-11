package com.example.loan.controller;

import com.example.loan.dto.LoanAccountResponse;
import com.example.loan.service.LoanAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loanaccounts")
public class LoanAccountController {

    private static final Logger log = LoggerFactory.getLogger(LoanAccountController.class);

    private final LoanAccountService loanAccountService;

    public LoanAccountController(LoanAccountService loanAccountService) {
        this.loanAccountService = loanAccountService;
    }

    @GetMapping("/{loanAccountNumber}")
    public ResponseEntity<LoanAccountResponse> getLoanAccount(@PathVariable String loanAccountNumber) {
        log.info("Received request for loanAccountNumber={}", loanAccountNumber);
        LoanAccountResponse response = loanAccountService.getAndPersistLoanAccount(loanAccountNumber);
        return ResponseEntity.ok(response);
    }
}