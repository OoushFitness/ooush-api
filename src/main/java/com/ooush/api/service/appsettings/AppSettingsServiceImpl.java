package com.ooush.api.service.appsettings;

import com.ooush.api.repository.AppSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("appSettingService")
public class AppSettingsServiceImpl implements AppSettingsService {

	@Autowired
	private AppSettingsRepository appSettingsRepository;

	@Override
	public String constructAppBaseUrl() {
		String protocol = appSettingsRepository.getBySettingKey("protocol").getSettingValue();
		String fqdn = appSettingsRepository.getBySettingKey("fqdn").getSettingValue();
		String port = appSettingsRepository.getBySettingKey("api.port").getSettingValue();
		return protocol + "://" + fqdn + ":" + port;
	}

	@Override
	public String constructWebBaseUrl() {
		String protocol = appSettingsRepository.getBySettingKey("protocol").getSettingValue();
		String fqdn = appSettingsRepository.getBySettingKey("fqdn").getSettingValue();
		String port = appSettingsRepository.getBySettingKey("web.port").getSettingValue();
		return protocol + "://" + fqdn + ":" + port;
	}
}
