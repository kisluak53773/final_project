package com.example.webproject.service.util;

import com.example.webproject.controller.command.Pagination;
import com.example.webproject.dao.entity.BaseEntity;
import com.example.webproject.service.validator.NumberValidator;
import com.example.webproject.service.validator.impl.NumberValidatorImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaginationHelper {
    private static final PaginationHelper instance = new PaginationHelper();

    private static final int DEFAULT_PAGE = 0;

    private PaginationHelper() {
    }

    public static PaginationHelper getInstance() {
        return instance;
    }

    public <T extends BaseEntity> Pagination<T> getPage(List<T> elements, int page, int elementsOnPage) {
        int from = page * elementsOnPage;
        int to = from + elementsOnPage - 1;
        int elementsSize = elements.size();

        List<T> pageElements;
        if (elementsSize > from) {
            if (elementsSize > to) {
                pageElements = elements.subList(from, to);
                pageElements.add(elements.get(to));
            } else {
                pageElements = elements.subList(from, elementsSize - 1);
                pageElements.add(elements.get(elementsSize - 1));
            }
        } else {
            pageElements = new ArrayList<>();
        }
        boolean previous = page > 0;
        boolean next = elementsSize > (to + 1);
        return new Pagination<>(pageElements, next, previous, page);
    }

    public int findPage(List<String> pages) {
        if (pages == null) {
            return DEFAULT_PAGE;
        }
        NumberValidator validator = NumberValidatorImpl.getInstance();
        Optional<Integer> validPage = pages.stream()
                .filter(validator::validateNumber)
                .map(Integer::parseInt)
                .findFirst();
        return validPage.orElse(DEFAULT_PAGE);
    }
}
