package io.inappchat.sdk.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apollographql.apollo3.api.Optional
import io.inappchat.sdk.API
import io.inappchat.sdk.state.User
import io.inappchat.sdk.type.UpdateProfileInput
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.utils.Monitoring
import io.inappchat.sdk.utils.bg
import kotlinx.coroutines.launch

@Composable
fun NameChangeDialog(
    title: String = "Change your username",
    onDismiss: () -> Unit,
    onComplete: () -> Unit,
    onError: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val textHint = "Choose your username"
    val scope = rememberCoroutineScope()
    val canSubmit = text.length >= 4
    val submit = {
        onDismiss()
        if (canSubmit) {
            scope.launch {
                try {
                    bg {
                        API.updateProfile(UpdateProfileInput(username = Optional.present(text)))
                    }
                    onComplete()
                    User.current?.username = text
                } catch (e: Throwable) {
                    Monitoring.error(e)
                    onError("Username couldn't be updated. Please try again.")
                }
            }
        } else {
            onError("Invalid username. Must be four or more alphanumeric characters.")
        }
    }
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            onDismiss()
                        }
                    )
                }
                Text(
                    text = title,
                    fonts.headline,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = text,
                    onValueChange = {
                        text = it.trim()
                    },
                    label = {
                        Text(text = textHint, fonts.body)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(Color.Transparent)
                )
            }

        },
        buttons = {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onDismiss, modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 24.dp)
                ) {
                    Text(text = "Cancel", fonts.body)
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 24.dp),
                    onClick = {
                        submit()
                    }) { Text(text = "Submit", fonts.body) }
            }
        }
    )
}