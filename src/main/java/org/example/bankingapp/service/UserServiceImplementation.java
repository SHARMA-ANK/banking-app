package org.example.bankingapp.service;

import org.example.bankingapp.dto.AccountInfo;
import org.example.bankingapp.dto.BankResponse;
import org.example.bankingapp.dto.EmailDetails;
import org.example.bankingapp.dto.UserRequest;
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
}
