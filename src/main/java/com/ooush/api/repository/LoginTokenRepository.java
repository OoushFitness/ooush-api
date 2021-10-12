package com.ooush.api.repository;

import com.ooush.api.entity.LoginToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alex Green
 */
@Repository
public interface LoginTokenRepository extends CrudRepository<LoginToken, Integer> {

}
