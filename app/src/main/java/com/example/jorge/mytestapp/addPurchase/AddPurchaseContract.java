package com.example.jorge.mytestapp.addPurchase;

import com.example.jorge.mytestapp.BasePresenter;
import com.example.jorge.mytestapp.BaseView;

/**
 * Created by jorge on 15/02/2018.
 */

public interface AddPurchaseContract {

    interface View extends BaseView<Presenter> {

        void showEmptyPurchaseError();

        void showPurchaseList();

        void setQuantity(String quantity);



    }

    interface Presenter extends BasePresenter {

        void savePurchase(String productId, String user, String quantity, String name, String image);

        void populatePurchase();

        boolean isDataMissing();
    }
}