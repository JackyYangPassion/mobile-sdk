/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sample

import android.app.Application
import ai.botstacks.sdk.BotStacksChat

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        BotStacksChat.shared.setup(
            context = this,
            apiKey = getString(R.string.botstacks_api_key),
            giphyApiKey = getString(R.string.giphy_api_key),
        )
    }
}