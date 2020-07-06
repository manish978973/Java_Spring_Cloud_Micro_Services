package com.learnprogramming.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public interface UsersRepository extends CrudRepository<UserEntity, Long> {
    
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
}
