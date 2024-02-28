/*
 * Copyright (c) 2023.
 */

@file:OptIn(ExperimentalResourceApi::class)

package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.internal.ui.components.OverflowMenu
import ai.botstacks.sdk.internal.ui.components.Pressable
import ai.botstacks.sdk.internal.ui.components.SearchField
import ai.botstacks.sdk.internal.ui.components.Text
import ai.botstacks.sdk.internal.ui.resources.botstacks_logo_daynight
import ai.botstacks.sdk.internal.utils.IPreviews
import ai.botstacks.sdk.internal.utils.annotated
import ai.botstacks.sdk.state.OnlineStatus
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.BotStacksThemeEngine
import ai.botstacks.sdk.ui.theme.LocalBotStacksAssets
import ai.botstacks.sdk.ui.theme.painterImageAsset
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import botstacks.sdk.core.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private val HeaderHeight = 56.dp

object HeaderDefaults {
    @Composable
    fun Title(text: String) {
        Text(
            text = text.annotated(),
            fontStyle = fonts.h2,
            color = colorScheme.onBackground,
            maxLines = 1
        )
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun Logo() {
        val icon = LocalBotStacksAssets.current.logo?.let { painterImageAsset(it) }
            ?: painterResource(resource = Res.drawable.botstacks_logo_daynight)

        Icon(
            painter = icon,
            tint = Color.Unspecified,
            contentDescription = "logo"
        )
    }

    @Composable
    fun NextAction(onClick: () -> Unit) {
        Pressable(onClick = onClick) {
            Text(
                text = "Next",
                fontStyle = fonts.button1,
                color = colorScheme.primary,
            )
        }
    }

    @Composable
    fun SaveAction(onClick: () -> Unit) {
        Pressable(onClick = onClick) {
            Text(
                text = "Save",
                fontStyle = fonts.button1,
                color = colorScheme.primary,
            )
        }
    }

    @Composable
    fun CreateAction(onClick: () -> Unit) {
        Pressable(onClick = onClick) {
            Text(
                text = "Create",
                fontStyle = fonts.button1,
                color = colorScheme.primary,
            )
        }
    }

    @Composable
    fun MenuAction(onClick: () -> Unit) {
        HeaderButton(onClick = onClick) {
            Icon(
                Icons.Outlined.MoreVert,
                contentDescription = "Menu",
                tint = colorScheme.onBackground,
                modifier = Modifier.requiredIconSize()
            )
        }
    }

    val IconSize: Dp = 20.dp
}

internal fun Modifier.requiredIconSize() = this.size(HeaderDefaults.IconSize)

class HeaderState(
    val showSearch: Boolean = false,
    val showSearchClear: Boolean = false,
    isSearchActive: Boolean = false,
) {
    var searchQuery: TextFieldValue by mutableStateOf(TextFieldValue())
    var searchActive by mutableStateOf(isSearchActive)
}

@Composable
fun rememberHeaderState(
    isSearchVisible: Boolean = false,
    isSearchActive: Boolean = false,
    showSearchClear: Boolean = false,
) = remember(isSearchVisible, showSearchClear) {
    HeaderState(isSearchVisible, isSearchActive, showSearchClear)
}
@Composable
fun Header(
    title: String,
    icon: @Composable () -> Unit = { },
    state: HeaderState = rememberHeaderState(),
    onSearchClick: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    onCompose: (() -> Unit)? = null,
    onBackClicked: (() -> Unit)? = null,
    menu: (OverflowMenuScope.() -> Unit)? = null,
    endAction: @Composable () -> Unit = { },
) {
    Header(
        title = { HeaderDefaults.Title(text = title) },
        icon = icon,
        state = state,
        onSearchClick = onSearchClick,
        onAdd = onAdd,
        onCompose = onCompose,
        onBackClick = onBackClicked,
        menu = menu,
        endAction = endAction,
    )
}


@Composable
fun Header() {
    Header(icon = { HeaderDefaults.Logo() })
}

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalResourceApi::class
)
@Composable
fun Header(
    title: @Composable () -> Unit = { },
    icon: @Composable () -> Unit = { },
    state: HeaderState = rememberHeaderState(),
    onSearchClick: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    onCompose: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    menu: (OverflowMenuScope.() -> Unit)? = null,
    endAction: @Composable () -> Unit = { },
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
                onClick = onBackClick,
            ) {
                Icon(
                    painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.ArrowBack),
                    contentDescription = "back",
                    modifier = Modifier.size(24.dp),
                    tint = colorScheme.onBackground,
                )
            }
        }

        AnimatedContent(
            modifier = Modifier.weight(1f),
            targetState = state.searchActive,
            label = "",
            transitionSpec = {
                fadeIn() togetherWith  fadeOut()
            }
        ) { active ->
            if (active) {
                val focusRequester = remember { FocusRequester() }
                SearchField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = state.searchQuery,
                    onValueChanged = { state.searchQuery = it },
                    textColor = colorScheme.onHeader,
                    showClear = state.showSearchClear,
                    onClear = { state.searchQuery = TextFieldValue() }
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

                        if (state.showSearch) {
                            HeaderButton(onClick = onSearchClick?.let { { it.invoke() } } ?: { state.searchActive = true }) {
                                Icon(
                                    painterResource(Res.drawable.magnifying_glass),
                                    contentDescription = "Search",
                                    tint = colorScheme.onBackground,
                                    modifier = Modifier.requiredIconSize()
                                )
                            }
                        }
                        if (onAdd != null) {
                            HeaderButton(onClick = onAdd) {
                                Icon(
                                    painterResource(Res.drawable.plus),
                                    contentDescription = "Add",
                                    tint = colorScheme.onBackground,
                                    modifier = Modifier.requiredIconSize()
                                )
                            }
                        }
                        if (onCompose != null) {
                            HeaderButton(onClick = onCompose) {
                                Icon(
                                    painterResource(Res.drawable.edit_outlined),
                                    contentDescription = "Compose",
                                    tint = colorScheme.onBackground,
                                    modifier = Modifier.requiredIconSize()
                                )
                            }
                        }
                        if (menu != null) {
                            var expanded by remember {
                                mutableStateOf(false)
                            }
                            OverflowMenu(
                                visible = expanded,
                                onDismissRequest = { expanded = false },
                                menu = { menu(this) },
                            ) {
                                HeaderButton(onClick = { expanded = true }) {
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
        }
        endAction()
    }
}

@Composable
internal fun HeaderButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(30.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        icon()
    }
}


@OptIn(ExperimentalFoundationApi::class)
@IPreviews
@Composable
private fun HeaderPreviews() {
    BotStacksThemeEngine {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            Header(
                icon = { HeaderDefaults.Logo() },
                state = rememberHeaderState(isSearchVisible = true),
                onCompose = {},
                menu = {
                    label(onClick = {}) {
                        Text(text = "Log out", fontStyle = fonts.body1, color = colorScheme.error)
                    }
                }
            )
            Header(
                title = "Thread",
                onBackClicked = {},
                menu = {
                    label(onClick = {}) {
                        Text(text = "Log out", fontStyle = fonts.body1, color = colorScheme.error)
                    }
                }
            )
            Header(title = "Create Channel",
                onBackClicked = {},
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
                onBackClicked = {},
                title = "Albert Bell",
                menu = {
                    label(onClick = {}) {
                        Text(text = "Log out", fontStyle = fonts.body1, color = colorScheme.error)
                    }
                }
            )

            Header(
                onBackClicked = {},
                title = "Users",
                state = rememberHeaderState(isSearchVisible = true)
            )
        }
    }
}