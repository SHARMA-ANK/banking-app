package org.example.bankingapp.service;

import org.example.bankingapp.dto.*;
import org.example.bankingapp.entity.User;
import org.example.bankingapp.repository.UserRepository;
import org.example.bankingapp.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
            private EmailService emailService;
    AccountUtils accountUtils;
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /** Creating an account saving the new user in the db*/
        /* Check if user already exists */
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser= User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .phoneNumber(userRequest.getPhoneNumber())
                .email(userRequest.getEmail())
                .status("ACTIVE")
                .accountBalance(BigDecimal.ZERO) // Initial balance set to zero
                .accountNumber(AccountUtils.generateAccountNumber()) // Generate a unique account number
                .build();
        User savedUser = userRepository.save(newUser);
        // Send welcome email to the user
        EmailDetails emailDetails=EmailDetails.builder()
        .recipient(savedUser.getEmail())
        .subject("Welcome to Our Banking App")
        .messageBody("Dear " + savedUser.getFirstName() + ",\n\n" +
                "Thank you for creating an account with us. Your account number is " + savedUser.getAccountNumber() + ".\n" +
                "We are excited to have you on board!\n\n" +
                "Best regards,\n" +
                "The Banking App Team")
        .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceInquiry(EnquiryRequest enquiryRequest) {
        //check if the provided acct no exists
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser= userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_BALANCE_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_BALANCE_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists) {
            return AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE;
        }
        User foundUser= userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName()+" " + foundUser.getLastName() + " " + foundUser.getOtherName();
    }

    @Override
    public BankResponse debitAmount(CreditDebitRequest creditDebitRequest) {
        // Check if the account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // Find the user by account number
        User foundUser = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());

        // Check if the debit amount is valid
        if (creditDebitRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_FAILURE_CODE)
                    .responseMessage("Invalid debit amount")
                    .accountInfo(null)
                    .build();
        }

        // Check if the user has sufficient balance
        if (foundUser.getAccountBalance().compareTo(creditDebitRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_FAILURE_CODE)
                    .responseMessage("Insufficient balance")
                    .accountInfo(null)
                    .build();
        }

        // Deduct the amount from the user's account balance
        foundUser.setAccountBalance(foundUser.getAccountBalance().subtract(creditDebitRequest.getAmount()));
        userRepository.save(foundUser);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse creditAmount(CreditDebitRequest creditDebitRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        foundUser.setAccountBalance(foundUser.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(foundUser);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .build())
                .build();

    }
}
