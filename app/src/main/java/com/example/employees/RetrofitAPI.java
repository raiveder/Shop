package com.example.employees;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitAPI {

    @POST("users")
    Call<Mask> createPost(@Body Mask mask);

    @PUT("api/users/2")
    Call<Mask> updateData(@Body Mask mask);
}