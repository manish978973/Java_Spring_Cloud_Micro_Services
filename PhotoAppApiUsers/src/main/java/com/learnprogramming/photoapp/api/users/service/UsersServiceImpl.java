package com.learnprogramming.photoapp.api.users.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.learnprogramming.photoapp.api.users.data.AlbumsServiceClient;
import com.learnprogramming.photoapp.api.users.data.UserEntity;
import com.learnprogramming.photoapp.api.users.data.UsersRepository;
import com.learnprogramming.photoapp.api.users.shared.UserDto;
import com.learnprogramming.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;

import org.slf4j.Logger;


@Service
public class UsersServiceImpl implements UsersService {
	
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	RestTemplate restTemplate;

	
	UsersRepository usersrepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	Environment environment;
	AlbumsServiceClient albumsServiceClient;
	
	@Autowired
	public UsersServiceImpl(UsersRepository usersrepository,BCryptPasswordEncoder bCryptPasswordEncoder,RestTemplate restTemplate,Environment environment,AlbumsServiceClient albumsServiceClient) {
	     this.usersrepository = usersrepository;	
	     this.bCryptPasswordEncoder=bCryptPasswordEncoder;
	     this.restTemplate = restTemplate;
	     this.environment = environment;
	     this.albumsServiceClient = albumsServiceClient;
	}
	
	
	@Override
	public UserDto createUser(UserDto userdetails) {
		// TODO Auto-generated method stub
		
		userdetails.setUserId(UUID.randomUUID().toString());
		
		ModelMapper modelmapper = new ModelMapper();
		
		modelmapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userentity = modelmapper.map(userdetails, UserEntity.class);
		userentity.setEncryptedPassword(bCryptPasswordEncoder.encode(userdetails.getPassword()));
		usersrepository.save(userentity);
		
		UserDto userentitydto = modelmapper.map(userentity, UserDto.class);
		return userentitydto;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserEntity userEntity =  usersrepository.findByEmail(username);
		
		if (userEntity == null) throw new UsernameNotFoundException(username);
		
		return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),true,true,true,true, new ArrayList<>());
	}


	@Override
	public UserDto getUserDetailsByEmail(String email) {
		// TODO Auto-generated method stub
		UserEntity userEntity =  usersrepository.findByEmail(email);
		
		if (userEntity == null) throw new UsernameNotFoundException(email);
		
		return new ModelMapper().map(userEntity, UserDto.class);
	}


	@Override
	public UserDto getUserByUserId(String userId) {
		
		ModelMapper modelmapper = new ModelMapper();
		// TODO Auto-generated method stub
		
		UserEntity userEntity =  usersrepository.findByUserId(userId);
		
		List<AlbumResponseModel> albumslist = null;
		
		
		if (userEntity == null) throw new UsernameNotFoundException(userId);
		
		
	      UserDto userdto = new ModelMapper().map(userEntity, UserDto.class);
	      
	      /*
	      
	    String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
	//    Map<String, String> uriParams = new HashMap<String, String>();

		ResponseEntity<List<AlbumResponseModel>> albumlistresponse =  restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {});
		 
			
		
		
				//(albumsUrl, HttpMethod.GET, null, String.class);
				
		// new ParameterizedTypeReference<List<AlbumResponseModel>>() {}
		
		List<AlbumResponseModel> albumslist = albumlistresponse.getBody();
		
		
	      
	      try {
	         albumslist = albumsServiceClient.getAlbums(userId);
	      }
	      
	      catch(FeignException e) {
	    	  logger.error(e.getLocalizedMessage());
	      }
	      */
	      
	      logger.info("Before calling get albums");
	      
	      albumslist = albumsServiceClient.getAlbums(userId);
	      logger.info("After calling get albums");
		userdto.setAlbums(albumslist);
		
		return userdto;
		
	}
	
	
	
	
	
	

}
