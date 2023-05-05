package io.inappchat.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.ui.views.Spinner
import io.inappchat.sdk.utils.launch

@Composable
fun Login() {
    Column {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
        ) {
            Text(text = "InAppChat Demo", fontSize = 30.sp, color = Color.White)
            if (InAppChat.shared.loggingIn) {
                Spinner()
            }
        }
        ElevatedButton(onClick = {
            launch { App.app.login() }
        }, enabled = !InAppChat.shared.loggingIn, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Login")
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    MaterialTheme {
        Login()
    }
}