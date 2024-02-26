package ai.botstacks.sdk.internal.state

import ai.botstacks.sdk.internal.utils.urlEncode

data class Location(val latitude: Double?, val longitude: Double?, val address: String? = null) {
    val link: String
        get() = "https://www.google.com/maps/search/?api=1&query=" + urlEncode(
            address ?: "${latitude!!},${longitude!!}", "utf-8"
        )
    val markdown: String
        get() = "[Location${address?.let { ": $it" } ?: ""}](${link})"

}