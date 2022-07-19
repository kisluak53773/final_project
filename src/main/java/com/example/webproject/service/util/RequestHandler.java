package com.example.webproject.service.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.webproject.controller.RequestAttribute.*;
import static com.example.webproject.service.validator.impl.UserValidatorImpl.NOT_VALID;


public class RequestHandler {
    private static final RequestHandler instance=new RequestHandler();

    public static RequestHandler getInstance(){
        return instance;
    }

    public Map<String, List<String>> transform(Map<String, String[]> parameterMap) {
        Map<String, List<String>> requestParams = parameterMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> Arrays.stream(e.getValue()).collect(Collectors.toList())));
        return requestParams;
    }

    public Map<String, String> findUserData(Map<String, String[]> parameterMap) {
        String id = getSingle(parameterMap.get(ID));
        String email = getSingle(parameterMap.get(EMAIL));
        String phone = getSingle(parameterMap.get(PHONE));
        String password = getSingle(parameterMap.get(PASSWORD));
        String passwordRepeat = getSingle(parameterMap.get(PASSWORD_REPEAT));
        String role = getSingle(parameterMap.get(USER_ROLE));
        Map<String, String> credentials = new HashMap<>();
        credentials.put(ID, id);
        credentials.put(EMAIL, email);
        credentials.put(PHONE, phone);
        credentials.put(PASSWORD, password);
        credentials.put(PASSWORD_REPEAT, passwordRepeat);
        credentials.put(USER_ROLE, role);
        return credentials;
    }

    private String getSingle(String[] params) {
        if (params == null) {
            return NOT_VALID;
        }
        return Arrays.stream(params).findFirst().orElse(NOT_VALID);
    }
}
