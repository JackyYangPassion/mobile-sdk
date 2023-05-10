package com.g2minus.chatapp

import android.content.Context
import android.icu.text.UnicodeSet.SpanCondition
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.g2minus.chatapp.R.*
import io.inappchat.sdk.ui.theme.Assets
import io.inappchat.sdk.ui.theme.Colors
import io.inappchat.sdk.ui.theme.EmptyScreenConfig
import io.inappchat.sdk.ui.theme.FontStyle
import io.inappchat.sdk.ui.theme.Fonts
import io.inappchat.sdk.ui.theme.Theme

fun makeTheme(context: Context): Theme {
    val primary = Color(0xFF92C748)
    val fontName = GoogleFont("Nunito")

    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = array.com_google_android_gms_fonts_certs
    )
    val fontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )
    val app = { size: TextUnit, weight: FontWeight ->
        FontStyle(size, weight, fontFamily)
    }

    return Theme().apply {
        light = Colors(
            true,
            primary = primary,
            senderBubble = Color(0xFFC0D99E),
            senderUsername = primary
        )
        dark = Colors(
            false,
            primary = primary,
            senderBubble = Color(0xFFC0D99E),
            senderText = Color(0xFF202127),
            senderUsername = primary
        )
        fonts = Fonts(
            title = app(22.sp, FontWeight.ExtraBold),
            title2 = app(20.sp, FontWeight.SemiBold),
            title3 = app(15.sp, FontWeight.SemiBold),
            headline = app(13.sp, FontWeight.Bold),
            body = app(14.sp, FontWeight.Normal),
            caption = app(12.sp, FontWeight.Normal)
        )
        assets = Assets(
            drawable.pp_icon,
            emptyChannels = EmptyScreenConfig(
                drawable.empty_channels,
                "You haven't joined any channels yet"
            ),
            emptyChat = EmptyScreenConfig(
                drawable.empty_chat,
                "Your friends are ***dying*** to see you"
            ),
            emptyThreads = EmptyScreenConfig(
                drawable.empty_threads,
                "You haven't added to any threads yet"
            ),
            emptyAllChannels = EmptyScreenConfig(
                drawable.empty_all_channels,
                "It's dead in here"
            )

        )
    }
}
