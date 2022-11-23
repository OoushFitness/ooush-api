package com.ooush.api.dto.response;

import com.ooush.api.entity.UserSetting;

public class UserSettingsResponse {

    private String weightDenomination;

    public UserSettingsResponse(UserSetting userSetting) {
        this.weightDenomination = userSetting.getWeightDenomination().getLabel();
    }

    public String getWeightDenomination() {
        return weightDenomination;
    }

    public void setWeightDenomination(String weightDenomination) {
        this.weightDenomination = weightDenomination;
    }
}
