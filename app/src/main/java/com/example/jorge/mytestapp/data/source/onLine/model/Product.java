package com.example.jorge.mytestapp.data.source.onLine.model;

/**
 * Created by jorge on 14/02/2018.
 * Model Product data the json
 */

import java.io.Serializable;

public class Product implements Serializable {

    private int product_id;
    private String name;
    private String url_image_small = "0";
    private String url_image_big  = "0";

    public int getId() {
        return product_id;
    }

    public void setId(int id) {
        this.product_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_image_small() {
        return url_image_small;
    }

    public void setUrl_image_small(String url_image_small) {
        this.url_image_small = url_image_small;
    }

    public String getUrl_image_big() {
        return url_image_big;
    }

    public void setUrl_image_big(String url_image_big) {
        this.url_image_big = url_image_big;
    }

}