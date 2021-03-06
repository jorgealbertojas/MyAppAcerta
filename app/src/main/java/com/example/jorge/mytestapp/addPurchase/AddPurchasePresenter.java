package com.example.jorge.mytestapp.addPurchase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jorge.mytestapp.data.Purchase;
import com.example.jorge.mytestapp.data.source.ShoppingDataSource;

/**
 * Created by jorge on 15/02/2018.
 * Presenter for add Purchase
 */

public class AddPurchasePresenter implements AddPurchaseContract.Presenter, ShoppingDataSource.GetPurchaseCallback{

    @NonNull
    private final ShoppingDataSource mShoppingRepository;

    @NonNull
    private final AddPurchaseContract.View mAddPurchaseView;

    @Nullable
    private String mShoppingId;



    private boolean mIsDataMissing;

    public AddPurchasePresenter(@NonNull ShoppingDataSource shoppingRepository, @NonNull AddPurchaseContract.View addPurchaseView, @Nullable String shoppingId, boolean isDataMissing) {
        mShoppingRepository = shoppingRepository;
        mAddPurchaseView = addPurchaseView;
        mShoppingId = shoppingId;
        mIsDataMissing = isDataMissing;

        mAddPurchaseView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isNewPurchase() && mIsDataMissing) {
            populatePurchase();
        }
    }

    @Override
    public void savePurchase(String productId, String user, String quantity, String name, String image) {
        if (isNewPurchase()) {
            createPurchase(productId, user,quantity,name, image);
        } else {
            updatePurchase(productId, user,quantity,name, image);
        }
    }



    @Override
    public void populatePurchase() {
        if (isNewPurchase()) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        mShoppingRepository.getPurchase(mShoppingId, this);
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    private boolean isNewPurchase() {
        return mShoppingId == null;
    }



    @Override
    public void onPurchaseLoaded(Purchase purchase) {
        // The view may not be able to handle UI updates anymore
        if (mAddPurchaseView.isActive()) {
            mAddPurchaseView.setQuantity(purchase.getQuantity());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (mAddPurchaseView.isActive()) {
            mAddPurchaseView.showEmptyPurchaseError();
        }
    }


    private void createPurchase(String productId, String user, String quantity, String name, String image) {
        Purchase newPurchase  = new Purchase(productId, user,name,quantity,image);
        if (newPurchase.isEmpty()) {
            mAddPurchaseView.showEmptyPurchaseError();
        } else {
            mShoppingRepository.savePurchase(newPurchase);
            mAddPurchaseView.showPurchaseList();
        }
    }

    private void updatePurchase(String productId, String user, String quantity, String name, String image) {
        if (isNewPurchase()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        mShoppingRepository.activatePurchase(mShoppingId,quantity);
        mAddPurchaseView.showPurchaseList(); // After an edit, go back to the list.
    }

}
