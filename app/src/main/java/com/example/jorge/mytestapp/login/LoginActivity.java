package com.example.jorge.mytestapp.login;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jorge.mytestapp.R;

import static com.example.jorge.mytestapp.products.ProductActivity.SHARED_KEY_USER;
import static com.example.jorge.mytestapp.products.ProductActivity.SHARED_PREF_USER;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (null == savedInstanceState) {
            initFragment(LoginFragment.newInstance());
        }
    }

    private void initFragment(Fragment productFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_frameContainer, productFragment);
        transaction.commit();

    }


}
