package org.example.bankingapp.service;

import org.example.bankingapp.dto.BankResponse;
import org.example.bankingapp.dto.CreditDebitRequest;
import org.example.bankingapp.dto.EnquiryRequest;
import org.example.bankingapp.dto.UserRequest;
import org.springframework.stereotype.Service;


public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceInquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse debitAmount(CreditDebitRequest creditDebitRequest);
    BankResponse creditAmount (CreditDebitRequest creditDebitRequest);
}
