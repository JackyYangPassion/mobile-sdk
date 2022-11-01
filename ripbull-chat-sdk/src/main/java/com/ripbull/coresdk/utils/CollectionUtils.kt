package com.ripbull.coresdk.utils

/**
 * @author meeth
 */
fun <U, V> List<U>.transform(function: (u: U) -> V): ArrayList<V> {
  val result = ArrayList<V>(size)
  for (u in this) {
    result.add(function(u))
  }
  return result
}

fun <K, V> List<V>.transformAsMap(function: (v: V) -> K): HashMap<K, V> {
  val result = HashMap<K, V>(size)
  for (v in this) {
    result[function(v)] = v
  }
  return result
}