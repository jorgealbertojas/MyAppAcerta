package com.example.jorge.mytestapp.login;

import android.support.annotation.NonNull;

import com.example.jorge.mytestapp.data.source.onLine.login.model.Login;
import com.example.jorge.mytestapp.data.source.onLine.model.Product;

import java.util.List;

/**
 * Created by jorge on 18/02/2018.
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

