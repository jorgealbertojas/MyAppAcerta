package com.example.jorge.mytestapp.data.source.onLine.login;

import android.content.res.Resources;
import android.widget.Toast;

import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.data.source.onLine.ProductClient;
import com.example.jorge.mytestapp.data.source.onLine.ProductEndpoint;
import com.example.jorge.mytestapp.data.source.onLine.ProductServiceApi;
import com.example.jorge.mytestapp.data.source.onLine.login.model.Login;
import com.example.jorge.mytestapp.data.source.onLine.model.ListProduct;
import com.example.jorge.mytestapp.data.source.onLine.model.Product;
import com.example.jorge.mytestapp.login.LoginContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge on 18/02/2018.
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
                    //showLogin(resultSearch.getLogin().toString());
                    //finish();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }



}
