package ai.botstacks.sdk.ui.theme

import androidx.compose.ui.graphics.Color

object BotStacksColorPalette {
    val Primary = BluePalette
    val Green = GreenPalette
    val Red = RedPalette
    val Yellow = YellowPalette
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

data object YellowPalette : ColorPalette {
    val _100 = Color(0xFFFFFBEB)
    val _200 = Color(0xFFFFF5D1)
    val _300 = Color(0xFFFFF0BC)
    val _400 = Color(0xFFFFEA9F)
    val _500 = Color(0xFFFFE589)
    val _600 = Color(0xFFF4D66A)
    val _700 = Color(0xFFECC94A)
    val _800 = Color(0xFFDDB730)
    val _900 = Color(0xFFAD8800)
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
    val _900 = Color(0xFFFFFFFF)
}