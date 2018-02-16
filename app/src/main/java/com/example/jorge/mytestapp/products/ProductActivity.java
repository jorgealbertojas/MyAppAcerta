package com.example.jorge.mytestapp.products;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jorge.mytestapp.R;


public class ProductActivity extends AppCompatActivity {

    public static String SHARED_PREF_USER = "SHARED_PREF_USER";
    public static String SHARED_KEY_USER = "SHARED_KEY_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        if (null == savedInstanceState) {
            initFragment(ProductFragment.newInstance());
        }

        salveSharedPreferences();


    }

    private void initFragment(Fragment productFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_content, productFragment);
        transaction.commit();


    }

    public void salveSharedPreferences(){
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_USER, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SHARED_KEY_USER, "bebeto");
        editor.commit();
    }
}
