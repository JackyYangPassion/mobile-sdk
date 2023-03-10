/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import io.inappchat.sdk.ui.theme.InAppChatTheme

class InAppChatActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            theme {
            }
        }
    }

    companion object {
        var theme: @Composable (content: @Composable () -> Unit) -> Unit = {
            InAppChatTheme(content = it)
        }
    }
}