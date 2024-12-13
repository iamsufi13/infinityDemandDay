package com.contenttree.utils;

public class ApiResponseTemplate<T> {
    private String status;
    private String token;
    private T data;

    public ApiResponseTemplate(String status, String token, T data) {
        this.status = status;
        this.token = token;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
