package ai.botstacks.sdk.ui.theme

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


object BotStacksColorPalette {
    val Primary = BluePalette
    val Green = GreenPalette
    val Error = RedPalette
    val Dark = DarkPalette
    val Light = LightPalette
}

sealed interface ColorPalette
data object BluePalette : ColorPalette {
    val _100 = Color(0xFFF6F7FF)
    val _200 = Color(0xFFEBEDFF)
    val _300 = Color(0xFFD9DDFF)
    val _400 = Color(0xFFB2C4FF)
    val _500 = Color(0xFF89A4FF)
    val _600 = Color(0xFF7487FF)
    val _700 = Color(0xFF4772FF)
    val _800 = Color(0xFF295BFF)
    val _900 = Color(0xFF0E3FDF)
}

data object GreenPalette : ColorPalette {
    val _100 = Color(0xFFDBFDE9)
    val _200 = Color(0xFFECFEF3)
    val _300 = Color(0xFFDBFDE9)
    val _400 = Color(0xFFB7FAD2)
    val _500 = Color(0xFF90F8BA)
    val _600 = Color(0xFF7CF7AD)
    val _700 = Color(0xFF52F493)
    val _800 = Color(0xFF36F281)
    val _900 = Color(0xFF0CC054)
}

data object RedPalette : ColorPalette {
    val _100 = Color(0xFFFFDCD9)
    val _200 = Color(0xFFFFECEB)
    val _300 = Color(0xFFFFDCD9)
    val _400 = Color(0xFFFFB7B2)
    val _500 = Color(0xFFFF9189)
    val _600 = Color(0xFFFF7D74)
    val _700 = Color(0xFFFF5347)
    val _800 = Color(0xFFFF3729)
    val _900 = Color(0xFFDF1B0E)
}

data object DarkPalette : ColorPalette {
    val _100 = Color(0xFF87878C)
    val _200 = Color(0xFF71717A)
    val _300 = Color(0xFF62626A)
    val _400 = Color(0xFF53535A)
    val _500 = Color(0xFF45454A)
    val _600 = Color(0xFF3B3B3F)
    val _700 = Color(0xFF36363A)
    val _800 = Color(0xFF313135)
    val _900 = Color(0xFF2B2B2F)
}

data object LightPalette : ColorPalette {
    val _100 = Color(0xFFCFCFD3)
    val _200 = Color(0xFFD5D5D8)
    val _300 = Color(0xFFDADADD)
    val _400 = Color(0xFFE4E4E7)
    val _500 = Color(0xFFEAEAEB)
    val _600 = Color(0xFFEFEFF0)
    val _700 = Color(0xFFF4F4F5)
    val _800 = Color(0xFFFAFAFA)
    val _900 = Color.White
}