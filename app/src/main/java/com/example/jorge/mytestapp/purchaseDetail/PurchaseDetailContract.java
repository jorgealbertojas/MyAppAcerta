package com.example.jorge.mytestapp.purchaseDetail;

import com.example.jorge.mytestapp.BasePresenter;
import com.example.jorge.mytestapp.BaseView;

/**
 * Created by jorge on 15/02/2018.
 */

public interface PurchaseDetailContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingPurchase();

        void hideQuantity();

        void showName(String name);

        void hideName();

        void showQuantity(String quantity);

        void showCompletionStatus(boolean complete);

        void showEditPurchase(String shoppingId);

        void showPurchaseDeleted();

        void showPurchaseMarkedComplete();

        void showPurchaseMarkedActive();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void editPurchase();

        void deletePurchase();

        void findPurchase();

        void completePurchase();

        void activatePurchase();
    }
}
