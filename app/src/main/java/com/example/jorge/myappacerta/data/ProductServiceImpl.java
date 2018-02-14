package com.example.jorge.myappacerta.data;

import com.example.jorge.myappacerta.data.model.ListProduct;
import com.example.jorge.myappacerta.data.model.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge on 14/02/2018.
 */

public class ProductServiceImpl implements ProductServiceApi {
    ProductEndpoint mRetrofit;

    public ProductServiceImpl(){
        mRetrofit = ProductClient.getClient().create(ProductEndpoint.class);
    }






    @Override
    public void getProducts(ProductServiceCallback<ListProduct<Product>> callback) {
        Call<ListProduct<Product>> callProduct = mRetrofit.getProduct();
        callProduct.enqueue(new Callback<ListProduct<Product>>() {
            @Override
            public void onResponse(Call<ListProduct<Product>> call, Response<ListProduct<Product>> response) {
                if(response.code()==200){
                    ListProduct<Product> resultSearch = response.body();
                    callback.onLoaded(resultSearch);
                }
            }

            @Override
            public void onFailure(Call<ListProduct<Product>> call, Throwable t) {

            }
        });
    }

    @Override
    public void getProduct(String filmeId, ProductServiceCallback<Product> callback) {

    }
}
