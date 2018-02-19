package com.example.jorge.mytestapp.custom.action.util;

/**
 * Created by jorge on 19/02/2018.
 */

import android.support.annotation.NonNull;


import com.example.jorge.mytestapp.util.AppExecutors;

import java.util.concurrent.Executor;

/**
 * Allow instant execution of tasks.
 */
public class SingleExecutors extends AppExecutors {
    private static Executor instant = new Executor() {
        @Override
        public void execute(@NonNull Runnable command) {
            command.run();
        }
    };

    public SingleExecutors() {
        super(instant, instant, instant);
    }
}
