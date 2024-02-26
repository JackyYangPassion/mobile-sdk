package ai.botstacks.sdk.internal.utils

internal inline fun <T> Iterable<T>.contains(predicate: (T) -> Boolean): Boolean {
    return firstOrNull(predicate) != null
}