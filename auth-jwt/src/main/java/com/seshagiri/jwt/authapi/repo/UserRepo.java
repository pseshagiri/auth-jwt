package com.seshagiri.jwt.authapi.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.seshagiri.jwt.authapi.entity.User;

public interface UserRepo extends CrudRepository<User, Integer> {
	
	public Optional<User> findByEmail(String email);

}
