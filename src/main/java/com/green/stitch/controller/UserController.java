package com.green.stitch.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    // test kar ke dekho chal rah hai ky
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/test")
    public String AdminTest() {
        return "User hai mai..";
    }
}