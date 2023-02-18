package io.inappchat.InAppChat.Example

import android.app.Application
import android.util.Log
import io.inappchat.sdk.InAppChat

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        InAppChat.init(this, "fd119.dev.ertc.com", "eanrt0nu")
    }
}