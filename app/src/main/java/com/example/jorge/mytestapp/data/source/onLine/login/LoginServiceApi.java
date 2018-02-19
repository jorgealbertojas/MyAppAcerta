package com.example.jorge.mytestapp.data.source.onLine.login;

import com.example.jorge.mytestapp.data.source.onLine.login.model.Login;

/**
 * Created by jorge on 18/02/2018.
 * Interface the Api post Information Login
 */

public interface LoginServiceApi {

    interface LoginServiceCallback<T> {

        void onLoaded(Login User);
    }

    void postLogin(LoginServiceApi.LoginServiceCallback<Login> callback, Login login);

}
