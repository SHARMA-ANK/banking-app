package org.example.bankingapp.utils;

import java.time.Year;

public class AccountUtils {
    /** Current year + Random 6 digit number*/

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "Account already exists";
    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created successfully";
    public static final String ACCOUNT_NOT_FOUND_CODE = "003";
    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";
    public static final String ACCOUNT_BALANCE_SUCCESS_CODE = "004";
    public static final String ACCOUNT_BALANCE_SUCCESS_MESSAGE = "Account balance retrieved successfully";
    public static final String ACCOUNT_DEBIT_SUCCESS_CODE = "005";
    public static final String ACCOUNT_DEBIT_SUCCESS_MESSAGE = "Debit successful";
    public static final String ACCOUNT_CREDIT_SUCCESS_CODE = "006";
    public static final String ACCOUNT_CREDIT_SUCCESS_MESSAGE = "Credit successful";
    public static final String ACCOUNT_DEBIT_FAILURE_CODE = "007";
    public static final String ACCOUNT_DEBIT_FAILURE_MESSAGE = "Debit failed due to insufficient balance or invalid amount";
    public static final String ACCOUNT_CREDIT_FAILURE_CODE = "008";
    public static final String ACCOUNT_CREDIT_FAILURE_MESSAGE = "Credit failed due to invalid amount";

    public static String generateAccountNumber() {
        Year currentYear = Year.now();
        int min=100000;
        int max=999999;
        int randomNumber = (int)(Math.random() * (max - min + 1) + min);
        String year=String.valueOf(currentYear);
        String randNumber = String.valueOf(randomNumber);
        return year + randNumber;
    }
}
