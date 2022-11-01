package com.ripbull.coresdk.filter

/**
 * Created by DK on 26/03/20.
 */
interface Filter {
  fun execute(command: Command): Boolean?
  fun setNext(filter: Filter?)
  fun getNext(): Filter?
  fun getLast(): Filter?
}