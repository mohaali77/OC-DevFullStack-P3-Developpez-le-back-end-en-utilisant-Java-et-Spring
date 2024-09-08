package com.app.chatop.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.chatop.model.UserModel;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Integer> {
	Optional<UserModel> findByEmail(String email);
}
