package com.ooush.api.repository;

import com.ooush.api.entity.LoginToken;
import com.ooush.api.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alex Green
 */
@Repository
public interface LoginTokenRepository extends CrudRepository<LoginToken, Integer> {

	LoginToken findLoginTokenByUsers (Users user);

	LoginToken findByToken(String token);

}
