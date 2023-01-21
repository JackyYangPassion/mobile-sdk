package io.inappchat.inappchat.chat.repository

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import io.inappchat.inappchat.chat.mapper.MessageRecord
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

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