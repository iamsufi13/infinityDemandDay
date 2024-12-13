package com.contenttree.utils;

import java.util.List;
import java.util.Map;

public class ResponseUtils {

        public static <T> ApiResponse1<T> createResponse1(T responseData, String message, boolean status) {
            return new ApiResponse1<>(status, message, responseData);
        }

        public static ApiResponse2 createResponse2(List<Map<String, Object>> responseData, String message, boolean status) {
            return new ApiResponse2(status, message, responseData);
        }
    public static <T> ApiResponseTemplate<T> createResponse( String status, String token, T data) {
        return new ApiResponseTemplate<>(status, token, data);
    }
    }



