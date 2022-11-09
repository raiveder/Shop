package com.example.employees;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitAPI {

    @POST("Shops/")
    Call<Mask> createPost(@Body Mask mask);

    @PUT("Shops/{id}")
    Call<Mask> updateData(@Query("id") int id, @Body Mask mask);

    @DELETE("Shops/{id}")
    Call<Mask> deleteData(@Query("id") int id);

}