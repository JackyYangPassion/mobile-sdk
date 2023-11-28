/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.extensions

public inline fun <T> Iterable<T>.contains(predicate: (T) -> Boolean): Boolean {
  return firstOrNull(predicate) != null
}

