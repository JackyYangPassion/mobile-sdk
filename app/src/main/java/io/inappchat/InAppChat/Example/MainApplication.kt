package io.inappchat.InAppChat.Example

import android.app.Application
import io.inappchat.inappchat.InAppChat

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        InAppChat.init("fd119.dev.ertc.com", "eanrt0nu")
    }
}