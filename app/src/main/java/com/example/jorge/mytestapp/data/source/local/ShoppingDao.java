package com.example.jorge.mytestapp.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jorge.mytestapp.data.Purchase;

import java.util.List;

/**
 * Created by jorge on 15/02/2018.
 * Data Access Object for the Shopping table.
 */
@Dao
public interface ShoppingDao {

    /**
     * Select all Purchase from the Purchase table.
     */
    @Query("SELECT * FROM Shopping")
    List<Purchase> getShopping();

    /**
     * Select a Purchase by id.
     */
    @Query("SELECT * FROM Shopping WHERE entryid = :shoppingId ")
    Purchase getPurchaseById(String shoppingId);


    @Query("SELECT * FROM Shopping WHERE nameProduct like :partName ")
    List<Purchase> getFind(String partName);

    /**
     * Insert a Purchase in the database. If the Purchase already exists, replace it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPurchase(Purchase purchase);

     /**
     * Update the complete status of a Purchase
     */
    @Query("UPDATE Shopping SET quantity = :quantity WHERE entryid = :shoppingId ")
    void updateQuantity(String shoppingId, String quantity);

    /**
     * Delete a Purchase by id.
     */
    @Query("DELETE FROM Shopping WHERE entryid = :shoppingId")
    int deletePurchaseById(String shoppingId);

    /**
     * Delete all Purchase.
     */
    @Query("DELETE FROM Shopping")
    void deleteShopping();


    /**
     * Select a Purchase by id.
     */
    @Query("SELECT * FROM Shopping WHERE user = :user and productid = :productid")
    Purchase getPurchaseByIdUser(String productid, String user);

}

