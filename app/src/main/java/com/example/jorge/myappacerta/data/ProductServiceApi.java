package com.example.jorge.myappacerta.data;

import com.example.jorge.myappacerta.data.model.ListProduct;
import com.example.jorge.myappacerta.data.model.Product;

/**
 * Created by jorge on 14/02/2018.
 */

public interface ProductServiceApi {

    interface ProductServiceCallback<T> {

        void onLoaded(ListProduct<Product> Product);
    }

    void getProducts(ProductServiceCallback<ListProduct<Product>> callback);

    void getProduct(String filmeId, ProductServiceCallback<Product> callback);
}
