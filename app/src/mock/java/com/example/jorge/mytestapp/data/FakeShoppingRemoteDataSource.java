package com.example.jorge.mytestapp.data;

import android.support.annotation.NonNull;

import com.example.jorge.mytestapp.data.source.ShoppingDataSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jorge on 15/02/2018.
 */

public class FakeShoppingRemoteDataSource implements ShoppingDataSource {

    private static FakeShoppingRemoteDataSource INSTANCE;

    private static final Map<String, Purchase> TASKS_SERVICE_DATA = new LinkedHashMap<>();

    // Prevent direct instantiation.
    private FakeShoppingRemoteDataSource() {}

    public static FakeShoppingRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeShoppingRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getShopping(@NonNull LoadShoppingCallback callback) {

    }

    @Override
    public void getPurchase(@NonNull String shoppingId, @NonNull GetPurchaseCallback callback) {

    }



    @Override
    public void savePurchase(@NonNull Purchase purchase) {

    }

    @Override
    public void activatePurchase(@NonNull String productId) {

    }

    @Override
    public void activatePurchase(@NonNull Purchase purchase, String quantity) {

    }


    @Override
    public void refreshShopping() {

    }

    @Override
    public void deleteAllShopping() {

    }

    @Override
    public void deletePurchase(@NonNull String shoppingId) {

    }


    @Override
    public void completePurchase(@NonNull Purchase purchase) {

    }

    @Override
    public void completePurchase(@NonNull String productId) {

    }
}
