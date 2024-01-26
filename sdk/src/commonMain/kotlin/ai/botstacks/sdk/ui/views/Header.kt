/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import ai.botstacks.sdk.type.OnlineStatus
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.utils.Fn
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.annotated
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

val HeaderHeight = 56.dp

object HeaderDefaults {
    @Composable
    fun Title(text: String) {
        Text(
            text = text.annotated(),
            iac = BotStacks.fonts.body1.copy(weight = FontWeight.W600),
            color = colorScheme.onBackground,
            maxLines = 1
        )
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun Logo() {
        Icon(
            painter = painterResource(resource = Res.Drawables.Logos.BotStacks),
            tint = Color.Unspecified,
            contentDescription = "logo"
        )
    }

    @Composable
    fun NextAction(onClick: Fn) {
        Pressable(onClick = onClick) {
            Text(
                text = "Next",
                fontStyle = BotStacks.fonts.button1,
                color = colorScheme.primary,
            )
        }
    }

    val IconSize: Dp = 20.dp
}

internal fun Modifier.requiredIconSize() = this.size(HeaderDefaults.IconSize)

@Composable
fun Header(
    title: String,
    icon: @Composable Fn = { },
    onSearch: ((String) -> Unit)? = null,
    onAdd: Fn? = null,
    onCompose: Fn? = null,
    onBackClick: Fn? = null,
    onMenuClick: Fn? = null,
    endAction: @Composable Fn = { },
) {
    Header(
        title = {
            HeaderDefaults.Title(text = title)
        },
        icon = icon,
        onSearch = onSearch,
        onAdd = onAdd,
        onCompose = onCompose,
        onBackClick = onBackClick,
        onMenuClick = onMenuClick,
        endAction = endAction,
    )
}

@Composable
fun Header() {
    Header(icon = { HeaderDefaults.Logo() })
}

@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
fun Header(
    title: @Composable Fn = { },
    icon: @Composable Fn = { },
    onSearch: ((String) -> Unit)? = null,
    onAdd: Fn? = null,
    onCompose: Fn? = null,
    onBackClick: Fn? = null,
    onMenuClick: Fn? = null,
    endAction: @Composable Fn = { },
) {
    Row(
        modifier = Modifier
            .background(colorScheme.header)
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(
                vertical = 4.dp,
                horizontal = 16.dp
            )
            .heightIn(min = HeaderHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (onBackClick != null) {
            HeaderButton(
                onBackClick,
                true
            ) {
                Icon(
                    painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.ArrowBack),
                    contentDescription = "back",
                    modifier = Modifier.size(24.dp),
                    tint = colorScheme.onBackground,
                )
            }
        }

        var searchActive by remember {
            mutableStateOf(false)
        }
        val search = rememberTextFieldState()

        AnimatedContent(
            modifier = Modifier.weight(1f),
            targetState = searchActive, label = ""
        ) { active ->
            if (active) {
                val focusRequester = remember { FocusRequester() }
                SearchField(
                    modifier = Modifier.focusRequester(focusRequester),
                    state = search,
                    onSearch = { onSearch?.invoke(search.text.toString()) },
                    onClear = { searchActive = false }
                )

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                ) {
                    icon()
                    title()
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                    ) {

                        if (onSearch != null) {
                            HeaderButton(onClick = { searchActive = true }) {
                                Icon(
                                    painterResource(Res.Drawables.Outlined.MagnifyingGlass),
                                    contentDescription = "Search",
                                    tint = colorScheme.onBackground,
                                    modifier = Modifier.requiredIconSize()
                                )
                            }
                        }
                        if (onAdd != null) {
                            HeaderButton(onAdd) {
                                Icon(
                                    painterResource(Res.Drawables.Outlined.Plus),
                                    contentDescription = "Add",
                                    tint = colorScheme.onBackground,
                                    modifier = Modifier.requiredIconSize()
                                )
                            }
                        }
                        if (onCompose != null) {
                            HeaderButton(onClick = onCompose) {
                                Icon(
                                    painterResource(Res.Drawables.Outlined.Edit),
                                    contentDescription = "Compose",
                                    tint = colorScheme.onBackground,
                                    modifier = Modifier.requiredIconSize()
                                )
                            }
                        }
                        if (onMenuClick != null) {
                            HeaderButton(onClick = onMenuClick) {
                                Icon(
                                    Icons.Outlined.MoreVert,
                                    contentDescription = "Menu",
                                    tint = colorScheme.onBackground,
                                    modifier = Modifier.requiredIconSize()
                                )
                            }
                        }
                    }
                }
            }
        }
        endAction()
    }
}

@Composable
fun HeaderButton(onClick: Fn, transparent: Boolean = false, icon: @Composable Fn) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(0.dp)
            .size(30.dp)
            .background(
                if (transparent) Color.Transparent else colorScheme.header,
                CircleShape
            )
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .border(0.dp, Color.Transparent, CircleShape)
    ) {
        icon()
    }
}


@IPreviews
@Composable
fun HeaderPreviews() {
    BotStacksChatContext {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            Header(
                icon = { HeaderDefaults.Logo() },
                onSearch = {},
                onCompose = {},
                onMenuClick = {}
            )
            Header(title = "Thread", onBackClick = {}, onMenuClick = {})
            Header(title = "Create Channel",
                onBackClick = {},
                endAction = {
                    HeaderDefaults.NextAction {}
                }
            )
            Header(
                icon = {
                    Avatar(
                        modifier = Modifier.padding(vertical = 5.dp),
                        size = AvatarSize.Custom(46.dp),
                        url = null,
                        status = OnlineStatus.Online
                    )
                },
                onBackClick = {},
                title = "Albert Bell",
                onMenuClick = {}
            )

            Header(
                onBackClick = {},
                title = "Users",
                onSearch = {}
            )
        }
    }
}