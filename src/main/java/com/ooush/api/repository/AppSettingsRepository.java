package com.ooush.api.repository;

import com.ooush.api.entity.AppSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppSettingsRepository extends CrudRepository<AppSettings, Integer> {

	AppSettings getBySettingKey(String settingKey);

}
