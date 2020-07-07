package com.learnprogramming.photoapp.api.users.ui.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnprogramming.photoapp.api.users.data.UserEntity;
import com.learnprogramming.photoapp.api.users.shared.UserDto;
import com.learnprogramming.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.learnprogramming.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.learnprogramming.photoapp.api.users.ui.model.UserResponseModel;
import com.learnprogramming.photoapp.api.users.service.UsersServiceImpl;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private Environment env;

	@Autowired
	UsersServiceImpl usersServiceImpl;


	@GetMapping("/status/check")
	public String status() {
		return "Working on port " + env.getProperty("local.server.port") + env.getProperty("token.expiration_time");
	}


	@PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE }, produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {

        ModelMapper modelmapper = new ModelMapper();

		modelmapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


		UserDto userdtodetails = modelmapper.map(userDetails, UserDto.class);

		UserDto userdtoobject = usersServiceImpl.createUser(userdtodetails);

		CreateUserResponseModel  finaluserdtodetails = modelmapper.map(userdtoobject, CreateUserResponseModel .class);

		return ResponseEntity.status(HttpStatus.CREATED).body(finaluserdtodetails);
	}
	
	
	
	@GetMapping(value="/{userId}",produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE })
	//@PreAuthorize("principal == #userId")
	@PostAuthorize("principal == returnObject.getBody().getUserId()")
	public ResponseEntity<UserResponseModel> getuser(@PathVariable("userId") String userId){
		
		 ModelMapper modelmapper = new ModelMapper();
		
		   UserDto userdtoobject = usersServiceImpl.getUserByUserId(userId);
		   
		   UserResponseModel userresponse = modelmapper.map(userdtoobject,UserResponseModel.class);
		   
		   return ResponseEntity.status(HttpStatus.OK).body(userresponse);
		   
		   
		
		
	}


}
