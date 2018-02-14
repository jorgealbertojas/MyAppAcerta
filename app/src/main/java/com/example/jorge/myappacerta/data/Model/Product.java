package com.example.jorge.myappacerta.data.model;

/**
 * Created by jorge on 14/02/2018.
 */

/**
 * Created by jorge on 14/02/2018.
 */

public class Product {

    private int id;
    private String name;
    private int rate;
    private String url_image_small = "0";
    private String url_image_big  = "0";
    private String price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}