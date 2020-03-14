/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote;
// Created by ahmedsomer on 4/2/20.
//
///***SeNote
//
//

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global Executor pools or the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g local reads don't wait
 * behind webservice requests).
 */
public class AppExecutors {
    private final Executor mLocalIO;

    private final Executor mNetworkIO;

    private final Executor mMainThread;

    private AppExecutors(Executor localIO, Executor networkIo, Executor mainThread) {
        this.mLocalIO = localIO;
        this.mNetworkIO = networkIo;
        this.mMainThread = mainThread;
    }

    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new MainThreadExecutor());
    }

    public Executor localIO() {
        return mLocalIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
