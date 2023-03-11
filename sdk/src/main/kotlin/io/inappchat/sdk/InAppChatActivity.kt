/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import io.inappchat.sdk.ui.InAppChatUI

class InAppChatActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InAppChatUI()
        }
    }

}