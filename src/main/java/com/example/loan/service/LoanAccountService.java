package com.example.loan.service;

import com.example.loan.dto.EmiDetail;
import com.example.loan.dto.ExternalLoanAccountResponse;
import com.example.loan.dto.LoanAccountResponse;
import com.example.loan.integration.LoanAccountClient;
import com.example.loan.model.LoanAccount;
import com.example.loan.repository.LoanAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Service
public class LoanAccountService {

    private static final Logger log = LoggerFactory.getLogger(LoanAccountService.class);

    private final LoanAccountClient loanAccountClient;
    private final LoanAccountRepository loanAccountRepository;

    public LoanAccountService(LoanAccountClient loanAccountClient, LoanAccountRepository loanAccountRepository) {
        this.loanAccountClient = loanAccountClient;
        this.loanAccountRepository = loanAccountRepository;
    }

    @Transactional
    public LoanAccountResponse getAndPersistLoanAccount(String loanAccountNumber) {
        ExternalLoanAccountResponse external;
        try {
            external = loanAccountClient.fetchLoanAccount(loanAccountNumber);
        } catch (Exception ex) {
            log.error("Failed to fetch loan account {} from external API", loanAccountNumber, ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to fetch loan account from upstream API");
        }

        if (external == null || external.getLoanAccountNumber() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan account not found in upstream API");
        }

        LocalDate dueDate = null;
        BigDecimal emiAmount = null;
        if (external.getEmiDetails() != null) {
            for (EmiDetail detail : external.getEmiDetails()) {
                if (detail != null && detail.isDueStatus()) {
                    emiAmount = detail.getEmiAmount();
                    dueDate = parseDueDate(detail.getMonth());
                    break;
                }
            }
        }

        LoanAccount entity = new LoanAccount();
        entity.setLoanAccountNumber(external.getLoanAccountNumber());
        entity.setDueDate(dueDate);
        entity.setEmiAmount(emiAmount);

        loanAccountRepository.save(entity);

        return new LoanAccountResponse(entity.getLoanAccountNumber(), entity.getDueDate(), entity.getEmiAmount());
    }

    private LocalDate parseDueDate(String monthString) {
        if (monthString == null || monthString.isBlank()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
            // Parse to 1st day then set to 15th as a mid-month due date per requirement example
            LocalDate firstOfMonth = LocalDate.parse(monthString, formatter).withDayOfMonth(1);
            return firstOfMonth.withDayOfMonth(Math.min(15, firstOfMonth.lengthOfMonth()));
        } catch (DateTimeParseException e) {
            log.warn("Unable to parse month '{}' to LocalDate", monthString);
            return null;
        }
    }
}