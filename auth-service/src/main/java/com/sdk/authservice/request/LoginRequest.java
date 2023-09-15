package com.sdk.authservice.request;

import lombok.Data;

@Data
public class LoginRequest {
    String email;
    String password;
}
