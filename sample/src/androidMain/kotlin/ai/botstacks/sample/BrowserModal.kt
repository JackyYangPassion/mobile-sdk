package ai.botstacks.sample

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ai.botstacks.sdk.ui.views.TextInput
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.rememberTextFieldState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrowserModal(url: String, onClose: () -> Unit) {
    val state = rememberTextFieldState(initialText = url)
    Column(modifier = Modifier.fillMaxHeight()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .height(44.dp)
                .padding(12.dp)
        ) {
            TextInput(modifier = Modifier.weight(1f), state = state, enabled = false)
            Box(
                modifier = Modifier
                    .height(44.dp)
                    .width(44.dp)
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "close")
            }
        }
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                alpha = 0.99f
                loadUrl(url)
            }
        }, update = {
        })
    }
}