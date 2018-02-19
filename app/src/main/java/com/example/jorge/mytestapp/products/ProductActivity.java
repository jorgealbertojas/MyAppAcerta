package com.example.jorge.mytestapp.products;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.util.Common;



public class ProductActivity extends AppCompatActivity {

    public static String SHARED_PREF_USER = "SHARED_PREF_USER";

    public static String SHARED_KEY_USER = "SHARED_KEY_USER";
    public static String SHARED_KEY_PASSWORD = "SHARED_KEY_PASSWORD";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(ProductFragment.newInstance());
            }
        }

    }

    private void initFragment(Fragment productFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_content, productFragment);
        transaction.commit();


    }

}
