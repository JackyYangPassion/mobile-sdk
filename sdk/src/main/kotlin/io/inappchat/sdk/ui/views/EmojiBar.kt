/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.inappchat.sdk.R
import io.inappchat.sdk.state.InAppChatStore
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.ift
import nl.coffeeit.aroma.emojipicker.presentation.ui.emoji.EmojiBottomSheet

@Composable
fun EmojiKeyboard(onEmoji: (String) -> Unit = {}) {
    val emojiBottomSheetDialogFragment = EmojiBottomSheet.newInstance({ emoji ->
        onEmoji(emoji.emoji)
    })
    (LocalContext.current as? AppCompatActivity)?.supportFragmentManager?.let {
        emojiBottomSheetDialogFragment.show(it, EmojiBottomSheet.TAG)
    }
}

@Composable
fun EmojiBar(
    current: String? = null,
    onEmoji: (String) -> Unit = { Log.d("EmojiBar", "Got Emoji" + it) }
) {
    var keyboard = remember {
        false
    }
    val emojis = InAppChatStore.current.settings.lastUsedReactions.toMutableList()
    current?.let {
        if (!emojis.contains(it)) {
            emojis.add(0, it)
            emojis.removeLast()
        }
    }
    if (keyboard) {
        EmojiKeyboard(onEmoji)
        return
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        emojis.map { emoji ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .circle(40.dp, colors.softBackground)
                  .border(
                    2.dp,
                    ift(current == emoji, colors.primary, Color.Transparent),
                    CircleShape
                  )
                  .clickable { onEmoji(emoji) }
            ) {
                androidx.compose.material3.Text(text = emoji, fontSize = 18.sp)
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .circle(40.dp, colors.softBackground)
              .clickable { keyboard = true }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "more reactions",
                tint = colors.text,
                modifier = Modifier.size(
                    20
                )
            )
        }
    }
}

@IPreviews
@Composable
fun EmojiBarPreviews() {
    InAppChatContext {
        EmojiBar(current = "🤟")
    }
}
