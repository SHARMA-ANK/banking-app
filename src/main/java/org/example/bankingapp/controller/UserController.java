package org.example.bankingapp.controller;

import org.example.bankingapp.dto.BankResponse;
import org.example.bankingapp.dto.UserRequest;
import org.example.bankingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {

        return userService.createAccount(userRequest);
    }
}
