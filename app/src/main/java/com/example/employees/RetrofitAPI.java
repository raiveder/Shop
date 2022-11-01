package com.example.employees;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    // Передаём параметр как пользватели
    @POST("users")

    //Метод для публикации данных
    Call<DataModal> createPost(@Body DataModal dataModal);
}