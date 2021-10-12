package com.ooush.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ooush.api.entity.Users;


/**
 * @author Alex Green
 */
@Repository
public interface UserRespository extends CrudRepository<Users, Integer> {

	@Query("SELECT user "
			+ "FROM Users user "
			+ "WHERE user.userName = ?1 "
			+ "AND user.active = TRUE")
	Users findByUserName(String userName);

	@Query("SELECT user "
			+ "FROM Users user "
			+ "WHERE user.email = ?1 "
			+ "AND user.active = TRUE")
	Users findByEmail(String userName);

	@Query("SELECT user "
			+ "FROM Users user "
			+ "WHERE user.email = ?1 "
			+ "AND user.userStatus = 'PRE_VERIFIED'")
	Users findPreverifiedByEmail(String email);

	Users findByVerificationCode(String verificationCode);

}
