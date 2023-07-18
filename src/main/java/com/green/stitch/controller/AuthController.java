package com.green.stitch.controller;

import java.util.ArrayList;
import java.util.List;
// import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.green.stitch.jwt.JwtUtility;
import com.green.stitch.model.Login;
import com.green.stitch.model.Signup;
import com.green.stitch.repository.UserRepository;
import com.green.stitch.request.LoginRequest;
import com.green.stitch.response.JSONResponse;
import com.green.stitch.service.UserDetailsServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/app")
public class AuthController {

	@Autowired
	private JwtUtility jwtTokenUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	UserRepository repository;

	@PostMapping("/signin")
	public ResponseEntity<?> validateUser(@RequestBody LoginRequest loginRequest) {

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginRequest.getUsername());

			String token = jwtTokenUtil.generateToken(authentication);
			List<String> roles = new ArrayList<>();
			for (GrantedAuthority authority : userDetails.getAuthorities()) {
				roles.add(String.valueOf(authority));
			}
			JSONResponse jsonResponse = new JSONResponse(token, userDetails.getUsername(), roles);

			return ResponseEntity.ok(jsonResponse);
		} catch (Exception authExc) {
			throw new RuntimeException(authExc.getMessage());
		}

	}

	@PostMapping("/signup")
	public ResponseEntity<?> signUpUser(@RequestBody Signup signupRequest) {
		String email = signupRequest.getEmail();
		String password = signupRequest.getPassword();

		// Extract username from email
		int atIndex = email.indexOf("@");
		String username = email.substring(0, atIndex);
		Login isExitUser = repository.findByUsername(username);

		if (isExitUser == null) {
			// Create a new User object
			Login user = new Login();
			user.setUsername(username);
			user.setPassword(password);
			// Save the user in the database
			repository.save(user);
			return ResponseEntity.ok("Signup successful. Username: " + username);
		} else {
			return ResponseEntity.ok("Username already exists " + username);
		}

	}

	@GetMapping("/validateToken/{authorization}")
	public boolean isValidToken(@PathVariable String authorization) {
		String token = authorization.substring(7);
		if (jwtTokenUtil.validateToken(token)) {
			return true;
		} else {
			return false;
		}
	}

}
