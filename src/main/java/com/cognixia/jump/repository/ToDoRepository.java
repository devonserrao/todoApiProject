package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.ToDo;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Integer>{

//	@Query("delete from to_do t where t.user_id = ?")
//	void deleteToDosOfUser(int userId);
	
// @Query("update to_do t set finished = 1 where t.user_id = ?;")
//	void finishAllToDosOfUser(int userId);
}
