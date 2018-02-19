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
 * Data Source for repository data
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

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getFind(@NonNull final FindShoppingCallback callback, final String partName) {
        checkNotNull(callback);

        if (true) {
            // Query the local storage if available. If not, query the network.
            mShoppingLocalDataSource.getFind(new FindShoppingCallback() {

                @Override
                public void onFindLoaded(List<Purchase> purchaseList) {
                    refreshCacheFind(purchaseList);
                    callback.onFindLoaded(new ArrayList<>(mCachedShopping.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getFindFromRemoteDataSource(callback,partName);
                }
            },partName);
        }
    }

    @Override
    public void getShopping(@NonNull final LoadShoppingCallback callback) {
        checkNotNull(callback);


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
        //}
    }

    @Override
    public void getPurchase(@NonNull final String shoppingId, @NonNull final GetPurchaseCallback callback) {
        checkNotNull(shoppingId);
        checkNotNull(callback);

        final Purchase cachedPurchase = getPurchaseWithId(shoppingId);

        mShoppingLocalDataSource.getPurchase(shoppingId, new GetPurchaseCallback() {
            @Override
            public void onPurchaseLoaded(Purchase purchase) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedShopping == null) {
                    mCachedShopping = new LinkedHashMap<>();
                }
                mCachedShopping.put(purchase.getId(), cachedPurchase);
                callback.onPurchaseLoaded(cachedPurchase);
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
        checkNotNull(purchase);
        mShoppingRemoteDataSource.savePurchase(purchase);
        mShoppingLocalDataSource.savePurchase(purchase);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedShopping == null) {
            mCachedShopping = new LinkedHashMap<>();
        }
        mCachedShopping.put(purchase.getId(), purchase);
    }

    @Override
    public void activatePurchase(@NonNull String productId, String quantity) {
        checkNotNull(productId);
        activatePurchase(getPurchaseWithId(productId),quantity);

        Purchase purchase =getPurchaseWithId(productId);
        mShoppingLocalDataSource.activatePurchase(purchase.getId(),quantity);
    }



    @Override
    public void activatePurchase(@NonNull Purchase purchase, String quantity) {
        checkNotNull(purchase);
        mShoppingRemoteDataSource.activatePurchase(purchase,quantity);
        mShoppingLocalDataSource.activatePurchase(purchase, quantity);

        Purchase activeTask = new Purchase(purchase.getProductId(), purchase.getUser(),purchase.getNameProduct(),quantity,purchase.getImage(), purchase.getId());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedShopping == null) {
            mCachedShopping = new LinkedHashMap<>();
        }
        mCachedShopping.put(purchase.getId(), activeTask);
    }


    @Override
    public void refreshShopping() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllShopping() {
        mShoppingRemoteDataSource.deleteAllShopping();
        mShoppingLocalDataSource.deleteAllShopping();

        if (mCachedShopping == null) {
            mCachedShopping = new LinkedHashMap<>();
        }
        mCachedShopping.clear();
    }

    @Override
    public void deletePurchase(@NonNull String shoppingId) {
        mShoppingRemoteDataSource.deletePurchase(checkNotNull(shoppingId));
        mShoppingLocalDataSource.deletePurchase(checkNotNull(shoppingId));
        mCachedShopping.remove(shoppingId);
    }


    @Override
    public void completePurchase(@NonNull Purchase purchase, String user) {
        checkNotNull(purchase);
        mShoppingRemoteDataSource.completePurchase(purchase, user);
        mShoppingLocalDataSource.completePurchase(purchase, user);

        Purchase completedPurchase = new Purchase(purchase.getProductId(), user, purchase.getNameProduct(),purchase.getQuantity(),purchase.getImage(), purchase.getId());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedShopping == null) {
            mCachedShopping = new LinkedHashMap<>();
        }
        mCachedShopping.put(purchase.getId(), completedPurchase);
    }

    @Override
    public void completePurchase(@NonNull String shoppingID) {
        checkNotNull(shoppingID);
        completePurchase(shoppingID);
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

    private void getFindFromRemoteDataSource(@NonNull final FindShoppingCallback callback, String partName) {
        mShoppingRemoteDataSource.getFind(new FindShoppingCallback() {

            @Override
            public void onFindLoaded(List<Purchase> purchaseList) {
                refreshCacheFind(purchaseList);
                refreshLocalDataSource(purchaseList);
                callback.onFindLoaded(new ArrayList<>(mCachedShopping.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        },partName);
    }





    private void refreshCacheFind(List<Purchase> purchaseList) {
        if (mCachedShopping == null) {
            mCachedShopping = new LinkedHashMap<>();
        }

        mCachedShopping.clear();
        for (Purchase purchase : purchaseList) {
            mCachedShopping.put(purchase.getId(), purchase);
        }
        mCacheIsDirty = false;
    }

    private void refreshCache(List<Purchase> purchaseList) {
        if (mCachedShopping == null) {
            mCachedShopping = new LinkedHashMap<>();
        }
        // For Test
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
      // For Test
        mShoppingLocalDataSource.deleteAllShopping();
        for (Purchase purchase : purchaseList) {
            mShoppingLocalDataSource.savePurchase(purchase);
        }
    }


}
