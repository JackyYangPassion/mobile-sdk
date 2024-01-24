/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.utils

import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.theme.DarkPalette
import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Dark mode",
    group = "UI mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true,
    backgroundColor = 0xFF2B2B2F
)
@Preview(
    name = "Light mode",
    group = "UI mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
annotation class IPreviews