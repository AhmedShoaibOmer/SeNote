/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote;

import android.app.Application;

import com.aso.senote.data.source.DefaultNotesRepository;
import com.aso.senote.data.source.local.LocalNotesDataSource;
import com.aso.senote.data.source.local.NoteDatabase;
import com.aso.senote.data.source.remote.RemoteNotesDataSource;

// Created by ahmedsomer on 4/1/20.
//
///***SeNote
//
//

/**
 * Android Application Class used or accessing Singletons.
 */
public class BaseApplication extends Application {

    private AppExecutors mAppExecutors;
    private LocalNotesDataSource localDataSource;
    private RemoteNotesDataSource remoteDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = new AppExecutors();
        localDataSource = new LocalNotesDataSource(this, mAppExecutors);
        remoteDataSource = new RemoteNotesDataSource();
    }

    public NoteDatabase getDataBase() {
        return NoteDatabase.getInstance(this, mAppExecutors);
    }

    public DefaultNotesRepository getDefaultNotesRepository() {
        return DefaultNotesRepository.getInstance(remoteDataSource, localDataSource);
    }
}
