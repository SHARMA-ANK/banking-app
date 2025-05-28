package org.example.bankingapp.controller;

import org.example.bankingapp.dto.BankResponse;
import org.example.bankingapp.dto.CreditDebitRequest;
import org.example.bankingapp.dto.EnquiryRequest;
import org.example.bankingapp.dto.UserRequest;
import org.example.bankingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {

        return userService.createAccount(userRequest);
    }
    @GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceInquiry(enquiryRequest);
    }
    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }
    @GetMapping("debit")
    public BankResponse debitAmount(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.debitAmount(creditDebitRequest);
    }
    @GetMapping("credit")
    public BankResponse creditAmount(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.creditAmount(creditDebitRequest);
    }
}
