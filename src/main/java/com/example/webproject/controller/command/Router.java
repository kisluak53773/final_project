package com.example.webproject.controller.command;

import java.util.Objects;

public class Router {
    private final DispatchType dispatchType;
    private final String targetPath;

    public Router(DispatchType routeType, String targetPath) {
        this.dispatchType = routeType;
        this.targetPath = targetPath;
    }

    public DispatchType getDispatchType() {
        return dispatchType;
    }

    public String getTargetPath() {
        return targetPath;
    }


    public enum DispatchType {

        FORWARD,
        REDIRECT
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Router router = (Router) o;
        return dispatchType == router.dispatchType && Objects.equals(targetPath, router.targetPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dispatchType, targetPath);
    }

    @Override
    public String toString() {
        return "Router{" +
                "dispatchType=" + dispatchType +
                ", targetPath='" + targetPath + '\'' +
                '}';
    }
}
