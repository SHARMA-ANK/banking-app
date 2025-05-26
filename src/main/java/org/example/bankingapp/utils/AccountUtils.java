package org.example.bankingapp.utils;

import java.time.Year;

public class AccountUtils {
    /** Current year + Random 6 digit number*/

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "Account already exists";
    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created successfully";
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
