package com.example.jorge.mytestapp.data.source.onLine;

import com.example.jorge.mytestapp.data.source.onLine.model.ListProduct;
import com.example.jorge.mytestapp.data.source.onLine.model.Product;

/**
 * Created by jorge on 14/02/2018.
 */

public interface ProductServiceApi {

    interface ProductServiceCallback<T> {

        void onLoaded(ListProduct<Product> Product);
    }

    void getProducts(ProductServiceCallback<ListProduct<Product>> callback);

    void getProduct(String productId, ProductServiceCallback<Product> callback);
}
