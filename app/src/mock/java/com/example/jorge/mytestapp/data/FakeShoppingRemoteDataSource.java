package com.example.jorge.mytestapp.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.example.jorge.mytestapp.data.source.ShoppingDataSource;
import com.google.common.collect.Lists;

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
        callback.onShoppingLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
    }

    @Override
    public void getPurchase(@NonNull String shoppingId, @NonNull GetPurchaseCallback callback) {
        Purchase purchase = TASKS_SERVICE_DATA.get(shoppingId);
        callback.onPurchaseLoaded(purchase);
    }



    @Override
    public void savePurchase(@NonNull Purchase purchase) {
        TASKS_SERVICE_DATA.put(purchase.getId(), purchase);
    }

    @Override
    public void activatePurchase(@NonNull String productId) {

    }

    @Override
    public void activatePurchase(@NonNull Purchase purchase, String quantity) {
        Purchase activePurchase = new Purchase(purchase.getProductId(),purchase.getUser(),purchase.getNameProduct(), purchase.getQuantity(), purchase.getImage());
        TASKS_SERVICE_DATA.put(purchase.getId(), activePurchase);
    }


    @Override
    public void refreshShopping() {

    }

    @Override
    public void deleteAllShopping() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deletePurchase(@NonNull String shoppingId) {

    }

    @Override
    public void completePurchase(@NonNull Purchase purchase, String user) {
        Purchase completedTask = new Purchase(purchase.getProductId(),purchase.getUser(),purchase.getNameProduct(),purchase.getQuantity(),purchase.getImage());
        TASKS_SERVICE_DATA.put(purchase.getId(), completedTask);
    }


    @Override
    public void completePurchase(@NonNull String productId) {

    }

    @VisibleForTesting
    public void addShopping(Purchase... purchases) {
        for (Purchase purchase : purchases) {
            TASKS_SERVICE_DATA.put(purchase.getId(), purchase);
        }
    }
}
