package com.ooush.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ooush.api.entity.UserSetting;
import com.ooush.api.entity.Users;

@Repository
public interface UserSettingRepository extends CrudRepository<UserSetting, Integer> {

    UserSetting findByUser(Users user);

}
