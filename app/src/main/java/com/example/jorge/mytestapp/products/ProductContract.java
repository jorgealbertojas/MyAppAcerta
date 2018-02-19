package com.example.jorge.mytestapp.products;

import android.support.annotation.NonNull;

import com.example.jorge.mytestapp.data.source.onLine.model.Product;

import java.util.List;

/**
 * Created by jorge on 14/02/2018.
 */

public interface ProductContract {

    interface View {

        void setLoading(boolean isAtivo);

        void showProduct(List<Product> productList);

        void showAllShopping();
    }

    interface UserActionsListener {

        void loadingProduct();

        void openDetail(@NonNull Product product);
    }
}
