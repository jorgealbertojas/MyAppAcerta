package com.example.jorge.mytestapp.products;

import android.support.annotation.NonNull;

import com.example.jorge.mytestapp.data.source.onLine.ProductServiceApi;
import com.example.jorge.mytestapp.data.source.onLine.model.ListProduct;
import com.example.jorge.mytestapp.data.source.onLine.model.Product;

/**
 * Created by jorge on 14/02/2018.
 */

public class ProductPresenter implements ProductContract.UserActionsListener {

    private final ProductServiceApi mProductServiceApi;
    private final ProductContract.View mProductContractView;

    public ProductPresenter(ProductServiceApi mProductServiceApi, ProductContract.View mProductContract_View) {
        this.mProductContractView = mProductContract_View;
        this.mProductServiceApi = mProductServiceApi;
    }

    @Override
    public void loadingProduct() {
        mProductContractView.setLoading(true);
        mProductServiceApi.getProducts(new ProductServiceApi.ProductServiceCallback<ListProduct<Product>>(){
            @Override
            public void onLoaded(ListProduct listProduct) {
                mProductContractView.setLoading(false);
                mProductContractView.showProduct(listProduct.items);
            }
        });

    }

    @Override
    public void openDetail(@NonNull Product product) {

    }
}
