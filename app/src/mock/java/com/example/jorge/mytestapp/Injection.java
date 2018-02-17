package com.example.jorge.mytestapp;

import android.content.Context;
import android.support.annotation.NonNull;


import com.example.jorge.mytestapp.data.FakeShoppingRemoteDataSource;
import com.example.jorge.mytestapp.data.source.ShoppingRepository;
import com.example.jorge.mytestapp.data.source.local.ShoppingLocalDataSource;
import com.example.jorge.mytestapp.data.source.local.ToDoDatabase;
import com.example.jorge.mytestapp.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 15/02/2018.
 */
public class Injection {

    public static ShoppingRepository provideShoppingRepository(@NonNull Context context) {
        checkNotNull(context);
        ToDoDatabase database = ToDoDatabase.getInstance(context);

        return ShoppingRepository.getInstance(FakeShoppingRemoteDataSource.getInstance(),
                ShoppingLocalDataSource.getInstance(new AppExecutors(),
                        database.ShoppingDao()));
    }
}