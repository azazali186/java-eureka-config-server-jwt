package com.sdk.authservice.request;

import lombok.Data;

@Data
public class RegisterRequest {
    String username;
    String email;
    String password;
}
