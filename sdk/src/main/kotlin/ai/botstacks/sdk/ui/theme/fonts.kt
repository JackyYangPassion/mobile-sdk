package ai.botstacks.sdk.ui.theme

/*
 * Copyright (c) 2023.
 */

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Stable
data class FontStyle(
    val size: TextUnit = 14.sp,
    val weight: FontWeight = FontWeight.Normal,
    val family: FontFamily = FontFamily.Default
)

@Stable
data class Fonts(
    val title: FontStyle = FontStyle(size = 22.sp),
    val title2: FontStyle = FontStyle(size = 17.sp),
    val title3: FontStyle = FontStyle(size = 15.sp),
    val headline: FontStyle = FontStyle(size = 13.sp, weight = FontWeight.Bold),
    val body: FontStyle = FontStyle(size = 13.sp),
    val caption: FontStyle = FontStyle(size = 10.sp),
    val username: FontStyle = FontStyle(size = 12.sp, weight = FontWeight.ExtraBold),
    val timestamp: FontStyle = FontStyle(size = 12.sp),
    val mini: FontStyle = FontStyle(size = 10.sp)
)
