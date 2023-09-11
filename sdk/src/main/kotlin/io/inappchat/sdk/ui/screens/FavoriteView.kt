/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.inappchat.sdk.state.InAppChatStore
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.Header
import io.inappchat.sdk.ui.views.MessageView
import io.inappchat.sdk.ui.views.PagerList
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genM
import io.inappchat.sdk.utils.random

@Composable
fun FavoritesView(
    back: () -> Unit,
    openReplies: (Message) -> Unit,
    openProfile: (User) -> Unit,
    scrollToTop: Int
) {
    LaunchedEffect(key1 = true, block = {
        InAppChatStore.current.favorites.loadMoreIfEmpty()
    })
    Column {
        Header(title = "Favorite Messages", back = back)
        PagerList(pager = InAppChatStore.current.favorites, scrollToTop = scrollToTop.toString()) {
            MessageView(message = it, onPressUser = openProfile, onLongPress = {}, onClick = {
                openReplies(it)
            })
        }
    }
}

@IPreviews
@Composable
fun FavoritesViewPreview() {
    InAppChatStore.current.favorites.items.addAll(random(50, { genM() }))
    InAppChatContext {
        FavoritesView(back = {}, openReplies = {}, openProfile = {}, scrollToTop = 0)
    }
}
