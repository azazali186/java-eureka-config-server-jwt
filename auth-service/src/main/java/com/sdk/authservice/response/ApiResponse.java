package com.sdk.authservice.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ApiResponse<Object> {
    private int status;
    private String message;
    private Object data;

    public ApiResponse() {
    }

    public ApiResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int status) {
        this.status = status;
    }

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    
}

