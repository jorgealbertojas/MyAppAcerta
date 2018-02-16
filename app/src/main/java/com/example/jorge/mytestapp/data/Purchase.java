package com.example.jorge.mytestapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import java.util.UUID;


/**
 * Created by jorge on 15/02/2018.
 * Immutable model class for a Purchase.
 */

@Entity(tableName = "Shopping")
public final class Purchase {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String mId;


    @NonNull
    @ColumnInfo(name = "productid")
    private final String mProductId;

    @Nullable
    @ColumnInfo(name = "user")
    private final String mUser;

    @Nullable
    @ColumnInfo(name = "nameProduct")
    private final String mNameProduct;

    @Nullable
    @ColumnInfo(name = "quantity")
    private final String mQuantity;

    @Nullable
    @ColumnInfo(name = "image")
    private final String mImage;

    @Ignore
    public Purchase(@Nullable String productId, @Nullable String user, @Nullable String nameProduct, @Nullable String quantity, @Nullable String image) {
        this(productId, user,nameProduct, quantity, image, UUID.randomUUID().toString());
    }

    public Purchase(@Nullable String productId, @Nullable String user, @Nullable String nameProduct, @Nullable String quantity, @Nullable String image ,
                @NonNull String id) {
        mId = id;
        mProductId = productId;
        mUser = user;
        mNameProduct = nameProduct;
        mQuantity = quantity;
        mImage = image;
    }



    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getProductId() {
        return mProductId;
    }

    @Nullable
    public String getUser() {
        return mUser;
    }

    @Nullable
    public String getNameProduct() {
        return mNameProduct;
    }

    @Nullable
    public String getQuantity() {
        return mQuantity;
    }

    @Nullable
    public String getImage() {
        return mImage;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mId) ||
                Strings.isNullOrEmpty(mUser);
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mId)) {
            return mId;
        } else {
            return mNameProduct;
        }
    }





}
