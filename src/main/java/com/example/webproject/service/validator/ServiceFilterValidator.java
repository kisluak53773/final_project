package com.example.webproject.service.validator;

import java.util.List;
import java.util.Map;

public interface ServiceFilterValidator {

    boolean validateServiceName(String name);

    boolean validatePrice(String price);


    boolean validateDescription(String description);


    boolean validateImage(String image);


    Map<String, List<String>> validateParameterMap(Map<String, List<String>> parameterMap);
}
