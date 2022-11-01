package com.ripbull.coresdk.filter

/**
 * Created by DK on 26/03/20.
 */
class FilterChain {
  private var chain: Filter? = null

  fun addFilter(filter: Filter) {
    if (chain == null) {
      chain = filter
    } else {
      val last = chain?.getLast()
      last?.setNext(filter)
    }
  }

  fun execute(command: Command): Boolean? {
    return if (chain != null) {
      chain!!.execute(command)
    } else {
      false
    }
  }
}