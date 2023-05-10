package io.inappchat.sdk.ui.theme

/*
 * Copyright (c) 2023.
 */

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class Colors(
    val light: Boolean = false,
    val bubble: Color = if (light) Color(0xFFF0F0F0) else Color(0xFF2B2B2B),
    val bubbleText: Color = if (light) Color(0xFF1C1C1C) else Color(0xFFE3E3E3),
    val senderBubble: Color = Color(0xFFE5ECFF),
    val senderText: Color = Color(0xFF202127), // Color(hex: 0xE3E3E3)
    val senderUsername: Color = if (light) Color(0xFF000000) else Color(0xFFFFFFFF),
    val text: Color = if (light) Color(0xFF1C1C1C) else Color(0xFFE3E3E3),
    val username: Color = if (light) Color(0xFF2D3237) else Color(0xFFE3E3E3),
    val timestamp: Color = if (light) Color(0xFF71869C) else Color(0x4DE3E3E3),
    val primary: Color = Color(0xff0091ff),
    val button: Color = if (light) Color(0xFFF0F0F0) else Color(0xFF2B2B2B),
    val background: Color = if (light) Color(0xFFFFFFFF) else Color(0xFF171717),
    val destructive: Color = Color(0xFFC74848),
    val softBackground: Color = if (light) Color(0xFFD4D4D4) else Color(0xFF2B2B2B),
    val caption: Color = if (light) Color(0x502C2C2C) else Color(0x50E3E3E3),
    val unread: Color = Color(0xFFC74848),
    val _public: Color = Color(0xFF4B48C7),
    val _private: Color = Color(0xFF488AC7),
    val border: Color = if (light) Color(0xFF1B1B1B) else Color(0xFFE3E3E3)
)