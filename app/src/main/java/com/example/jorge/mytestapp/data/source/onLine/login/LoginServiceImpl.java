package com.example.jorge.mytestapp.data.source.onLine.login;

import com.example.jorge.mytestapp.data.source.onLine.login.model.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge on 18/02/2018.
 * Service for support Login
 */

public class LoginServiceImpl implements LoginServiceApi {

    LoginEndpoint mRetrofit;

    public LoginServiceImpl(){
        mRetrofit = LoginClient.getClient().create(LoginEndpoint.class);
    }

    @Override
    public void postLogin(final LoginServiceCallback<Login> callback, Login login) {
        Call<Login> callLogin = mRetrofit.createLogin(login);
        callLogin.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.code()==201){
                    Login resultSearch = response.body();
                    callback.onLoaded(resultSearch);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }



}
