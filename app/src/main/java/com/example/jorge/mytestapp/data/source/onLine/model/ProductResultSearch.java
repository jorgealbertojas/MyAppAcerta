package com.example.jorge.mytestapp.data.source.onLine.model;


import java.util.List;

/**
 * Created by jorge on 14/02/2018.
 */

public class ProductResultSearch {

    public List<Product> listProduct;

    public ProductResultSearch() {
    }

    public ProductResultSearch(List<Product> products) {
        this.listProduct = products;
    }
}
