package com.contenttree.utils;

public class ResponseUtils {

        public static <T> ApiResponse1<T> createResponse1(T responseData, String message, boolean status) {
            return new ApiResponse1<>(status, message, responseData);
        }


}
