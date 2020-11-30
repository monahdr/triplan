package com.example.triplanproject.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionApi {
    @GET("/maps/api/directions/json")
    Call<DirectionResponse> getJson(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);
}
