package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognixia.jump.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
