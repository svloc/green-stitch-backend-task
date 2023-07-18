package com.green.stitch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import com.green.stitch.model.Login;
import com.green.stitch.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Login userOp = userRepository.findByUsername(username);
		if (userOp == null) {
			throw new UsernameNotFoundException("User Not Found with username: " + username);
		}
		return getUser(userOp);
		
	}

	public UserDetails getUser(Login user) {
		return UserDetailsImpl.getUser(user);
	}

}
