package com.example.jorge.mytestapp.login;

import com.example.jorge.mytestapp.data.source.onLine.login.model.Login;

/**
 * Created by jorge on 18/02/2018.
 * Contract login View and Present
 */

public class LoginContract {

    interface View {

        void showLogin();
        void finish();

    }


    interface Presenter {

        void loadingLogin(Login login);


    }
}

