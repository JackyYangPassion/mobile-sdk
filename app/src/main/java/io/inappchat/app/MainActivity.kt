/*
 * Copyright (c) 2023.
 */

package io.inappchat.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import io.inappchat.sdk.InAppChatActivity

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    startActivity(Intent(this, InAppChatActivity::class.java))
//    setContent {
//      MaterialTheme {
//        InAppChatUI()
//      }
//    }
  }
}
