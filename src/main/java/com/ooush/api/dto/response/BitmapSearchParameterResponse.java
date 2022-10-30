package com.ooush.api.dto.response;

import java.util.List;

public class BitmapSearchParameterResponse {

    private List<BitmapSearchParameter> parameterList;
    private Integer bitmapPositionCount;

    public List<BitmapSearchParameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<BitmapSearchParameter> parameterList) {
        this.parameterList = parameterList;
    }

    public Integer getBitmapPositionCount() {
        return bitmapPositionCount;
    }

    public void setBitmapPositionCount(Integer bitmapPositionCount) {
        this.bitmapPositionCount = bitmapPositionCount;
    }
}
