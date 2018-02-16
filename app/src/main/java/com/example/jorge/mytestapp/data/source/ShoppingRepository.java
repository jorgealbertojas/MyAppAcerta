package com.example.jorge.mytestapp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jorge.mytestapp.data.Purchase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 15/02/2018.
 */

public class ShoppingRepository implements ShoppingDataSource{

    private static ShoppingRepository INSTANCE = null;

    private final ShoppingDataSource mShoppingRemoteDataSource;

    private final ShoppingDataSource mShoppingLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Purchase> mCachedShopping;

    boolean mCacheIsDirty = false;

    private ShoppingRepository(@NonNull ShoppingDataSource shoppingRemoteDataSource, @NonNull ShoppingDataSource shoppingLocalDataSource) {
        mShoppingRemoteDataSource = checkNotNull(shoppingRemoteDataSource);
        mShoppingLocalDataSource = checkNotNull(shoppingLocalDataSource);
    }

    public static ShoppingRepository getInstance(ShoppingDataSource ShoppingRemoteDataSource,
                                                 ShoppingDataSource ShoppingLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ShoppingRepository(ShoppingRemoteDataSource, ShoppingLocalDataSource);
        }
        return INSTANCE;
    }


    @Override
    public void getShopping(@NonNull final LoadShoppingCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedShopping != null && !mCacheIsDirty) {
            callback.onShoppingLoaded(new ArrayList<>(mCachedShopping.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getShoppingFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mShoppingLocalDataSource.getShopping(new LoadShoppingCallback() {

                @Override
                public void onShoppingLoaded(List<Purchase> purchaseList) {
                    refreshCache(purchaseList);
                    callback.onShoppingLoaded(new ArrayList<>(mCachedShopping.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getShoppingFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void getPurchase(@NonNull final String shoppingId, @NonNull final GetPurchaseCallback callback) {
        checkNotNull(shoppingId);
        checkNotNull(callback);

        Purchase cachedPurchase = getPurchaseWithId(shoppingId);

        // Respond immediately with cache if available
        if (cachedPurchase != null) {
            callback.onPurchaseLoaded(cachedPurchase);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mShoppingLocalDataSource.getPurchase(shoppingId, new GetPurchaseCallback() {
            @Override
            public void onPurchaseLoaded(Purchase purchase) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedShopping == null) {
                    mCachedShopping = new LinkedHashMap<>();
                }
                mCachedShopping.put(purchase.getId(), purchase);
                callback.onPurchaseLoaded(purchase);
            }

            @Override
            public void onDataNotAvailable() {
                mShoppingRemoteDataSource.getPurchase(shoppingId, new GetPurchaseCallback() {
                    @Override
                    public void onPurchaseLoaded(Purchase purchase) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedShopping == null) {
                            mCachedShopping = new LinkedHashMap<>();
                        }
                        mCachedShopping.put(purchase.getId(), purchase);
                        callback.onPurchaseLoaded(purchase);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
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

    private void getShoppingFromRemoteDataSource(@NonNull final LoadShoppingCallback callback) {
        mShoppingRemoteDataSource.getShopping(new LoadShoppingCallback() {
            @Override
            public void onShoppingLoaded(List<Purchase> purchaseList) {
                refreshCache(purchaseList);
                refreshLocalDataSource(purchaseList);
                callback.onShoppingLoaded(new ArrayList<>(mCachedShopping.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Purchase> purchaseList) {
        if (mCachedShopping == null) {
            mCachedShopping = new LinkedHashMap<>();
        }
        mCachedShopping.clear();
        for (Purchase purchase : purchaseList) {
            mCachedShopping.put(purchase.getId(), purchase);
        }
        mCacheIsDirty = false;
    }

    @Nullable
    private Purchase getPurchaseWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedShopping == null || mCachedShopping.isEmpty()) {
            return null;
        } else {
            return mCachedShopping.get(id);
        }
    }

    private void refreshLocalDataSource(List<Purchase> purchaseList) {
        mShoppingLocalDataSource.deleteAllShopping();
        for (Purchase purchase : purchaseList) {
            mShoppingLocalDataSource.savePurchase(purchase);
        }
    }
}
