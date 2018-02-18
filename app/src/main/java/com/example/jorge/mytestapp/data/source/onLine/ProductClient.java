package com.example.jorge.mytestapp.data.source.onLine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jorge on 14/02/2018.
 */

public class ProductClient {


    public static final String BASE_URL = "https://private-397433-myapiacerta1.apiary-mock.com";

    private static Retrofit retrofit = null;

    private static Gson gson = new GsonBuilder()
            .create();


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
