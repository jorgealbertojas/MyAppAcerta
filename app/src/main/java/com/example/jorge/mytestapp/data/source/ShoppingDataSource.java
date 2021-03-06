package com.example.jorge.mytestapp.data.source;

import android.support.annotation.NonNull;

import com.example.jorge.mytestapp.data.Purchase;

import java.util.List;

/**
 * Created by jorge on 15/02/2018.
 * /**
 * Main entry point for accessing Purchase data.
 * For simplicity, only getShopping() and getPurchase() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new Purchase is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */

public interface ShoppingDataSource {

    interface LoadShoppingCallback {

        void onShoppingLoaded(List<Purchase> Purchase);

        void onDataNotAvailable();

     }

    interface FindShoppingCallback {

        void onFindLoaded(List<Purchase> Purchase);

        void onDataNotAvailable();
    }

    interface GetPurchaseCallback {

        void onPurchaseLoaded(Purchase purchase);

        void onDataNotAvailable();
    }

    void getFind(@NonNull FindShoppingCallback callback, String partName);

    void getShopping(@NonNull LoadShoppingCallback callback);

    void getPurchase(@NonNull String shoppingId , @NonNull GetPurchaseCallback callback);

    void savePurchase(@NonNull Purchase purchase);

    void activatePurchase(@NonNull String productI, String Quantity);

    void activatePurchase(@NonNull Purchase purchase, String Quantity);

    void refreshShopping();

    void deleteAllShopping();

    void deletePurchase(@NonNull String shoppingId);

    void completePurchase(@NonNull Purchase purchase, String user);

    void completePurchase(@NonNull String productId);


}

