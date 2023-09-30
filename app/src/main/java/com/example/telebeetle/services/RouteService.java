package com.example.telebeetle.services;

import com.example.telebeetle.dto.Route;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RouteService {
    @GET("/v2/directions/foot-walking")
    Call<Route> getRoute(@Query("api_key")String apiKey,
                         @Query(value = "start", encoded = true)String start,
                         @Query(value = "end", encoded = true)String end);
}
