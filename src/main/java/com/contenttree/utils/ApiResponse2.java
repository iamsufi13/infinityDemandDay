package com.contenttree.utils;

import lombok.Getter;
import java.util.List;
import java.util.Map;

public class ApiResponse2 {
    private boolean status;
    @Getter
    private String message;
    private List<Map<String, Object>> responseData;

    public ApiResponse2(boolean status, String message, List<Map<String, Object>> responseData) {
        this.status = status;
        this.message = message;
        this.responseData = responseData;
    }

    // Getters and setters
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Object>> getData() {
        return responseData;
    }

    public void setData(List<Map<String, Object>> responseData) {
        this.responseData = responseData;
    }
}
