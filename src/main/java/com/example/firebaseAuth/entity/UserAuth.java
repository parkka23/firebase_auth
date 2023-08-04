package com.example.firebaseAuth.entity;

import lombok.Data;

@Data
public class UserAuth {
    private String email;
    private String password;
    private String displayName;
}