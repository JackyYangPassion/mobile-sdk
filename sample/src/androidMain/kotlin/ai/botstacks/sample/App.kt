/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sample

import android.app.Application
import com.giphy.sdk.ui.Giphy
import ai.botstacks.sdk.BotStacksChat

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
        Giphy.configure(this, getString(R.string.giphy))
        BotStacksChat.shared.setup(
            this,
            getString(R.string.inappchat)
        )
    }

    companion object {
        lateinit var app: App
    }

}