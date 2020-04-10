/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aso.senote.BaseApplication
import com.aso.senote.databinding.ActivityMainBinding

/**
 * Main Activity for the app.
 * Holds The Navigation Host Fragment and the Drawer, bottom Toolbar, etc.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferencesRepository = (application as BaseApplication).preferenceRepository

        preferencesRepository.nightModeLive.observe(this, Observer {
            it?.let { delegate.localNightMode = it }
        })

//        This is how to change the dark theme
        preferencesRepository.isDarkTheme = false

    }




    enum class Action {
        PREVIEW, ADD
    }

}