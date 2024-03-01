package ai.botstacks.sdk.internal.utils

import java.net.URLEncoder


internal actual fun urlEncode(value: String, encoding: String): String {
    return URLEncoder.encode(value, encoding)
}