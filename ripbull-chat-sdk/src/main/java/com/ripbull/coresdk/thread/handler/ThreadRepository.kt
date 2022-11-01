package com.ripbull.coresdk.thread.handler

import com.ripbull.ertc.cache.database.entity.Thread
import com.ripbull.ertc.cache.database.entity.ThreadUserLink
import com.ripbull.ertc.cache.database.entity.User
import com.ripbull.coresdk.thread.mapper.ThreadRecord
import com.ripbull.ertc.remote.model.response.CreateThreadResponse
import com.ripbull.ertc.remote.model.response.MessageResponse
import io.reactivex.Flowable
import io.reactivex.Single

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