package com.ooush.api.service.appsettings;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ooush.api.entity.AppSettings;
import com.ooush.api.repository.AppSettingsRepository;

class AppSettingsServiceImplTest {

    private final static String PROTOCOL_KEY = "protocol";
    private final static String FQDN_KEY = "fqdn";
    private final static String API_PORT_KEY = "api.port";
    private final static String WEB_PORT_KEY = "web.port";
    private final static String API_BASE_URL = "http://localhost:8080";
    private final static String WEB_BASE_URL = "http://localhost:3000";

    @Mock
    AppSettingsRepository mockAppSettingsRepository;

    @InjectMocks
    private AppSettingsService target = new AppSettingsServiceImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // GIVEN app running locally on developer machine
        AppSettings protocolSetting = new AppSettings();
        protocolSetting.setSettingValue("http");
        AppSettings fdqnSetting = new AppSettings();
        fdqnSetting.setSettingValue("localhost");

        when(mockAppSettingsRepository.getBySettingKey(PROTOCOL_KEY)).thenReturn(protocolSetting);
        when(mockAppSettingsRepository.getBySettingKey(FQDN_KEY)).thenReturn(fdqnSetting);
    }

    @DisplayName("shouldConstructAppBaseUrlFromDatabaseValues")
    @Test
    void shouldConstructAppBaseUrlFromDatabaseValues() {
        // WHEN constructing server url from database values
        AppSettings apiPortSetting = new AppSettings();
        apiPortSetting.setSettingValue("8080");
        when(mockAppSettingsRepository.getBySettingKey(API_PORT_KEY)).thenReturn(apiPortSetting);

        // THEN server url is properly constructed
        assertThat(target.constructAppBaseUrl(), Matchers.equalTo(API_BASE_URL));
    }

    @DisplayName("shouldConstructWebAppBaseUrlFromDatabaseValues")
    @Test
    void shouldConstructWebAppBaseUrlFromDatabaseValues() {
        // WHEN constructing web app url from database values
        AppSettings webPortSetting = new AppSettings();
        webPortSetting.setSettingValue("3000");
        when(mockAppSettingsRepository.getBySettingKey(WEB_PORT_KEY)).thenReturn(webPortSetting);

        // THEN web app url is properly constructed
        assertThat(target.constructWebBaseUrl(), Matchers.equalTo(WEB_BASE_URL));
    }
}