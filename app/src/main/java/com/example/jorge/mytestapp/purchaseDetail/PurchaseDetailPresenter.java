package com.example.jorge.mytestapp.purchaseDetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jorge.mytestapp.data.Purchase;
import com.example.jorge.mytestapp.data.source.ShoppingDataSource;
import com.example.jorge.mytestapp.data.source.ShoppingRepository;
import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 15/02/2018.
 */

public class PurchaseDetailPresenter implements PurchaseDetailContract.Presenter {

    private final ShoppingRepository mShoppingRepository;

    private final PurchaseDetailContract.View mPurchaseDetailView;

    @Nullable
    private String mShoppingId;


    public PurchaseDetailPresenter(@Nullable ShoppingRepository shoppingRepository,
                                   @Nullable PurchaseDetailContract.View purchaseDetailView ,
                                   @Nullable String shoppingId) {
        mShoppingId = shoppingId;
        mShoppingRepository = checkNotNull(shoppingRepository,"tasksRepository cannot be null!");
        mPurchaseDetailView = checkNotNull(purchaseDetailView,"taskDetailView cannot be null!");

        mPurchaseDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openTask();

    }



    private void openTask() {
        if (Strings.isNullOrEmpty(mShoppingId)) {
            mPurchaseDetailView.showMissingPurchase();
            return;
        }

        mPurchaseDetailView.setLoadingIndicator(true);
        mShoppingRepository.getPurchase(mShoppingId, new ShoppingDataSource.GetPurchaseCallback() {

            @Override
            public void onPurchaseLoaded(Purchase purchase) {
                // The view may not be able to handle UI updates anymore
                if (!mPurchaseDetailView.isActive()) {
                    return;
                }
                mPurchaseDetailView.setLoadingIndicator(false);
                if (null == purchase) {
                    mPurchaseDetailView.showMissingPurchase();
                } else {
                    showPurchase(purchase);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mPurchaseDetailView.isActive()) {
                    return;
                }
                mPurchaseDetailView.showMissingPurchase();
            }
        });
    }


    @Override
    public void editPurchase() {
        if (Strings.isNullOrEmpty(mShoppingId)) {
            mPurchaseDetailView.showMissingPurchase();
            return;
        }
        mPurchaseDetailView.showEditPurchase(mShoppingId);
    }

    @Override
    public void deletePurchase() {
        if (Strings.isNullOrEmpty(mShoppingId)) {
            mPurchaseDetailView.showMissingPurchase();
            return;
        }
        mShoppingRepository.deletePurchase(mShoppingId);
        mPurchaseDetailView.showPurchaseDeleted();
    }

    @Override
    public void findPurchase() {

    }

    @Override
    public void completePurchase() {
        if (Strings.isNullOrEmpty(mShoppingId)) {
            mPurchaseDetailView.showMissingPurchase();
            return;
        }
        mShoppingRepository.completePurchase(mShoppingId);
        mPurchaseDetailView.showPurchaseMarkedComplete();
    }

    @Override
    public void activatePurchase() {
        if (Strings.isNullOrEmpty(mShoppingId)) {
            mPurchaseDetailView.showMissingPurchase();
            return;
        }
        mShoppingRepository.activatePurchase(mShoppingId);
        mPurchaseDetailView.showPurchaseMarkedActive();
    }

    private void showPurchase(@NonNull Purchase purchase) {
        String name = purchase.getNameProduct();
        String quantity = purchase.getQuantity();

        if (Strings.isNullOrEmpty(name)) {
            mPurchaseDetailView.hideName();
        } else {
            mPurchaseDetailView.showName(name);
        }

        if (Strings.isNullOrEmpty(quantity)) {
            mPurchaseDetailView.hideQuantity();
        } else {
            mPurchaseDetailView.showQuantity(quantity);
        }

    }
}
