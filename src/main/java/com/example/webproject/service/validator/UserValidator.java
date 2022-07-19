package com.example.webproject.service.validator;

import java.util.Map;

public interface UserValidator {
    boolean validatePassword(String password);
    boolean validatePhone(String phone);
    boolean validateEmail(String email);
    boolean validateRole(String role);
    Map<String, String> validateUser(Map<String, String> credentialsMap);
}
