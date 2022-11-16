package com.ooush.api.dto.response;

public class BitmapSearchOption {

    private String name;
    private Integer position;

    public BitmapSearchOption() {

    }

    public BitmapSearchOption(String name, Integer position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
