package com.example.jorge.mytestapp.purchaseDetail;

import com.example.jorge.mytestapp.BasePresenter;
import com.example.jorge.mytestapp.BaseView;
import com.example.jorge.mytestapp.data.source.onLine.model.Product;

/**
 * Created by jorge on 15/02/2018.
 * Contract Purchase Detail View and presenter
 */

public interface PurchaseDetailContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingPurchase();



        void showName(String name);

        void hideName();


        void showQuantity(String quantity);

        void hideQuantity();


        void showUrl(String url);

        void hideUrl();


        void showUser(String user);

        void hideUser();


        void showProductId(String productId);

        void hideProductId();






        void showCompletionStatus(boolean complete);

        void showEditPurchase(String shoppingId, Product product);

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

        void activatePurchase(String quantity);
    }
}
