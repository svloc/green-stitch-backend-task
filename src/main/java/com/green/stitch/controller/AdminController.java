package com.green.stitch.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
	// test kar ke dekho chal rah hai ky
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/")
	public String AdminTest(){
		return "Admin hai mai..";
	}
}

