package com.learnprogramming.photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.learnprogramming.photoapp.api.users.shared.UserDto;

public interface UsersService extends UserDetailsService{
	
	UserDto createUser(UserDto userdetails);
	UserDto getUserDetailsByEmail(String email );
	UserDto getUserByUserId(String userId);
	

}
