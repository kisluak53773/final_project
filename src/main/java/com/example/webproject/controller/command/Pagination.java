package com.example.webproject.controller.command;

import com.example.webproject.dao.entity.BaseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Pagination <T extends BaseEntity>{
    private List<T> data;
    private boolean next;
    private boolean previous;
    private int currentPage;
    private Map<String, List<String>> requestAttributes;

    public Pagination() {
        requestAttributes = new HashMap<>();
    }

    public Pagination(List<T> data, boolean next, boolean previous, int currentPage) {
        this();
        this.data = data;
        this.next = next;
        this.previous = previous;
        this.currentPage = currentPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public boolean isPrevious() {
        return previous;
    }

    public void setPrevious(boolean previous) {
        this.previous = previous;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Map<String, List<String>> getRequestAttributes() {
        return requestAttributes;
    }public void setRequestAttributes(Map<String, List<String>> requestAttributes) {
        this.requestAttributes = requestAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagination<?> that = (Pagination<?>) o;
        return next == that.next && previous == that.previous && currentPage == that.currentPage && Objects.equals(data, that.data) && Objects.equals(requestAttributes, that.requestAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, next, previous, currentPage, requestAttributes);
    }
}
