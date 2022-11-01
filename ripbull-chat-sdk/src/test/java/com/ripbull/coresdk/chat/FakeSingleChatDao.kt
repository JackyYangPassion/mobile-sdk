package com.ripbull.coresdk.chat

import com.ripbull.ertc.cache.database.entity.SingleChat

/**
 * Created by DK on 01/06/20.
 */
class FakeSingleChatDao(private val chatDatabase: LinkedHashMap<Int, SingleChat?> = LinkedHashMap()) :
  BaseFakeDao<SingleChat>(chatDatabase) {

}