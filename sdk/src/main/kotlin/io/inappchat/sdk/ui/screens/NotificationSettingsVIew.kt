/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.type.NotificationSetting
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.GrowSpacer
import io.inappchat.sdk.ui.views.Header
import io.inappchat.sdk.ui.views.Space
import io.inappchat.sdk.ui.views.Text
import io.inappchat.sdk.utils.IPreviews

@Composable
fun Option(name: String, selected: Boolean, onClick: () -> Unit) {
    Row(
            modifier = Modifier
                    .height(64.dp)
                    .padding(16.dp)
    ) {
        Text(name, iac = fonts.headline, color = colors.text)
        GrowSpacer()
        RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = colors.primary)
        )
    }
}

@Composable
fun NotificationSettingsView(back: () -> Unit) {
    val settings = Chats.current.settings
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Header(title = "Manage Notifications", back = back)
        Row(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = "Notification Status", iac = fonts.body, color = colors.text)
        }
        Space(20f)
        Option(
                name = "Allow All",
                selected = settings.notifications == NotificationSetting.all
        ) {
            settings.setNotifications(NotificationSetting.all)
        }
        Option(
                name = "Mentions Only",
                selected = settings.notifications == NotificationSetting.none
        ) {
            settings.setNotifications(NotificationSetting.mentions)
        }
        Option(
                name = "None",
                selected = settings.notifications == NotificationSetting.none
        ) {
            settings.setNotifications(NotificationSetting.none)
        }
        GrowSpacer()
        Row(
                modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
        ) {
            Row(
                    modifier = Modifier
                            .clickable { back() }
                            .height(60.dp)
                            .background(colors.primary, RoundedCornerShape(30.dp))
                            .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
            ) {
                Text(text = "Update".uppercase(), iac = fonts.headline, color = colors.background)
            }
        }
    }
}

@IPreviews
@Composable
fun NotificationSettingsPreview() {
    InAppChatContext {
        NotificationSettingsView {

        }
    }
}