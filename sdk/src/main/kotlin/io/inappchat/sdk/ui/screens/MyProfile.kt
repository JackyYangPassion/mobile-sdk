/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apollographql.apollo3.api.Optional
import io.inappchat.sdk.API
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.R
import io.inappchat.sdk.state.Upload
import io.inappchat.sdk.state.User
import io.inappchat.sdk.type.UpdateProfileInput
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.Monitoring
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.genCurrentUser
import kotlinx.coroutines.launch

@Composable
fun MyProfile(
    openProfile: () -> Unit,
    openFavorites: () -> Unit,
    openNotificationSettings: () -> Unit
) {
    val scrollState = rememberScrollState()
    var logoutDialogue by remember { mutableStateOf(false) }
    var showImage by remember { mutableStateOf(false) }
    var upload by remember { mutableStateOf<Upload?>(null) }
    var editUsername by remember { mutableStateOf(false) }
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .verticalScroll(scrollState),
    ) {
        Header(title = "My Profile")
        Space(16f)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.clickable { showImage = true }) {
                Avatar(url = upload?.uri?.toString() ?: User.current?.avatar, 130.0)
            }
            Space()
            Box(modifier = Modifier
                .padding(4.dp)
                .clickable { editUsername = true }) {
                Text(
                    text = User.current?.username ?: "",
                    iac = fonts.title2,
                    color = colors.text,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 150.dp)
                )
            }
        }
        SimpleRow(icon = R.drawable.user_fill, text = "Profile", onClick = openProfile)
        SimpleRow(icon = R.drawable.star_fill, text = "Favorites", onClick = openFavorites)
        SimpleRow(
            icon = R.drawable.bell_simple_fill,
            text = "Manage Notifications",
            onClick = openNotificationSettings
        )
        SimpleRow(icon = R.drawable.door_fill, text = "Logout") {
            logoutDialogue = true
        }
        GrowSpacer()
    }
    if (logoutDialogue) {
        AlertDialog(
            onDismissRequest = { logoutDialogue = false },
            buttons = {
                Row(modifier = Modifier.height(44.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { logoutDialogue = false }) {
                        Text(text = "No", iac = fonts.headline, color = colors.text)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                logoutDialogue = false
                                InAppChat.logout()
                            }
                    ) {
                        Text(text = "Log out", iac = fonts.headline, color = colors.primary)
                    }
                }
            },
            title = {
                Text(
                    text = "Are you sure you want to logout?",
                    iac = fonts.headline,
                    color = colors.text
                )
            },
            backgroundColor = colors.background
        )
    }
    if (showImage) {
        AssetPicker(video = false, onUri = {
            showImage = false
            Toast.makeText(ctx, "Uploading profile picture", Toast.LENGTH_SHORT).show()
            val up = Upload(uri = it)
            upload = up
            up.upload()
            scope.launch {
                try {
                    bg {
                        val url = up.await()
                        API.updateProfile(UpdateProfileInput(image = Optional.present(url)))
                        User.current?.avatar = url
                    }
                    upload = null
                    Toast.makeText(ctx, "Profile picture updated", Toast.LENGTH_SHORT).show()
                } catch (ex: Throwable) {
                    Monitoring.error(ex)
                    Toast.makeText(
                        ctx,
                        "Failed to upload profile picture. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    upload = null
                }
            }
        }) {
            showImage = false
        }
    }
    if (editUsername) {
        NameChangeDialog(
            onDismiss = { editUsername = false },
            onComplete = { Toast.makeText(ctx, "Username updated", Toast.LENGTH_SHORT).show() },
            onError = { Toast.makeText(ctx, it, Toast.LENGTH_LONG).show() })
    }
}

@IPreviews
@Composable
fun MyProfilePreview() {
    genCurrentUser()
    InAppChatContext {
        MyProfile(
            openProfile = { /*TODO*/ },
            openFavorites = { /*TODO*/ },
            openNotificationSettings = { /*TODO*/ })
    }
}