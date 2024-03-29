package com.cognixia.jump.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.ToDo;
import com.cognixia.jump.model.User;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Integer>{

//	@Transactional
//	@Modifying
//	@Query("DELETE FROM to_do t WHERE t.user.id = :userId")
//	void deleteToDosOfUser(@Param(value = "userId") int userId);
	
	@Transactional
	void removeByUser(User user);
	
// @Query("update to_do t set finished = 1 where t.user_id = ?;")
//	void finishAllToDosOfUser(int userId);
}
