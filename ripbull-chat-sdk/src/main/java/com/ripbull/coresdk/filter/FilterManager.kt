package com.ripbull.coresdk.filter

/**
 * Created by DK on 26/03/20.
 */
class FilterManager {
  private val filterChain: FilterChain = FilterChain()

  fun addFilter(filter: Filter) {
    filterChain.addFilter(filter)
  }

  fun filterRequest(command: Command) = filterChain.execute(command)
}