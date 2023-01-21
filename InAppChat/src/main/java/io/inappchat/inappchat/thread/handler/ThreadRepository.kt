package io.inappchat.inappchat.thread.handler

import io.inappchat.inappchat.cache.database.entity.Thread
import io.inappchat.inappchat.cache.database.entity.ThreadUserLink
import io.inappchat.inappchat.cache.database.entity.User
import io.inappchat.inappchat.thread.mapper.ThreadRecord
import io.inappchat.inappchat.remote.model.response.CreateThreadResponse
import io.inappchat.inappchat.remote.model.response.MessageResponse
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

/** Created by DK on 24/02/19.  */
interface ThreadRepository {

  fun getThreadByIdSync(threadId: String): Thread?

  fun getThreadByIdAsync(threadId: String): Single<Thread?>

  fun createThread(
    tenantId: String?,
    currentUser: User?,
    recipientUser: User?
  ): Single<String?>

  fun hasThread(
    tenantId: String?,
    currentUser: User?,
    recipientUser: User?
  ): Single<List<ThreadUserLink?>?>

  fun getThreads(tenantId: String): Flowable<MutableList<ThreadRecord>>?

  fun insertThreadData(
    response: CreateThreadResponse,
    chatUserId: String,
    tenantId: String,
    currentUser : User,
    recipientUser : User,
    lastMessage : MessageResponse? = null
  )

  fun getThread(tenantId: String, threadId: String): Single<ThreadRecord>
}