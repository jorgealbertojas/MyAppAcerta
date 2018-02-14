package com.example.jorge.myappacerta.Products;

import android.support.annotation.NonNull;

import com.example.jorge.myappacerta.data.model.Product;

import java.util.List;

/**
 * Created by jorge on 14/02/2018.
 */

public interface ProductContract {

    interface View {

        void setLoading(boolean isAtivo);

        void showProduct(List<Product> productList);

        void showDetailUI (String productId);
    }

    interface UserActionsListener {

        void loadingProduct();

        void openDetail(@NonNull Product product);
    }
}
