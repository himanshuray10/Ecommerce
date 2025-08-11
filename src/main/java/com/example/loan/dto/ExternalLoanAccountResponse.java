package com.example.loan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalLoanAccountResponse {
    private String loanAccountNumber;
    private List<EmiDetail> emiDetails;

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public List<EmiDetail> getEmiDetails() {
        return emiDetails;
    }

    public void setEmiDetails(List<EmiDetail> emiDetails) {
        this.emiDetails = emiDetails;
    }
}