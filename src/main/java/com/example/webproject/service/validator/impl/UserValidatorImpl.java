package com.example.webproject.service.validator.impl;

import com.example.webproject.dao.entity.User;
import com.example.webproject.service.validator.UserValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.webproject.controller.RequestAttribute.*;
import static com.example.webproject.dao.ColumnName.PASSWORD;

public class UserValidatorImpl implements UserValidator {
    private static final UserValidatorImpl instance=new UserValidatorImpl();

    private static final String EMAIL_REGEX = "^[A-Za-z][._]{0,19}.+@[A-Za-z]+.*\\..*[A-Za-z]$";
    private static final String PHONE_REGEX = "^\\+?\\d{10,15}$";
    private static final String PASSWORD_REGEX = "^[^ ]{5,30}$";

    private static final int MAX_EMAIL_LENGTH = 100;

    public static final String NOT_VALID = "";

    private final Pattern emailPattern;
    private final Pattern phonePattern;
    private final Pattern passPattern;

    private UserValidatorImpl() {
        emailPattern = Pattern.compile(EMAIL_REGEX);
        phonePattern = Pattern.compile(PHONE_REGEX);
        passPattern = Pattern.compile(PASSWORD_REGEX);
    }

    public static UserValidatorImpl getInstance(){
        return instance;
    }

    @Override
    public boolean validatePassword(String password) {
        if (password != null && password.length() > 0) {
            Matcher matcher = passPattern.matcher(password);
            return matcher.matches();
        }
        return false;
    }

    @Override
    public boolean validatePhone(String phone) {
        if (phone != null) {
            if (phone.length() > 0) {
                Matcher matcher = phonePattern.matcher(phone);
                return matcher.matches();
            }
        }
        return false;
    }

    @Override
    public boolean validateEmail(String email) {
        if (email != null) {
            if (email.length() > 0 && email.length() <= MAX_EMAIL_LENGTH) {
                Matcher matcher = emailPattern.matcher(email);
                return matcher.matches();
            }
        }
        return false;
    }

    @Override
    public boolean validateRole(String role) {
        if (role == null) {
            return false;
        }
        try {
            User.UserRole.valueOf(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Map<String, String> validateUser(Map<String, String> credentialsMap) {
        Map<String, String> correctCredentials = new HashMap<>();
        String email = credentialsMap.get(EMAIL);
        String phone = credentialsMap.get(PHONE);
        correctCredentials.put(EMAIL, validateEmail(email) ? email : NOT_VALID);
        correctCredentials.put(PHONE, validatePhone(phone) ? phone : NOT_VALID);
        String password = credentialsMap.get(PASSWORD);
        String passwordRepeat = credentialsMap.get(PASSWORD_REPEAT);
        boolean check = password.equals(passwordRepeat);
        correctCredentials.put(PASSWORD, check ? password : NOT_VALID);
        correctCredentials.put(PASSWORD_REPEAT, check ? passwordRepeat : NOT_VALID);
        return correctCredentials;
    }
}
