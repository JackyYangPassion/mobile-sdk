/*
 * Copyright (c) 2023.
 */

package com.g2minus.chatapp

import android.app.Application
import com.auth0.android.Auth0
import com.giphy.sdk.ui.Giphy
import io.inappchat.sdk.InAppChat

class App : Application() {

  val auth0: Auth0 = Auth0(this)

  override fun onCreate() {
    super.onCreate()
    Giphy.configure(this, getString(R.string.giphy))

    InAppChat.shared.setup(this, "sample.ertc.com", "oj2k3r2x", true)
  }
}