package com.example.loan.integration;

import com.example.loan.dto.ExternalLoanAccountResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LoanAccountClient {

    private static final Logger log = LoggerFactory.getLogger(LoanAccountClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public LoanAccountClient(RestTemplate restTemplate,
                             @Value("${integration.loan.base-url:https://demo9993930.mockable.io}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ExternalLoanAccountResponse fetchLoanAccount(String loanAccountNumber) {
        String url = String.format("%s/loanaccount/%s", baseUrl, loanAccountNumber);
        log.info("Calling external API: {}", url);
        return restTemplate.getForObject(url, ExternalLoanAccountResponse.class);
    }
}