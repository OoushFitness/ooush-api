package com.ooush.api.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "appsettings")
public class AppSettings implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SettingId")
	private int settingId;

	@Column(name = "SettingKey")
	private String settingKey;

	@Column(name = "SettingValue")
	private String settingValue;

	public AppSettings() {
		// Empty constructor for hibernate
	}

	public int getSettingId() {
		return settingId;
	}

	public void setSettingId(int settingId) {
		this.settingId = settingId;
	}

	public String getSettingKey() {
		return settingKey;
	}

	public void setSettingKey(String settingKey) {
		this.settingKey = settingKey;
	}

	public String getSettingValue() {
		return settingValue;
	}

	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}
}
