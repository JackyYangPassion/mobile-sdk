package com.ripbull.coresdk.chat.repository

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import com.ripbull.coresdk.chat.mapper.MessageRecord
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

/** @author meeth
 */
@RestrictTo(LIBRARY_GROUP)
interface FavoriteMsgRepository {

  fun markAsFavorite(
    tenantId: String, threadId: String?, messages: List<MessageRecord?>, isFavorite: Boolean
  ): Single<List<MessageRecord>>

  fun getAllFavoriteMessages(tenantId: String): Observable<List<MessageRecord>>?

  fun getAllFavoriteMessages(
    tenantId: String, threadId: String
  ): Observable<List<MessageRecord>>?

  fun getAllFavoriteMessages(
    tenantId: String,
    threadId: String,
    parentMsgId: String
  ): Flowable<List<MessageRecord>>?
}