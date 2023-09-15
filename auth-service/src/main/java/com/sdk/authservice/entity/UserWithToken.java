package com.sdk.authservice.entity;

import lombok.Data;

@Data
public class UserWithToken {
    private UserEntity user;
    private String token;

    public UserWithToken(UserEntity user, String token) {
        this.user = user;
        this.token = token;
    }
}
