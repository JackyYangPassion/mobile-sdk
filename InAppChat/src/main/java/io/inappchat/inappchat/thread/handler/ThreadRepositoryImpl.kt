package io.inappchat.inappchat.thread.handler

import androidx.annotation.RestrictTo
import io.inappchat.inappchat.cache.database.entity.Thread
import io.inappchat.inappchat.cache.database.entity.ThreadUserLink
import io.inappchat.inappchat.cache.database.entity.User
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.thread.mapper.ThreadRecord
import io.inappchat.inappchat.remote.model.response.CreateThreadResponse
import io.inappchat.inappchat.remote.model.response.MessageResponse
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

/** Created by DK on 24/02/19.  */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ThreadRepositoryImpl private constructor(
  private val local: ThreadRepository,
  private val remote: ThreadRepository
) : ThreadRepository {

  companion object {
    @JvmStatic
    fun newInstance(dataManager: DataManager): ThreadRepository {
      val local: ThreadRepository = ThreadLocalRepository(dataManager)
      val remote: ThreadRepository = ThreadRemoteRepository(dataManager)
      return ThreadRepositoryImpl(local, remote)
    }
  }

  override fun getThreadByIdSync(threadId: String): Thread? {
    return local.getThreadByIdSync(threadId)
  }

  override fun getThreadByIdAsync(threadId: String): Single<Thread?> {
    return local.getThreadByIdAsync(threadId)
  }

  override fun createThread(
    tenantId: String?,
    currentUser: User?,
    recipientUser: User?
  ): Single<String?> {
    return local.hasThread(tenantId, currentUser, recipientUser)
      .flatMap { threadUserLink ->
        return@flatMap if (threadUserLink.isNotEmpty()) {
          Single.just(threadUserLink[0]?.threadId)
        } else {
          remote.createThread(tenantId, currentUser, recipientUser)
        }
      }
  }

  override fun insertThreadData(
    response: CreateThreadResponse,
    chatUserId: String,
    tenantId: String,
    currentUser: User,
    recipientUser: User,
    lastMessage : MessageResponse?
  ) {
    remote.insertThreadData(response,chatUserId,tenantId,currentUser, recipientUser, lastMessage)
  }

  override fun getThread(tenantId: String, threadId: String): Single<ThreadRecord> {
    return local.getThread(tenantId, threadId)
  }

  override fun hasThread(
    tenantId: String?,
    currentUser: User?,
    recipientUser: User?
  ): Single<List<ThreadUserLink?>?> {
    return local.hasThread(tenantId, currentUser, recipientUser)
  }

  override fun getThreads(tenantId: String): Flowable<MutableList<ThreadRecord>>?  = local.getThreads(tenantId)
}