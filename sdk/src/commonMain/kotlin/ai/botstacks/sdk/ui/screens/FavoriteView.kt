/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.views.Header
import ai.botstacks.sdk.ui.views.MessageView
import ai.botstacks.sdk.ui.views.PagerList
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.genM
import ai.botstacks.sdk.utils.random

@Composable
fun FavoritesView(
    back: () -> Unit,
    openReplies: (Message) -> Unit,
    openProfile: (User) -> Unit,
    scrollToTop: Int
) {
    LaunchedEffect(key1 = true, block = {
        BotStacksChatStore.current.favorites.loadMoreIfEmpty()
    })
    Column {
        Header(title = "Favorite Messages", onBackClick = back)
        PagerList(pager = BotStacksChatStore.current.favorites, scrollToTop = scrollToTop.toString()) {
            MessageView(message = it, onPressUser = openProfile, onLongPress = {}, onClick = {
                openReplies(it)
            })
        }
    }
}

@IPreviews
@Composable
fun FavoritesViewPreview() {
    BotStacksChatStore.current.favorites.items.addAll(random(50, { genM() }))
    BotStacksChatContext {
        FavoritesView(back = {}, openReplies = {}, openProfile = {}, scrollToTop = 0)
    }
}
