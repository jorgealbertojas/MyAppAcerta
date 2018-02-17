package com.example.jorge.mytestapp.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.jorge.mytestapp.data.Purchase;

/**
 * Created by jorge on 15/02/2018.
 * The Room Database that contains the Purchase table.
 */

@Database(entities = {Purchase.class}, version = 1)
public abstract class ToDoDatabase extends RoomDatabase {

    private static ToDoDatabase INSTANCE;

    public abstract ShoppingDao ShoppingDao();

    private static final Object sLock = new Object();

    public static ToDoDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ToDoDatabase.class, "Purchase.db")
                        .build();
            }else{
               // INSTANCE.setTransactionSuccessful();
            }
           // INSTANCE.beginTransaction();

            return INSTANCE;
        }
    }

}