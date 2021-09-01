package com.cognixia.jump.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.username = ?1 and u.password = ?2")
	User loginUser(String username, String password);
	
	/*
	 * Want to use this Custom query for delete all todos of user but doesnt work
	 * */
//	@Query("delete from to_do t where t.user_id = ?")
//	void deleteToDosOfUser(int userId);
	
	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.username = :username WHERE id = :id")
	void updateUsername(@Param(value = "id") int id, @Param(value = "username") String username);
}
