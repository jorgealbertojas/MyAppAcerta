package com.example.jorge.mytestapp.data.source.local;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.example.jorge.mytestapp.data.Purchase;
import com.example.jorge.mytestapp.data.source.ShoppingDataSource;
import com.example.jorge.mytestapp.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;




/**
 * Created by jorge on 15/02/2018.
 */

public class ShoppingLocalDataSource implements ShoppingDataSource {

        private static volatile ShoppingLocalDataSource INSTANCE;

        private ShoppingDao mShoppingDao;

        private AppExecutors mAppExecutors;

        // Prevent direct instantiation.
        private ShoppingLocalDataSource(@NonNull AppExecutors appExecutors,
                                     @NonNull ShoppingDao shoppingDao) {
                mAppExecutors = appExecutors;
                mShoppingDao = shoppingDao;
        }

        public static ShoppingLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                       @NonNull ShoppingDao shoppingDao) {
                if (INSTANCE == null) {
                        synchronized (ShoppingLocalDataSource.class) {
                                if (INSTANCE == null) {
                                        INSTANCE = new ShoppingLocalDataSource(appExecutors, shoppingDao);
                                }
                        }
                }
                return INSTANCE;
        }

        /**
         * This function is Fired if the database doesn't exist
         * or the table is empty.
         */
        @Override
        public void getShopping(@NonNull final LoadShoppingCallback callback) {
                Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                                final List<Purchase> shopping = mShoppingDao.getShopping();
                                mAppExecutors.mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                                if (shopping.isEmpty()) {
                                                        // This will be called if the table is new or just empty.
                                                        callback.onDataNotAvailable();
                                                } else {
                                                        callback.onShoppingLoaded(shopping);
                                                }
                                        }
                                });
                        }
                };

                mAppExecutors.diskIO().execute(runnable);
        }

        @Override
        public void getPurchase(@NonNull final String shoppingId, @NonNull final GetPurchaseCallback callback) {
                Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                                final Purchase purchase = mShoppingDao.getPurchaseById(shoppingId);

                                mAppExecutors.mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                                if (purchase != null) {
                                                        callback.onPurchaseLoaded(purchase);
                                                } else {
                                                        callback.onDataNotAvailable();
                                                }
                                        }
                                });
                        }
                };

                mAppExecutors.diskIO().execute(runnable);
        }

        @Override
        public void savePurchase(@NonNull final Purchase purchase) {
                checkNotNull(purchase);
                Runnable saveRunnable = new Runnable() {
                        @Override
                        public void run() {
                                mShoppingDao.insertPurchase(purchase);
                        }
                };
                mAppExecutors.diskIO().execute(saveRunnable);
        }

        @Override
        public void activatePurchase(@NonNull String productId) {
                // Not required for the local data source because the {@link TasksRepository} handles
                // converting from a {@code taskId} to a {@link task} using its cached data.
        }

        @Override
        public void activatePurchase(@NonNull final Purchase purchase,@NonNull final String quantity) {
                Runnable activateRunnable = new Runnable() {
                        @Override
                        public void run() {
                                mShoppingDao.updateQuantity(purchase.getId(), quantity);
                        }
                };
                mAppExecutors.diskIO().execute(activateRunnable);
        }


        @Override
        public void refreshShopping() {
                // Not required because the {@link ShoppingRepository} handles the logic of refreshing the
                // Shopping from all the available data sources.
        }

        @Override
        public void deleteAllShopping() {
                Runnable deleteRunnable = new Runnable() {
                        @Override
                        public void run() {
                                mShoppingDao.deleteShopping();
                        }
                };

                mAppExecutors.diskIO().execute(deleteRunnable);
        }

        @Override
        public void deletePurchase(@NonNull final String shoppingId) {
                Runnable deleteRunnable = new Runnable() {
                        @Override
                        public void run() {
                                mShoppingDao.deletePurchaseById(shoppingId);
                        }
                };

                mAppExecutors.diskIO().execute(deleteRunnable);
        }


        @Override
        public void completePurchase(@NonNull Purchase purchase) {

        }

        @Override
        public void completePurchase(@NonNull String productId) {

        }

        @VisibleForTesting
        static void clearInstance() {
                INSTANCE = null;
        }
}
