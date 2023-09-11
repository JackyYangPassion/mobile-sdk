/*
 * Copyright (c) 2023.
 */

package com.g2minus.chatapp

import android.app.Application
import com.giphy.sdk.ui.Giphy
import io.inappchat.sdk.InAppChat

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        app = this
        Giphy.configure(this, getString(R.string.giphy))
        InAppChat.shared.setup(this, getString(R.string.inappchat), delayLoad = true)
        appState.load(this)
    }


    companion object {
        lateinit var app: App
    }

}