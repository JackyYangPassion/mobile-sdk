package io.inappchat.inappchat.chat

/**
 * Created by DK on 01/06/20.
 */
open  class BaseFakeDao<T>(var hashMap: LinkedHashMap<Int, T?> = LinkedHashMap()) {

  fun insert(t: T?){
    hashMap[0] = t
  }

  fun insertWithFail(t: T?){}

  fun insertWithIgnore(t: T?){}

  fun insertWithReplace(t: T?){}

  fun insertWithRollback(t: T?){}

  fun insertWithAbort(t: T?){}

  fun update(t: T?){}

  fun insert(list: List<T?>?){}

  fun insertWithFail(list: List<T?>?){}

  fun insertWithIgnore(list: List<T?>?){}

  fun insertWithReplace(list: List<T?>?){}

  fun insertWithRollback(list: List<T?>?){}

  fun delete(t: T?){}

  fun delete(list: List<T?>?){}
}