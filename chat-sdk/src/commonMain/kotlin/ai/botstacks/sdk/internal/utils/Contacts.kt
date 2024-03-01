package ai.botstacks.sdk.internal.utils

expect class Vcard

internal expect fun parseVcard(data: String?): Vcard?

internal expect fun Vcard.simpleName(): String

internal expect fun Vcard.markdown(): String