/*
 * Copyright (c) 2023.
 */

package io.inappchat.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import io.inappchat.sdk.InAppChatActivity
import io.inappchat.sdk.ui.InAppChatUI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, InAppChatActivity::class.java))
        setContent {
            MaterialTheme {
                InAppChatUI()
            }
        }
    }
}
