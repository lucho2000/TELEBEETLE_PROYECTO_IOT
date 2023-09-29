package com.example.telebeetle.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {
    public List<Response> getList() {
        return list;
    }

    public void setList(List<Response> list) {
        this.list = list;
    }

    @SerializedName("features")
    private List<Response> list;
}
