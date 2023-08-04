package com.example.firebaseAuth.controller;


import com.example.firebaseAuth.entity.UserAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/app")
public class AppController {

//    @GetMapping(path = "/test")
//    public String test(Principal principal) {
//        return principal.getName();
//    }



    @GetMapping(path = "/test")
    public String test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // The user is authenticated, get the displayName
            String displayName = authentication.getName();
            return "Hello, " + displayName;
        } else {
            return "Hello, Unauthenticated User";
        }
    }

    @PostMapping(path = "/register")
    public String registerUser(@RequestBody UserAuth userAuth) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(userAuth.getEmail())
                    .setPassword(userAuth.getPassword())
                    .setDisplayName(userAuth.getDisplayName())
                    .setDisabled(false);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

            return "User registered successfully with UID: " + userRecord.getUid();
        } catch (FirebaseAuthException e) {
            return "User registration failed: " + e.getMessage();
        }
    }
}