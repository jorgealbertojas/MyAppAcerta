package com.example.jorge.mytestapp.shopping;

import android.support.annotation.NonNull;

import com.example.jorge.mytestapp.BasePresenter;
import com.example.jorge.mytestapp.BaseView;
import com.example.jorge.mytestapp.data.Purchase;
import com.example.jorge.mytestapp.data.source.remote.model.Product;

import java.util.List;

/**
 * Created by jorge on 15/02/2018.
 */

public interface ShoppingContract{

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showShopping(List<Purchase> listPurchase);

        void showFind(List<Purchase> listPurchase);

        void showAddPurchase();

        void showPurchaseDetailsUi(String shoppingId);

        void showPurchaseMarkedComplete();

        void showPurchaseMarkedActive();

        void showCompletedShoppingCleared();

        void showLoadingShoppingError();

        void showNoShopping();


        void showNoActiveShopping();

        void showNoCompletedShopping();

        void showSuccessfullySavedMessage();

        boolean isActive();



    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadShopping(boolean forceUpdate);

        void FindShopping(String partName);

        void addNewPurchase();

        void openPurchaseDetails(@NonNull Purchase requestedTask);

        void completePurchase(@NonNull Purchase completedTask);

        void activatePurchase(@NonNull Purchase activeTask);

        void clearCompletedShopping();


    }
}

