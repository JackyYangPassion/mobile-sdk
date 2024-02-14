package ai.botstacks.sdk.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.MissingResourceException
import platform.Foundation.NSBundle
import platform.Foundation.NSFileManager
import platform.posix.memcpy

private val cache: MutableMap<String, Font> = mutableMapOf()

@Composable
actual fun font(name: String, res: String, weight: FontWeight): Font {
    return cache.getOrPut(res) {
        val byteArray = runBlocking {
            readResourceBytes("font/$res.otf")
        }
        androidx.compose.ui.text.platform.Font(res, byteArray, weight, FontStyle.Normal)
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalForeignApi::class)
private fun readResourceBytes(path: String): ByteArray {
    val fileManager = NSFileManager.defaultManager()
    // todo: support fallback path at bundle root?
    val composeResourcesPath = NSBundle.mainBundle.resourcePath + "/compose-composeResources/" + path
    val contentsAtPath = fileManager.contentsAtPath(composeResourcesPath) ?: throw MissingResourceException(path)
    return ByteArray(contentsAtPath.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), contentsAtPath.bytes, contentsAtPath.length)
        }
    }
}