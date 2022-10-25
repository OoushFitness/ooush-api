package com.ooush.api.dto.response;

import java.util.ArrayList;
import java.util.List;

public class BitmapSearchParameter {

    private String searchParameter;
    private List<BitmapSearchOption> searchOptions;

    public BitmapSearchParameter() {
        this.searchOptions = new ArrayList<>();
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

    public List<BitmapSearchOption> getSearchOptions() {
        return searchOptions;
    }

    public void setSearchOptions(List<BitmapSearchOption> searchOptions) {
        this.searchOptions = searchOptions;
    }
}
