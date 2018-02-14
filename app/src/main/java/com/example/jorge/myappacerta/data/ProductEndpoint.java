package com.example.jorge.myappacerta.data;

import com.example.jorge.myappacerta.data.model.ListProduct;
import com.example.jorge.myappacerta.data.model.Product;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jorge on 14/02/2018.
 */

public interface ProductEndpoint {


    @GET("/Product")
    Call<ListProduct<Product>> getProduct() ;

}
