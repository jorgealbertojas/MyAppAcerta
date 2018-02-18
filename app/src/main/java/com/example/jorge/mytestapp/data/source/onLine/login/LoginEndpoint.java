package com.example.jorge.mytestapp.data.source.onLine.login;

import com.example.jorge.mytestapp.data.source.onLine.login.model.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jorge on 18/02/2018.
 */

public interface LoginEndpoint {

    @POST("/Product")
    Call<Login> createLogin(@Body Login login);
}
