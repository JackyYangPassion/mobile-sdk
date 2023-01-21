package io.inappchat.inappchat.filter

/**
 * Created by DK on 26/03/20.
 */
abstract class AbstractFilter : Filter {

  private var next: Filter? = null

  override fun setNext(filter: Filter?) {
    this.next = filter
  }

  override fun getNext(): Filter? = this.next

  override fun getLast(): Filter? {
    var last: Filter = this
    while (last.getNext() != null) {
      last = last.getNext()!!
    }
    return last
  }

  override fun execute(command: Command): Boolean? {
    return if (getNext() != null) {
      getNext()!!.execute(command)
    } else {
      false
    }
  }
}