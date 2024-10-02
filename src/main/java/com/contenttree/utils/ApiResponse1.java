package com.contenttree.utils;

public class ApiResponse1<T> {
    private boolean status;
    private String message;
    private T responseData;

    public ApiResponse1(boolean status, String message, T responseData) {
        this.status = status;
        this.message = message;
        this.responseData = responseData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return responseData;
    }

    public void setData(T responseData) {
        this.responseData = responseData;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
