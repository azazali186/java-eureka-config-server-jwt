package com.sdk.authservice.entity;

import java.util.List;

import lombok.Data;

@Data
public class UserWithToken {
    private UserEntity user;
    private String token;
    private List<PermissionEntity> permissions;

    public UserWithToken(UserEntity user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserWithToken(UserEntity user, String token, List<PermissionEntity> permissions) {
        this.user = user;
        this.token = token;        
        this.permissions = permissions;
    }
}
