package com.example.telebeetle.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @SerializedName("geometry")
    private Geometry geometry;
}
