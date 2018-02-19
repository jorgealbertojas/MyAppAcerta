package com.example.jorge.mytestapp.login;

import com.example.jorge.mytestapp.data.source.onLine.login.LoginServiceApi;
import com.example.jorge.mytestapp.data.source.onLine.login.model.Login;


/**
 * Created by jorge on 18/02/2018.
 * Presenter for support login
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginServiceApi mLoginServiceApi;
    private final LoginContract.View mLoginContractView;

    public LoginPresenter(LoginServiceApi mLoginServiceApi, LoginContract.View mLoginContract_View) {
        this.mLoginContractView = mLoginContract_View;
        this.mLoginServiceApi = mLoginServiceApi;
        }

    @Override
    public void loadingLogin(Login login) {

        mLoginServiceApi.postLogin(new LoginServiceApi.LoginServiceCallback<Login>(){
            @Override
            public void onLoaded(Login Login) {
                mLoginContractView.showLogin();
                mLoginContractView.finish();
            }
        },login);
    }
}

