package ai.botstacks.sdk.utils.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight

@SuppressLint("DiscouragedApi")
@Composable
actual fun font(name: String, res: String, weight: FontWeight): Font {
    val context = LocalContext.current
    val fontResId = context.resources.getIdentifier(res, "font", context.packageName)
    return Font(resId = fontResId, weight = weight)
}