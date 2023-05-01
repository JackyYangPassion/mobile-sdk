/*
 * Copyright (c) 2023.
 */

package io.inappchat.app

import android.app.Application
import com.giphy.sdk.ui.Giphy
import io.inappchat.sdk.InAppChat

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    Giphy.configure(this, "y29MF80OOqL4I0YQafUDAlcLiRjea4yI")
    InAppChat.shared.setup(this, "g2minus.ertc.com", "tsxghv2g", true)
    appState.load(this)
  }
}