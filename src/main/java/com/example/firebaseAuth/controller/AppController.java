package com.example.firebaseAuth.controller;


import com.example.firebaseAuth.entity.UserAuth;
import com.example.firebaseAuth.enums.Permission;
import com.example.firebaseAuth.service.UserManagementService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/app")
public class AppController {
    private final FirebaseAuth firebaseAuth;


    private final UserManagementService userManagementService;

    @Autowired
    public AppController(FirebaseAuth firebaseAuth, UserManagementService userManagementService) {
        this.firebaseAuth = firebaseAuth;
        this.userManagementService = userManagementService;
    }


//    @GetMapping(path = "/test")
//    public String test() {
//        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
//        return "Hello";
//    }


    @GetMapping(path = "/test")
    //@PreAuthorize("hasAuthority('READ')")
    public String test(@RequestHeader("Authorization") String authorizationHeader) throws FirebaseAuthException {
        String idToken = authorizationHeader.replace("Bearer ", "");
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            return "Hello, "+userRecord.getDisplayName()+"! ^_^";
        }
        catch (FirebaseAuthException e) {
            e.printStackTrace();
            return "Token spelled wrong or expired.";
        }
    }


//    @GetMapping(path = "/test")
//    @PreAuthorize("hasAuthority('READ')")
//    public String test(Principal principal) {
//        return principal.getName();
//    }


    @PostMapping(path = "/register")
    public String registerUser(@RequestBody UserAuth userAuth) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(userAuth.getEmail())
                    .setPassword(userAuth.getPassword())
                    .setDisplayName(userAuth.getDisplayName())
                    .setDisabled(false);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            userManagementService.setUserClaims(userRecord.getUid(), Collections.singletonList(Permission.READ));

            return "User registered successfully with UID: " + userRecord.getUid();
        } catch (FirebaseAuthException e) {
            return "User registration failed: " + e.getMessage();
        }
    }
}