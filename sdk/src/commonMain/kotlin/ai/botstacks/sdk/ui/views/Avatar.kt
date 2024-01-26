/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.type.OnlineStatus
import ai.botstacks.sdk.ui.BotStacks.colors
import ai.botstacks.sdk.ui.BotStacks.dimens
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.utils.IPreviews
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt


sealed interface AvatarSize {
    val value: Dp

    data object Small : AvatarSize {
        override val value: Dp = 50.dp
    }

    data object Large : AvatarSize {
        override val value: Dp = 100.dp
    }

    data class Custom(val dp: Dp): AvatarSize {
        override val value: Dp = dp
    }
}

object AvatarDefaults {
    val Size: AvatarSize = AvatarSize.Small
}

sealed interface AvatarType {
    data class User(
        val url: String?,
        val status: OnlineStatus = OnlineStatus.UNKNOWN__,
    ) : AvatarType

    data class Channel(val urls: List<String?>) : AvatarType
}

/**
 * Convenience wrapper around [User] to handle [OnlineStatus] easily.
 */
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarDefaults.Size,
    user: User,
) {
    val type = remember(user.avatar, user.status) {
        AvatarType.User(
            url = user.avatar,
            status = user.status
        )
    }
    Avatar(
        modifier = modifier,
        type = type,
        size = size,
    )
}

/**
 * Compatibility wrapper for new [Avatar] using [AvatarType]
 */
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarDefaults.Size,
    status: OnlineStatus = OnlineStatus.UNKNOWN__,
    url: String?,
    chat: Boolean = false,
) {
    val type = if (chat) {
        AvatarType.Channel(listOf(url))
    } else {
        AvatarType.User(url, status)
    }
    Avatar(type = type, size = size, modifier = modifier)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarDefaults.Size,
    type: AvatarType,
) {
    Box(
        modifier = modifier
            .size(size.value)
            .background(color = colors.Light._100, shape = CircleShape),
    ) {
        when (type) {
            is AvatarType.Channel -> {
                val urls = type.urls
                val none = urls.all { it == null }
                if (!none) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .align(Alignment.Center),
                    ) {
                        val split = urls.take(4).chunked(2)
                        split.forEach { chunk ->
                            Column(modifier = Modifier.weight(1f)) {
                                chunk.forEach { user ->
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(user)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "user profile picture",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.weight(1f)

                                    )
                                }
                            }
                        }
                    }
                } else {
                    Image(
                        painter = painterResource(Res.Drawables.Outlined.User),
                        contentDescription = "channel profile picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimens.grid.x3)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        colorFilter = ColorFilter.tint(colors.Dark._900)
                    )
                }
            }

            is AvatarType.User -> {
                val url = type.url
                val status = type.status
                val isVisibleStatus = remember(status) {
                    when (status) {
                        OnlineStatus.Away,
                        OnlineStatus.Offline,
                        OnlineStatus.Online -> true

                        else -> false
                    }
                }
                if (url != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(url)
                            .crossfade(true)
                            .build(),
                        contentDescription = "user profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .align(Alignment.Center),
                    )
                } else {
                    Image(
                        painter = painterResource(Res.Drawables.Outlined.User),
                        contentDescription = "user profile picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimens.grid.x3)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        colorFilter = ColorFilter.tint(colors.Dark._900)
                    )
                }

                if (isVisibleStatus) {
                    Layout(
                        content = { OnlineStatusIndicator(status = status) }
                    ) { measurables, constraints ->
                        val dot = measurables[0].measure(constraints)
                        layout(
                            constraints.maxWidth,
                            constraints.maxHeight
                        ) {
                            dot.placeRelative(
                                x = constraints.maxWidth - dot.width,
                                y = (constraints.maxHeight - dot.height / 1.25f).roundToInt()
                            )
                        }
                    }
                }
            }
        }
    }
}

@IPreviews
@Composable
fun AvatarPreview() {
    Row(
        modifier = Modifier.padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val url = "https://source.unsplash.com/featured/300x200"
            Avatar(type = AvatarType.User(url))
            Avatar(type = AvatarType.User(url = url, status = OnlineStatus.Online))
            Avatar(type = AvatarType.User(url = url, status = OnlineStatus.Offline))
            Avatar(type = AvatarType.User(url = url, status = OnlineStatus.Away))
            Avatar(type = AvatarType.Channel(
                listOf("https://source.unsplash.com/featured/300x201",

                    "https://source.unsplash.com/featured/300x202",
                    "https://source.unsplash.com/featured/300x203",
                    "https://source.unsplash.com/featured/300x205"
                )))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val url = null
            Avatar(type = AvatarType.User(url))
            Avatar(type = AvatarType.User(url = url, status = OnlineStatus.Online))
            Avatar(type = AvatarType.User(url = url, status = OnlineStatus.Offline))
            Avatar(type = AvatarType.User(url = url, status = OnlineStatus.Away))
            Avatar(type = AvatarType.Channel(emptyList()))
        }
    }
}
