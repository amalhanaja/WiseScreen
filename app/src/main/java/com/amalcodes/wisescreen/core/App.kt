package com.amalcodes.wisescreen.core

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

/**
 * @author: AMAL
 * Created On : 18/07/20
 */
 

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}