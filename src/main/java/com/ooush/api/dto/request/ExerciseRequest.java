package com.ooush.api.dto.request;

public class ExerciseRequest {
    private Integer searchBitmap;
    private String searchName;

    public Integer getSearchBitmap() {
        return searchBitmap;
    }

    public void setSearchBitmap(Integer searchBitmap) {
        this.searchBitmap = searchBitmap;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}
