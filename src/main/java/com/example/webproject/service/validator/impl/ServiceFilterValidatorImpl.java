package com.example.webproject.service.validator.impl;

import com.example.webproject.service.validator.NumberValidator;
import com.example.webproject.service.validator.ServiceFilterValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.example.webproject.controller.RequestAttribute.*;

public class ServiceFilterValidatorImpl implements ServiceFilterValidator {
    private static final ServiceFilterValidatorImpl instance = new ServiceFilterValidatorImpl();
    private static final NumberValidator numberValidator = NumberValidatorImpl.getInstance();

    private ServiceFilterValidatorImpl() {
    }

    public static ServiceFilterValidatorImpl getInstance() {
        return instance;
    }


    @Override
    public boolean validateServiceName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validatePrice(String price) {
        if (price == null || price.isEmpty()) {
            return false;
        }
        return numberValidator.validateNumber(price);
    }

    @Override
    public boolean validateDescription(String description) {
        if(description==null || description.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public boolean validateImage(String image) {
        if(image ==null || image.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public Map<String, List<String>> validateParameterMap(Map<String, List<String>> parameterMap) {
        Map<String, List<String>> requestParams = new HashMap<>();
        List<String> serviceName=parameterMap.get(SERVICE_NAME);
        List<String> prices = parameterMap.get(PRICE);
        List<String> descriptions=parameterMap.get(DESCRIPTION);
        List<String> images=parameterMap.get(IMAGE);
        requestParams.put(SERVICE_NAME,validateParam(serviceName, this::validateServiceName));
        requestParams.put(PRICE, validateParam(prices, this::validatePrice));
        requestParams.put(DESCRIPTION,validateParam(descriptions, this::validateDescription));
        requestParams.put(IMAGE,validateParam(images, this::validateImage));
        return requestParams;
    }

    private List<String> validateParam(List<String> params, Predicate<String> validator) {
        if (params == null) {
            return new ArrayList<>();
        }
        return params.stream().filter(validator).collect(Collectors.toList());
    }
}
