/*
 * Copyright (c) 2023.
 */

package io.inappchat.sample

import android.app.Application
import com.giphy.sdk.ui.Giphy
import io.inappchat.sdk.InAppChat

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
        Giphy.configure(this, getString(R.string.giphy))
        InAppChat.shared.setup(
            this,
            getString(R.string.inappchat)
        )
    }

    companion object {
        lateinit var app: App
    }

}