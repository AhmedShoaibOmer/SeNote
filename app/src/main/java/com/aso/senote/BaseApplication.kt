/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote

import android.app.Application
import android.content.Context
import com.aso.senote.data.PreferenceRepository
import com.aso.senote.data.source.DefaultNotesRepository
import com.aso.senote.data.source.DefaultNotesRepository.Companion.getInstance
import com.aso.senote.data.source.local.LocalNotesDataSource
import com.aso.senote.data.source.local.NoteDatabase
import com.aso.senote.data.source.local.NoteDatabase.Companion.getInstance
import com.aso.senote.data.source.remote.RemoteNotesDataSource

// Created by ahmedsomer on 4/1/20.
//
///***SeNote
//
//
/**
 * Android Application Class used or accessing Singletons.
 */
class BaseApplication : Application() {

    lateinit var preferenceRepository: PreferenceRepository

    private lateinit var mAppExecutors: AppExecutors

    private lateinit var localDataSource: LocalNotesDataSource

    private lateinit var remoteDataSource: RemoteNotesDataSource

    override fun onCreate() {
        super.onCreate()
        preferenceRepository = PreferenceRepository(
                getSharedPreferences(getString(R.string.PREFERENCES_FILE_KEY), Context.MODE_PRIVATE)
        )
        mAppExecutors = AppExecutors()
        localDataSource = LocalNotesDataSource(this, mAppExecutors)
        remoteDataSource = RemoteNotesDataSource()
    }

    val dataBase: NoteDatabase?
        get() = getInstance(this, mAppExecutors)

    val defaultNotesRepository: DefaultNotesRepository?
        get() = getInstance(remoteDataSource, localDataSource)


}