package io.inappchat.inappchat.thread.handler

import io.inappchat.inappchat.cache.database.dao.GroupDao
import io.inappchat.inappchat.cache.database.dao.ThreadDao
import io.inappchat.inappchat.cache.database.dao.UserDao
import io.inappchat.inappchat.cache.database.entity.Thread
import io.inappchat.inappchat.cache.database.entity.ThreadEmbedded
import io.inappchat.inappchat.cache.database.entity.ThreadUserLink
import io.inappchat.inappchat.cache.database.entity.User
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.group.mapper.GroupMapper
import io.inappchat.inappchat.thread.mapper.ThreadMapper
import io.inappchat.inappchat.thread.mapper.ThreadRecord
import io.inappchat.inappchat.user.mapper.UserMapper
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.remote.model.response.CreateThreadResponse
import io.inappchat.inappchat.remote.model.response.MessageResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

/** Created by DK on 24/02/19.  */
class ThreadLocalRepository constructor(
    private val dataManager: DataManager,
    private val threadDao: ThreadDao = dataManager.db().threadDao(),
    private val userDao: UserDao = dataManager.db().userDao(),
    private val groupDao: GroupDao = dataManager.db().groupDao()
) :
    ThreadRepository {
    override fun getThreadByIdSync(threadId: String): Thread? {
        return dataManager.db().threadDao().getThreadByIdInSync(threadId)
    }

    override fun getThreadByIdAsync(threadId: String): Single<Thread> {
        return dataManager.db().threadDao().getThreadByIdInAsync(threadId)
    }

    override fun createThread(
        tenantId: String?,
        currentUser: User?,
        recipientUser: User?
    ): Single<String> {
        return Single.error(Error("Not supported"))
    }

    override fun hasThread(
        tenantId: String?,
        currentUser: User?,
        recipientUser: User?
    ): Single<List<ThreadUserLink>> {
        return dataManager
            .db()
            .threadUserLinkDao()
            .hasThread(currentUser!!.id, recipientUser!!.id)
    }

    override fun getThreads(tenantId: String): Flowable<MutableList<ThreadRecord>> {
        return threadDao.getThreads(tenantId)
            .flatMap { threadEmbeddedList: List<ThreadEmbedded> ->
                var list: MutableList<ThreadRecord> =
                    ArrayList()
                for (threadEmbedded in threadEmbeddedList) {
                    val type = threadEmbedded.thread.type
                    if (type == ChatType.SINGLE.type) {
                        val threadUserLinks =
                            threadEmbedded.threadUserLinks
                        var userRecord: UserRecord? = null
                        if (threadUserLinks.size > 0) {
                            val recipientId = threadUserLinks[0].recipientId
                            val user = userDao.getUserByIdInSync(tenantId, recipientId) ?: continue
                            userRecord = UserMapper.transform(user)
                        }
                        val singleChat = dataManager.db().singleChatDao()
                            .getLastMessage(threadEmbedded.thread.id)
                        ThreadMapper.transform(threadEmbedded, userRecord, list, singleChat)
                    } else {
                        val group = groupDao.getGroupByIdInSync(
                            tenantId,
                            threadEmbedded.thread.recipientChatId
                        )
                        group?.let { it ->
                            val groupRecord =
                                GroupMapper.transform(
                                    it,
                                    tenantId,
                                    dataManager.preference().appUserId
                                )
                            val singleChat =
                                dataManager.db().singleChatDao()
                                    .getLastMessage(threadEmbedded.thread.id)
                            ThreadMapper.transform(threadEmbedded, groupRecord, list, singleChat)
                        }
                    }
                }
                val comparator = compareByDescending<ThreadRecord> { it.lastMessage.timestamp }
                list = list.sortedWith(comparator).toMutableList()
                Flowable.just(list)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertThreadData(
        response: CreateThreadResponse,
        chatUserId: String,
        tenantId: String,
        currentUser: User,
        recipientUser: User,
        lastMessage: MessageResponse?
    ) {
        // TODO: Nothing
    }

    override fun getThread(tenantId: String, threadId: String): Single<ThreadRecord> {
        return threadDao.getThread(tenantId, threadId).flatMap { threadEmbedded: ThreadEmbedded ->
            val type = threadEmbedded.thread.type
            var threadRecord: ThreadRecord? = null
            if (type == ChatType.SINGLE.type) {
                val threadUserLinks = threadEmbedded.threadUserLinks
                var userRecord: UserRecord? = null
                if (threadUserLinks.size > 0) {
                    val recipientId = threadUserLinks[0].recipientId
                    val user = userDao.getUserByIdInSync(tenantId, recipientId)
                    userRecord = UserMapper.transform(user)
                }
                threadRecord = ThreadMapper.transform(threadEmbedded.thread, userRecord, null)
            } else {
                val group = groupDao.getGroupByIdInSync(
                    tenantId,
                    threadEmbedded.thread.recipientChatId
                )
                group?.let { it ->
                    val groupRecord =
                        GroupMapper.transform(it, tenantId, dataManager.preference().appUserId)
                    threadRecord = ThreadMapper.transform(threadEmbedded.thread, null, groupRecord)
                }
            }
            if (threadRecord != null) {
                Single.just(threadRecord!!)
            } else {
                throw UnsupportedOperationException("Can't find thread")
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}