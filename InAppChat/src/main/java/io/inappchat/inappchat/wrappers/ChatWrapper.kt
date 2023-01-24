package io.inappchat.inappchat.wrappers

import android.annotation.SuppressLint
import android.text.TextUtils
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import io.inappchat.inappchat.announcement.mapper.AnnouncementMapper
import io.inappchat.inappchat.announcement.mapper.AnnouncementRecord
import io.inappchat.inappchat.chat.mapper.ChatEntityMapper.getChatRow
import io.inappchat.inappchat.chat.mapper.ChatEntityMapper.getGroupInfoChatRow
import io.inappchat.inappchat.chat.mapper.ChatEntityMapper.getThreadChatRow
import io.inappchat.inappchat.chat.mapper.ChatEvent
import io.inappchat.inappchat.chat.mapper.ChatReactionRecord
import io.inappchat.inappchat.chat.mapper.ChatRecordMapper.transform
import io.inappchat.inappchat.chat.mapper.MessageRecord
import io.inappchat.inappchat.chat.mapper.ThreadMessageMetadata
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.core.type.ChatEventType
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.DeleteType
import io.inappchat.inappchat.core.type.DownloadStatus
import io.inappchat.inappchat.core.type.EventType
import io.inappchat.inappchat.core.type.MessageStatus
import io.inappchat.inappchat.core.type.MessageUpdateType
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.e2e.E2EMapper
import io.inappchat.inappchat.e2e.ECDHUtils
import io.inappchat.inappchat.InAppChat
import io.inappchat.inappchat.fcm.NotificationRecord
import io.inappchat.inappchat.group.mapper.GroupMapper
import io.inappchat.inappchat.group.mapper.GroupRecord
import io.inappchat.inappchat.group.model.Event
import io.inappchat.inappchat.group.model.GroupUpdate
import io.inappchat.inappchat.thread.mapper.ThreadMapper
import io.inappchat.inappchat.user.mapper.UserMapper
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.utils.Logger.d
import io.inappchat.inappchat.utils.Logger.i
import io.inappchat.inappchat.cache.database.DataSource
import io.inappchat.inappchat.cache.database.entity.ChatReactionEntity
import io.inappchat.inappchat.cache.database.entity.ChatThread
import io.inappchat.inappchat.cache.database.entity.DomainFilter
import io.inappchat.inappchat.cache.database.entity.EKeyTable
import io.inappchat.inappchat.cache.database.entity.ProfanityFilter
import io.inappchat.inappchat.cache.database.entity.SingleChat
import io.inappchat.inappchat.cache.database.entity.SingleChatEmbedded
import io.inappchat.inappchat.cache.database.entity.TenantConfig
import io.inappchat.inappchat.cache.preference.PreferenceManager
import io.inappchat.inappchat.mqtt.MqttManager
import io.inappchat.inappchat.mqtt.listener.ReceivedMessage
import io.inappchat.inappchat.mqtt.model.AnnouncementResponse
import io.inappchat.inappchat.mqtt.model.ChatTopicResponse
import io.inappchat.inappchat.mqtt.model.ChatUpdateResponse
import io.inappchat.inappchat.mqtt.model.ForwardChat
import io.inappchat.inappchat.mqtt.model.ReplyThread
import io.inappchat.inappchat.mqtt.utils.Constants
import io.inappchat.inappchat.remote.model.response.ChatSettingsResponse
import io.inappchat.inappchat.remote.model.response.GroupResponse
import io.inappchat.inappchat.remote.model.response.TenantDetailResponse
import com.ripbull.mqtt.model.MessageReadStatus
import io.inappchat.inappchat.downloader.utils.Status
import io.inappchat.inappchat.utils.Constants.Features.Companion.DELETE
import io.inappchat.inappchat.utils.Constants.Features.Companion.EDIT
import io.inappchat.inappchat.utils.Constants.Features.Companion.SELF
import io.inappchat.inappchat.utils.Constants.Features.Companion.THIS_MESSAGE_WAS_DELETED
import io.inappchat.inappchat.utils.Constants.Features.Companion.THIS_MESSAGE_WAS_DELETED_FOR_YOU
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

/** Created by DK on 16/03/19.  */
class ChatWrapper(private val dataManager: DataManager, private val eventHandler: EventHandler) {
    private val db: DataSource = dataManager.db()
    private val gson: Gson = Gson()
    private val mqttManager: MqttManager = dataManager.mqtt()
    private var chatUserId: String? = null    // eRTCUserId
    private var appUserId: String? = null // appUserId
    private var deviceId: String? = null // deviceId
    private var tenantId: String? = null // tenantId

    private val preferenceManager: PreferenceManager = dataManager.preference()

    private fun getChatUserId(): String? {
        return if (this.chatUserId == null) {
            this.chatUserId = preferenceManager.chatUserId
            this.chatUserId
        } else {
            this.chatUserId
        }
    }

    private fun getAppUserId(): String? {
        return if (this.appUserId == null) {
            this.appUserId = preferenceManager.appUserId
            this.appUserId
        } else {
            this.appUserId
        }
    }

    private fun getDeviceId(): String? {
        return if (this.deviceId == null) {
            this.deviceId = preferenceManager.deviceId
            this.deviceId
        } else {
            this.deviceId
        }
    }

    private fun getTenantId(): String? {
        return if (this.tenantId == null) {
            this.tenantId = preferenceManager.tenantId
            this.tenantId
        } else {
            this.tenantId
        }
    }

    @SuppressLint("CheckResult")
    fun messagesOn(message: ReceivedMessage): Observable<MessageRecord> {
        return Observable.create { e: ObservableEmitter<MessageRecord> ->

            val response =
                gson.fromJson(message.message, ChatTopicResponse::class.java)
            // Check if thread is present. If not, insert one
            val replyThreadFeatureData =
                response.replyThreadFeatureData
            val threadId = response.thread.threadId
            val msgId = UUID.randomUUID().toString()
            val threadUserLinks = db.threadUserLinkDao().hasThread(threadId)
            var chatType = ChatEventType.INCOMING
            response.isFollowThread = if (response.follow == true) {
                1
            } else {
                0
            }
            if (getAppUserId().equals(response.sender.appUserId)) {
                chatType = ChatEventType.OUTGOING
            }
            if (threadUserLinks.size > 0) {
                if (replyThreadFeatureData?.baseMsgUniqueId != null) { // update thread table
                    val chatThreadList = db.chatThreadDao()
                        .hasMessage(response.msgUniqueId, MessageStatus.DELIVERED.status)
                    if (chatThreadList.size > 0) {
                        e.onComplete()
                        return@create
                    }
                    val senderUserRecord = if (response.thread.threadType == ChatType.GROUP.type) {
                        UserMapper.transform(
                            db.userDao().getUserByIdInSync(
                                response.tenantId, response.sender.appUserId
                            )
                        )
                    } else {
                        UserRecord(id = response.sender.appUserId, name = response.sender.name)
                    }

                    var groupRecord: GroupRecord? = null
                    if (response.thread.threadType == ChatType.GROUP.type) {
                        val group = db.groupDao()
                            .getGroupByThreadId(response.tenantId, response.thread.threadId)
                        groupRecord =
                            GroupMapper.transform(group, response.tenantId, getAppUserId())
                    }

                    publishStatus(response)

                    //val thread = db.threadDao().getThreadByIdInSync(threadId)
                    val singleChatParent = db.singleChatDao()
                        .getChatByServerMsgId(
                            response.replyThreadFeatureData!!.baseMsgUniqueId,
                            threadId
                        )

                    if (chatType == ChatEventType.INCOMING) {
                        //increasing the unreadCount of a thread
                        val unreadCount =
                            db.threadDao().getThreadByIdInSync(threadId).unReadCount + 1
                        db.threadDao().updateUnreadCount(threadId, unreadCount)
                    }
                    val from =
                        ThreadMapper.from(response, getChatUserId(), getAppUserId())
                    Observable.create { e: ObservableEmitter<MessageRecord> ->
                        val chatThread = getThreadChatRow(
                            response,
                            from,
                            preferenceManager, db.ekeyDao(), singleChatParent.id, msgId = msgId
                        )
                        d("thread_debug ###", "messageOn : chatThread : $chatThread")

                        db.chatThreadDao().insertWithAbort(chatThread)

                        d("thread_debug ###", "messageOn : chatThread : onNext")
                        // update Chat thread UI
                        e.onNext(
                            transform(
                                chatThread = chatThread!!,
                                senderRecord = senderUserRecord,
                                groupRecord = groupRecord,
                                notificationRecord = NotificationRecord(
                                    message.tile(),
                                    message.body()
                                ),
                                chatEventType = chatType,
                                isSilent = response.isSilent
                            )
                        )
                        val chatThreads =
                            db.chatThreadDao().getChatThreads(singleChatParent.id).blockingGet()

                        if (replyThreadFeatureData.replyMsgConfig == 1) {
                            val singleChat = getChatRow(
                                response,
                                ThreadMapper.from(response, getChatUserId(), getAppUserId()),
                                preferenceManager,
                                db.ekeyDao(),
                                singleChatParent.id,
                                getParentMessage(singleChatParent),
                                msgId = msgId
                            )
                            db.singleChatDao().insertWithAbort(singleChat)
                            // update main chat UI
                            e.onNext(
                                transform(
                                    singleChat = singleChat,
                                    senderRecord = senderUserRecord,
                                    groupRecord = groupRecord,
                                    chatEventType = chatType,
                                    chatThreadList = chatThreads,
                                    isSilent = response.isSilent
                                )
                            )
                        }
                        // thread msg metadata fro single chat update
                        eventHandler.chatEventSource().onNext(
                            ChatEvent(
                                eventType = EventType.THREAD_MESSAGE_METADATA,
                                threadId = threadId,
                                threadMetadata = ThreadMessageMetadata(
                                    chatThreadCount = chatThreads.size,
                                    parentMsgId = singleChatParent.id,
                                    msgId = chatThread.id
                                )
                            )
                        )

                        db.singleChatDao().update(singleChatParent)
                    }.subscribeOn(Schedulers.io())
                        .doOnNext {
                            it?.let { messageRecord -> e.onNext(messageRecord) }
                        }.subscribe()

                } else {
                    // message received where thread created already
                    val singleChatList = db.singleChatDao()
                        .hasMessage(threadId, response.msgUniqueId, MessageStatus.DELIVERED.status)
                    if (singleChatList.size > 0) {
                        e.onComplete()
                        return@create
                    }

                    val senderUserRecord: UserRecord? =
                        if (response.thread.threadType == ChatType.GROUP.type) {
                            UserMapper.transform(
                                db.userDao().getUserByIdInSync(
                                    response.tenantId, response.sender.appUserId
                                )
                            )
                        } else {
                            UserRecord(id = response.sender.appUserId, name = response.sender.name)
                        }

                    var groupRecord: GroupRecord? = null
                    if (response.thread.threadType == ChatType.GROUP.type) {
                        val group = db.groupDao()
                            .getGroupByThreadId(response.tenantId, response.thread.threadId)
                        groupRecord =
                            GroupMapper.transform(group, response.tenantId, getAppUserId())
                    }

                    val from =
                        ThreadMapper.from(response, getChatUserId(), getAppUserId())
                    val singleChat = getChatRow(
                        response,
                        from,
                        preferenceManager,
                        db.ekeyDao(),
                        msgId = msgId
                    )
                    if (chatType == ChatEventType.INCOMING) {
                        //increasing the unreadCount of a thread
                        val unreadCount =
                            db.threadDao().getThreadByIdInSync(threadId).unReadCount + 1
                        db.threadDao().updateUnreadCount(threadId, unreadCount)
                    }

                    db.singleChatDao().insertWithAbort(singleChat)
                    publishStatus(response)

                    d("thread_debug ###", "messageOn : SingleChat : onNext")
                    e.onNext(
                        transform(
                            singleChat = singleChat,
                            senderRecord = senderUserRecord,
                            groupRecord = groupRecord,
                            chatEventType = chatType,
                            isSilent = response.isSilent
                        )
                    )
                }
            } else {
                if (response.thread.threadType == ChatType.SINGLE.type) { // create thread & then message table
                    val chatUserId = getChatUserId()
                    val appUserId = getAppUserId()
                    var recipientChatUserId: String? = null
                    var recipientAppUserId: String? = null
                    for (eRTCChatUserId in response.thread.participants) {
                        if (chatUserId != eRTCChatUserId.user) {
                            recipientAppUserId = eRTCChatUserId.appUserId
                            recipientChatUserId = eRTCChatUserId.user
                        }
                    }

                    val from =
                        ThreadMapper.from(
                            response,
                            chatUserId,
                            appUserId,
                            recipientChatUserId,
                            recipientAppUserId
                        )
                    // from.setMessageCount(from.getMessageCount()+1);
                    if (chatType == ChatEventType.INCOMING) {
                        from.unReadCount = 1
                    }
                    db.threadDao().insertWithReplace(from)
                    db.threadUserLinkDao()
                        .insertWithReplace(
                            ThreadMapper.from(
                                appUserId, recipientAppUserId,
                                response.thread.threadId
                            )
                        )
                    // update user info
                    val senderUserRecord =
                        UserRecord(id = response.sender.appUserId, name = response.sender.name)
                    db.userDao()
                        .updateUserChatId(response.sender.appUserId, response.sender.eRTCUserId)
                    // adding receiver side key
                    val keyDetails = response.senderKeyDetails
                    if (keyDetails != null) {
                        val eKeyTable1 = E2EMapper.getEKeyFromChatTopicResponse(response)
                        val updatedRow: Int = db.ekeyDao().updateKey(
                            eKeyTable1.ertcUserId,
                            eKeyTable1.publicKey,
                            eKeyTable1.keyId,
                            eKeyTable1.deviceId,
                            System.currentTimeMillis()
                        )

                        if (updatedRow == 0) {
                            val eKeyTableUpdated = EKeyTable(
                                keyId = eKeyTable1.keyId,
                                deviceId = eKeyTable1.deviceId,
                                publicKey = eKeyTable1.publicKey,
                                privateKey = "",
                                ertcUserId = eKeyTable1.ertcUserId,
                                tenantId = response.tenantId
                            )
                            db.ekeyDao().save(eKeyTableUpdated)
                        }
                    }
                    val singleChat =
                        getChatRow(response, from, preferenceManager, db.ekeyDao(), msgId = msgId)
                    db.singleChatDao().insertWithAbort(singleChat)
                    publishStatus(response)

                    e.onNext(
                        transform(
                            singleChat = singleChat,
                            senderRecord = senderUserRecord,
                            chatEventType = chatType,
                            isSilent = response.isSilent
                        )
                    )
                } else {
                    dataManager.network()
                        .api()
                        .getGroupByThreadId(
                            response.tenantId, getChatUserId()!!,
                            response.thread.threadId
                        ).map { groupResponse: GroupResponse ->
                            val groupRecord = GroupMapper.transform(
                                groupResponse, response.tenantId, dataManager.db(),
                                preferenceManager.chatServer!!,
                                getAppUserId(), getChatUserId()
                            )
                            // save group into db
                            db.groupDao().insertWithReplace(GroupMapper.transform(groupRecord))
                            val thread =
                                ThreadMapper.fromGroupResponse(
                                    groupResponse,
                                    getAppUserId(),
                                    getChatUserId()
                                )
                            if (chatType == ChatEventType.INCOMING) {
                                thread.unReadCount = 1
                            }
                            db.threadDao().insertWithReplace(thread)
                            val link = ThreadMapper.from(
                                getAppUserId(), groupResponse.groupId,
                                groupResponse.threadId
                            )
                            db.threadUserLinkDao().insertWithReplace(link)
                            val chatRow =
                                getChatRow(
                                    response,
                                    thread,
                                    preferenceManager,
                                    db.ekeyDao(),
                                    msgId = msgId
                                )
                            db.singleChatDao().insertWithAbort(chatRow)

                            // save e2e keys when e2e feature is enabled
                            if (groupResponse.participants != null && isE2EFeatureEnabled()) {
                                for (participant in groupResponse.participants) {
                                    if (!participant.e2eEncryptionKeys.isNullOrEmpty()) {
                                        val eKeyDao = dataManager.db().ekeyDao()
                                        for (e2eKey in participant.e2eEncryptionKeys!!) {
                                            if (e2eKey.deviceId == dataManager.preference().deviceId) {

                                            } else {
                                                //update Remaining keys
                                                val updatedRow: Int = eKeyDao.updateKey(
                                                    e2eKey.eRTCUserId,
                                                    e2eKey.publicKey,
                                                    e2eKey.keyId,
                                                    e2eKey.deviceId,
                                                    System.currentTimeMillis()
                                                )

                                                if (updatedRow == 0) {
                                                    val eKeyTableUpdated = EKeyTable(
                                                        keyId = e2eKey.keyId,
                                                        deviceId = e2eKey.deviceId,
                                                        publicKey = e2eKey.publicKey,
                                                        privateKey = "",
                                                        ertcUserId = e2eKey.eRTCUserId,
                                                        tenantId = preferenceManager.tenantId
                                                    )
                                                    eKeyDao.save(eKeyTableUpdated)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            return@map chatRow

                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            var senderUserRecord: UserRecord? = null
                            if (response.thread.threadType == ChatType.GROUP.type) {
                                senderUserRecord = UserMapper.transform(
                                    db.userDao().getUserByIdInSync(
                                        response.tenantId, response.sender.appUserId
                                    )
                                )
                            }

                            val group = db.groupDao()
                                .getGroupByThreadId(response.tenantId, response.thread.threadId)
                            val groupRecord =
                                GroupMapper.transform(group, response.tenantId, getAppUserId())
                            e.onNext(
                                transform(
                                    singleChat = it,
                                    senderRecord = senderUserRecord,
                                    groupRecord = groupRecord,
                                    chatEventType = chatType,
                                    isSilent = response.isSilent
                                )
                            )
                        }
                        ) { it.printStackTrace() }
                }
            }
        }.doOnError { error -> i("messageOn", "error: " + error.message) }
            .subscribeOn(Schedulers.io())
    }

    private fun publishStatus(response: ChatTopicResponse) {
        if (getAppUserId() == response.sender.appUserId) return

        publishStatus(
            response.tenantId, response.msgUniqueId,
            response.sender.eRTCUserId, response.thread.threadId, getChatUserId(),
            MessageStatus.DELIVERED.status, response.replyThreadFeatureData,
            response.forwardChatFeatureData
        )
    }

    private fun publishStatus(
        tenantId: String, msgUniqueId: String?, recipientERTCUserId: String,
        threadId: String?, chatUserId: String?, status: String, replyThread: ReplyThread? = null,
        forwardChat: ForwardChat? = null, deviceId: String? = preferenceManager.deviceId
    ) {
        val messageReadStatus = MessageReadStatus(
            tenantId = tenantId,
            msgUniqueId = msgUniqueId!!,
            sendereRTCUserId = recipientERTCUserId,
            threadId = threadId,
            eRTCUserId = chatUserId!!,
            msgStatusEvent = status,
            timeStamp = System.currentTimeMillis(),
            replyThreadFeatureData = replyThread,
            forwardChatFeatureData = forwardChat,
            deviceId = deviceId
        )
        i("publishing status", "====>$messageReadStatus")
        try {
            mqttManager.publish(
                Constants.MSG_READ_STATUS_TOPIC,
                gson.toJson(messageReadStatus)
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun msgReadStatus(receivedMessage: ReceivedMessage): Observable<MessageRecord> {
        return Observable.create { e: ObservableEmitter<MessageRecord> ->
            d("thread_debug ###", "msgRead status initiated...")
            val status = gson.fromJson(
                receivedMessage.message,
                MessageReadStatus::class.java
            )
            d("thread_debug ###", "record : $status")
            val reactionDao = dataManager.db().chatReactionDao()

            if (getChatUserId() == status.eRTCUserId) {
                d("thread_debug ###", "ignoring : unrelated status push from server")
                e.onComplete()
                return@create
            }

            val msgStatusEvent = status.msgStatusEvent
            if (status.replyThreadFeatureData != null &&
                status.replyThreadFeatureData!!.baseMsgUniqueId.isNotEmpty()
            ) {

                val chatThreadDao = db.chatThreadDao()
                val chatThreadList =
                    chatThreadDao.hasMessage(status.msgUniqueId, msgStatusEvent)
                if (chatThreadList != null && chatThreadList.size > 0) {
                    e.onComplete()
                    return@create
                }

                if (status.replyThreadFeatureData!!.replyMsgConfig == 1) {

                    Observable.create { e: ObservableEmitter<MessageRecord> ->
                        val singleChatDao = db.singleChatDao()
                        val singleChat =
                            singleChatDao.getChatByServerMsgId(status.msgUniqueId, status.threadId)
                        d("thread_debug ###", "singleChat : $singleChat")
                        val eventType = if (singleChat.senderAppUserId != getAppUserId()) {
                            ChatEventType.INCOMING
                        } else {
                            ChatEventType.OUTGOING
                        }
                        if (singleChat.status != MessageStatus.SEEN.status) {
                            singleChat.status = msgStatusEvent
                        }
                        singleChatDao.update(singleChat)
                        val reactionsFromDatabase =
                            reactionDao.getAllReactions(singleChat.id).blockingGet()
                        val reactionRecords = composeReactionList(reactionsFromDatabase)
                        e.onNext(
                            transform(
                                singleChat = singleChat,
                                chatEventType = eventType,
                                chatReactions = reactionRecords
                            )
                        )
                    }.subscribeOn(Schedulers.io()).doOnNext { it?.let { it1 -> e.onNext(it1) } }
                        .subscribe()

                }
                Observable.create { e: ObservableEmitter<MessageRecord> ->
                    val chatThread = chatThreadDao
                        .getChatByServerMsgId(status.msgUniqueId, status.threadId)
                    d("thread_debug ###", "chatThread : $chatThread")
                    val eventType = if (chatThread.senderAppUserId != getAppUserId()) {
                        ChatEventType.INCOMING
                    } else {
                        ChatEventType.OUTGOING
                    }
                    if (chatThread.status != MessageStatus.SEEN.status) {
                        chatThread.status = msgStatusEvent
                    }
                    db.chatThreadDao().update(chatThread)
                    val reactionsFromDatabase =
                        reactionDao.getAllThreadReactions(chatThread.id).blockingGet()
                    val reactionRecords = composeReactionList(reactionsFromDatabase)
                    e.onNext(
                        transform(
                            chatThread = chatThread,
                            chatEventType = eventType,
                            chatReactions = reactionRecords
                        )
                    )
                }.subscribeOn(Schedulers.io()).doOnNext { it?.let { it1 -> e.onNext(it1) } }
                    .subscribe()

            } else {

                val singleChatDao = db.singleChatDao()
                val singleChatList =
                    singleChatDao.hasMessage(status.threadId, status.msgUniqueId, msgStatusEvent)
                if (singleChatList != null && singleChatList.size > 0) {
                    e.onComplete()
                    return@create
                }

                val singleChatEmbedded =
                    singleChatDao.getSingleChatByServerMsgId(status.msgUniqueId, status.threadId)
                singleChatEmbedded.singleChat?.let { singleChat ->
                    val eventType = if (singleChat.senderAppUserId != getAppUserId()) {
                        ChatEventType.INCOMING
                    } else {
                        ChatEventType.OUTGOING
                    }
                    if (singleChat.status != MessageStatus.SEEN.status) {
                        singleChat.status = msgStatusEvent
                    }
                    singleChatDao.update(singleChat)
                    val reactionsFromDatabase =
                        reactionDao.getAllReactions(singleChat.id).blockingGet()
                    val reactionRecords = composeReactionList(reactionsFromDatabase)
                    e.onNext(
                        transform(
                            singleChat = singleChat,
                            chatThreadList = singleChatEmbedded.listChatThread,
                            chatEventType = eventType,
                            chatReactions = reactionRecords
                        )
                    )
                }

            }

        }.subscribeOn(Schedulers.io())
    }

    fun onChatUpdate(receivedMessage: ReceivedMessage): Observable<MessageRecord> {
        return Observable.create { e: ObservableEmitter<MessageRecord> ->
            val status = gson.fromJson(receivedMessage.message, ChatUpdateResponse::class.java)
            val chatUserId = getChatUserId()
            val reactionDao = dataManager.db().chatReactionDao()
            val chatThreadDao = db.chatThreadDao()
            var threadId = status.threadId

            status.chats?.forEach { it ->

                if (it.replyThreadFeatureData != null) {
                    if (it.replyThreadFeatureData!!.replyMsgConfig == 1) {
                        val singleChatDao = db.singleChatDao()
                        val singleChat =
                            singleChatDao.getChatByServerMsgId(it.msgUniqueId, threadId)
                        d("thread_debug ###", "singleChat : $singleChat")
                        var senderUserRecord: UserRecord? = null
                        if (singleChat.type.equals(ChatType.GROUP.type)) {
                            senderUserRecord = UserMapper.transform(
                                db.userDao()
                                    .getUserByIdInSync(getTenantId()!!, singleChat.senderAppUserId)
                            )
                        }

                        if (status.updateType == DELETE) {
                            singleChat.message = THIS_MESSAGE_WAS_DELETED
                            singleChat.deleteType = DeleteType.DELETE_FOR_EVERYONE.type
                            singleChat.updateType = status.updateType
                            singleChat.isStarredChat = "0"
                            status.deleteType?.let {
                                if (it == SELF) {
                                    singleChat.message = THIS_MESSAGE_WAS_DELETED_FOR_YOU
                                    singleChat.deleteType = DeleteType.DELETE_FOR_USER.type
                                }
                            }

                            singleChat.mediaPath = ""
                            singleChat.localMediaPath = ""
                            singleChat.mediaThumbnail = ""
                            reactionDao.clearAllUserReaction(singleChat.id, chatUserId)
                        }

                        val eventType = if (singleChat.senderAppUserId != getAppUserId()) {
                            ChatEventType.INCOMING
                        } else {
                            ChatEventType.OUTGOING
                        }

                        if (it.message == null && status.updateType != DELETE) {
                            if (singleChat.updateType != null && singleChat.updateType == MessageUpdateType.EDIT.type) {
                                singleChat.updateType = status.updateType
                            } else {
                                singleChat.updateType = MessageUpdateType.FAVORITE.type
                            }
                            it.isStarred?.let { isStarred ->
                                singleChat.isStarredChat = if (isStarred) "1" else "0"
                            }
                        } else {
                            singleChat.updateType = updateType(status.updateType)
                            it.message?.let { message ->
                                if (isE2EFeatureEnabled()) {
                                    val myKeyTable =
                                        dataManager.db().ekeyDao().getPrivateKeyByKeyId(
                                            Objects.requireNonNull(it).eRTCUserId,
                                            it.deviceId,
                                            getTenantId(),
                                            it.keyId
                                        )
                                    val privateKey = myKeyTable?.privateKey
                                    val publicKey =
                                        Objects.requireNonNull(status.senderKeyDetails)?.publicKey
                                    val msg =
                                        ECDHUtils.decrypt(
                                            Objects.requireNonNull(message),
                                            publicKey,
                                            privateKey
                                        )
                                    singleChat.message = msg
                                } else {
                                    singleChat.message = message
                                }
                            }
                        }
                        singleChatDao.update(singleChat)
                        val reactionsFromDatabase =
                            reactionDao.getAllReactions(singleChat.id).blockingGet()
                        val reactionRecords = composeReactionList(reactionsFromDatabase)
                        e.onNext(
                            transform(
                                singleChat = singleChat,
                                chatEventType = eventType,
                                chatReactions = reactionRecords,
                                senderRecord = senderUserRecord
                            )
                        )
                    }

                    //Chat thread message update
                    val chatThread = chatThreadDao.getChatByServerMsgId(it.msgUniqueId, threadId)
                    var senderUserRecord: UserRecord? = null
                    if (chatThread.type.equals(ChatType.GROUP_CHAT_THREAD.type)) {
                        senderUserRecord = UserMapper.transform(
                            db.userDao()
                                .getUserByIdInSync(getTenantId()!!, chatThread.senderAppUserId)
                        )
                    }
                    val eventType = if (chatThread.senderAppUserId != getAppUserId()) {
                        ChatEventType.INCOMING
                    } else {
                        ChatEventType.OUTGOING
                    }
                    if (status.updateType == DELETE) {
                        chatThread.message = THIS_MESSAGE_WAS_DELETED
                        chatThread.deleteType = DeleteType.DELETE_FOR_EVERYONE.type
                        chatThread.updateType = status.updateType
                        chatThread.isStarredChat = "0"
                        status.deleteType?.let {
                            if (it == SELF) {
                                chatThread.message = THIS_MESSAGE_WAS_DELETED_FOR_YOU
                                chatThread.deleteType = DeleteType.DELETE_FOR_USER.type
                            }
                        }

                        chatThread.mediaPath = ""
                        chatThread.localMediaPath = ""
                        chatThread.mediaThumbnail = ""
                        reactionDao.clearAllUserReaction(chatThread.id, chatUserId)
                    }

                    if (it.message == null && status.updateType != DELETE) {
                        if (chatThread.updateType != null && chatThread.updateType == MessageUpdateType.EDIT.type) {
                            chatThread.updateType = status.updateType
                        } else {
                            chatThread.updateType = MessageUpdateType.FAVORITE.type
                        }
                        it.isStarred?.let { isStarred ->
                            chatThread.isStarredChat = if (isStarred) "1" else "0"
                        }
                    } else {
                        chatThread.updateType = updateType(status.updateType)
                        it.message?.let { message ->
                            if (isE2EFeatureEnabled()) {
                                val myKeyTable = dataManager.db().ekeyDao().getPrivateKeyByKeyId(
                                    Objects.requireNonNull(it).eRTCUserId,
                                    it.deviceId,
                                    getTenantId(),
                                    it.keyId
                                )
                                val privateKey = myKeyTable?.privateKey
                                val publicKey =
                                    Objects.requireNonNull(status.senderKeyDetails)?.publicKey
                                val msg =
                                    ECDHUtils.decrypt(
                                        Objects.requireNonNull(message),
                                        publicKey,
                                        privateKey
                                    )
                                chatThread.message = msg
                            } else {
                                chatThread.message = message
                            }
                        }
                    }
                    chatThreadDao.update(chatThread)
                    val reactionsFromDatabase =
                        reactionDao.getAllThreadReactions(chatThread.id).blockingGet()
                    val reactionRecords = composeReactionList(reactionsFromDatabase)
                    e.onNext(
                        transform(
                            chatThread = chatThread,
                            chatEventType = eventType,
                            chatReactions = reactionRecords,
                            senderRecord = senderUserRecord
                        )
                    )
                } else {
                    val singleChatDao = db.singleChatDao()
                    val singleChatEmbedded =
                        singleChatDao.getSingleChatByServerMsgId(it.msgUniqueId, threadId)
                    singleChatEmbedded.singleChat?.let { singleChat ->
                        var senderUserRecord: UserRecord? = null
                        if (singleChat.type.equals(ChatType.GROUP.type)) {
                            senderUserRecord = UserMapper.transform(
                                db.userDao()
                                    .getUserByIdInSync(getTenantId()!!, singleChat.senderAppUserId)
                            )
                        }

                        val eventType = if (singleChat.senderAppUserId != getAppUserId()) {
                            ChatEventType.INCOMING
                        } else {
                            ChatEventType.OUTGOING
                        }
                        if (status.updateType == DELETE) {
                            singleChat.message = THIS_MESSAGE_WAS_DELETED
                            singleChat.deleteType = DeleteType.DELETE_FOR_EVERYONE.type
                            singleChat.updateType = status.updateType
                            singleChat.isStarredChat = "0"
                            status.deleteType?.let {
                                if (it == SELF) {
                                    singleChat.message = THIS_MESSAGE_WAS_DELETED_FOR_YOU
                                    singleChat.deleteType = DeleteType.DELETE_FOR_USER.type
                                }
                            }

                            singleChat.mediaPath = ""
                            singleChat.localMediaPath = ""
                            singleChat.mediaThumbnail = ""
                            reactionDao.clearAllUserReaction(singleChat.id, chatUserId)
                        }

                        if (it.message == null && status.updateType != DELETE) {
                            if (singleChat.updateType != null && singleChat.updateType == MessageUpdateType.EDIT.type) {
                                singleChat.updateType = status.updateType
                            } else {
                                singleChat.updateType = MessageUpdateType.FAVORITE.type
                            }
                            it.isStarred?.let { isStarred ->
                                singleChat.isStarredChat = if (isStarred) "1" else "0"
                            }
                        } else {
                            singleChat.updateType = updateType(status.updateType)
                            it.message?.let { message ->
                                if (isE2EFeatureEnabled()) {
                                    val myKeyTable =
                                        dataManager.db().ekeyDao().getPrivateKeyByKeyId(
                                            Objects.requireNonNull(it).eRTCUserId,
                                            it.deviceId,
                                            getTenantId(),
                                            it.keyId
                                        )
                                    val privateKey = myKeyTable?.privateKey
                                    val publicKey =
                                        Objects.requireNonNull(status.senderKeyDetails)?.publicKey
                                    val msg =
                                        ECDHUtils.decrypt(
                                            Objects.requireNonNull(message),
                                            publicKey,
                                            privateKey
                                        )
                                    singleChat.message = msg
                                } else {
                                    singleChat.message = message
                                }
                            }
                        }
                        singleChatDao.update(singleChat)
                        val reactionsFromDatabase =
                            reactionDao.getAllReactions(singleChat.id).blockingGet()
                        val reactionRecords = composeReactionList(reactionsFromDatabase)
                        e.onNext(
                            transform(
                                singleChat = singleChat,
                                chatThreadList = singleChatEmbedded.listChatThread,
                                chatEventType = eventType,
                                chatReactions = reactionRecords,
                                senderRecord = senderUserRecord
                            )
                        )
                    }
                }
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun updateType(updateType: String?): String? {
        var messageUpdateType: MessageUpdateType? = null
        if (updateType == DELETE) {
            messageUpdateType = MessageUpdateType.DELETE
        } else if (updateType == EDIT) {
            messageUpdateType = MessageUpdateType.EDIT
        }
        return messageUpdateType?.type
    }

    fun markAsRead(threadId: String?, appUserId: String?, parentMsgId: String?): Completable {
        return Completable.create { e: CompletableEmitter ->
            if (parentMsgId != null && parentMsgId.isNotEmpty()) {
                val chatThreadDao = db.chatThreadDao()
                val thread =
                    db.threadDao().getThreadByIdInSync(threadId)
                val deliveredMessages =
                    chatThreadDao.getDeliveredMessages(
                        parentMsgId,
                        MessageStatus.DELIVERED.status,
                        getAppUserId()
                    )
                if (deliveredMessages != null) {
                    for (chatThread in deliveredMessages) {
                        val status = MessageStatus.SEEN.status

                        /// recipient chat Id and sender chat Id
                        // reply thread info needs to be part of message status
                        // if reply-config = 1 then seen needs to be sent to both main and chat thread
                        publishStatus(
                            thread.tenantId, chatThread.msgUniqueId,
                            thread.recipientChatId, threadId, thread.senderChatId, status,
                            chatThread.msgUniqueId?.let {
                                ReplyThread(
                                    it,
                                    chatThread.sendToChannel
                                )
                            }
                        )
                        chatThreadDao.setStatus(status, chatThread.id)
                    }
                    db.threadDao().updateUnreadCount(threadId, 0)
                }
            } else {
                val thread =
                    db.threadDao().getThreadByIdInSync(threadId)
                val singleChatDao = db.singleChatDao()
                val deliveredMessages =
                    singleChatDao.getDeliveredMessages(
                        threadId,
                        MessageStatus.DELIVERED.status,
                        getAppUserId()
                    )
                if (deliveredMessages != null) {
                    for (singleChat in deliveredMessages) {
                        if (singleChat.senderAppUserId == getAppUserId()) continue

                        val status = MessageStatus.SEEN.status
                        publishStatus(
                            thread.tenantId, singleChat.msgUniqueId,
                            thread.recipientChatId, thread.id, thread.senderChatId, status
                        )
                        singleChatDao.setStatus(status, singleChat.id)
                    }
                }
                db.threadDao().updateUnreadCount(threadId, 0)
            }
            e.onComplete()
        }.subscribeOn(Schedulers.io())
    }

    fun groupUpdated(receivedMessage: ReceivedMessage): Observable<GroupRecord> {
        return Observable.create { e: ObservableEmitter<GroupRecord> ->
            val groupUpdate = gson.fromJson(
                receivedMessage.message,
                GroupUpdate::class.java
            )

            groupUpdate.eventList?.let {
                if (it.isNotEmpty() && it[0].eventType.equals(GroupRecord.STATUS_DELETED)) {
                    dataManager.db().groupDao()
                        .updateGroupStatus(groupUpdate.groupId, GroupRecord.STATUS_DELETED)
                    val group = db.groupDao().getGroupById(groupUpdate.groupId)
                    e.onNext(GroupMapper.transform(group, group.tenantId, getAppUserId()))
                    db.groupDao().deleteByGroupId(groupUpdate.groupId)
                    db.chatReactionDao().deleteByThreadId(groupUpdate.threadId)
                    db.chatThreadDao().deleteByThreadId(groupUpdate.threadId)
                    db.singleChatDao().deleteByThreadId(groupUpdate.threadId)
                    db.threadDao().deleteThreadById(groupUpdate.threadId)
                    db.threadUserLinkDao().deleteThreadById(groupUpdate.threadId)
                    return@create
                } else if (it.isNotEmpty() && it[0].eventType.equals(GroupRecord.STATUS_DEACTIVATED)) {
                    dataManager.db().groupDao()
                        .updateGroupStatus(groupUpdate.groupId, GroupRecord.STATUS_DEACTIVATED)
                    val group = db.groupDao().getGroupById(groupUpdate.groupId)
                    e.onNext(GroupMapper.transform(group, group.tenantId, getAppUserId()))
                    db.groupDao().deleteByGroupId(groupUpdate.groupId)
                    db.chatReactionDao().deleteByThreadId(groupUpdate.threadId)
                    db.chatThreadDao().deleteByThreadId(groupUpdate.threadId)
                    db.singleChatDao().deleteByThreadId(groupUpdate.threadId)
                    db.threadDao().deleteThreadById(groupUpdate.threadId)
                    db.threadUserLinkDao().deleteThreadById(groupUpdate.threadId)
                    return@create
                }
            }

            // profilePicChanged, descriptionChanged, nameChanged, profilePicRemoved, participantsRemoved, participantsAdded
            preferenceManager.chatUserId?.let { it ->
                preferenceManager.tenantId?.let { it1 ->
                    groupUpdate.groupId?.let { it2 ->
                        dataManager.network()
                            .api()
                            .getGroup(it1, it, it2)
                            .map { groupResponse: GroupResponse ->
                                val groupRecord = GroupMapper.transform(
                                    groupResponse,
                                    preferenceManager.tenantId,
                                    dataManager.db(),
                                    dataManager.preference().chatServer!!,
                                    getAppUserId(),
                                    getChatUserId()
                                )
                                // save group into db
                                dataManager.db().groupDao()
                                    .insertWithReplace(GroupMapper.transform(groupRecord))
                                e.onNext(groupRecord)

                                val threadId = groupResponse.threadId
                                val threadUserLinks = db.threadUserLinkDao().hasThread(threadId)

                                if (threadUserLinks.isNullOrEmpty()) {
                                    val thread =
                                        ThreadMapper.fromGroupResponse(
                                            groupResponse,
                                            getAppUserId(),
                                            getChatUserId()
                                        )

                                    db.threadDao().insertWithReplace(thread)
                                    val link = ThreadMapper.from(
                                        getAppUserId(), groupResponse.groupId,
                                        groupResponse.threadId
                                    )
                                    db.threadUserLinkDao().insertWithReplace(link)
                                }

                                // save e2e keys when e2e feature is enabled
                                if (groupResponse.participants != null && isE2EFeatureEnabled()) {
                                    for (participant in groupResponse.participants) {
                                        if (!participant.e2eEncryptionKeys.isNullOrEmpty()) {
                                            val eKeyDao = dataManager.db().ekeyDao()
                                            for (e2eKey in participant.e2eEncryptionKeys!!) {
                                                if (e2eKey.deviceId == dataManager.preference().deviceId) {

                                                } else {
                                                    //update Remaining keys
                                                    val updatedRow: Int = eKeyDao.updateKey(
                                                        e2eKey.eRTCUserId,
                                                        e2eKey.publicKey,
                                                        e2eKey.keyId,
                                                        e2eKey.deviceId,
                                                        System.currentTimeMillis()
                                                    )

                                                    if (updatedRow == 0) {
                                                        val eKeyTableUpdated = EKeyTable(
                                                            keyId = e2eKey.keyId,
                                                            deviceId = e2eKey.deviceId,
                                                            publicKey = e2eKey.publicKey,
                                                            privateKey = "",
                                                            ertcUserId = e2eKey.eRTCUserId,
                                                            tenantId = preferenceManager.tenantId
                                                        )
                                                        eKeyDao.save(eKeyTableUpdated)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            .subscribeOn(Schedulers.io())
                            .subscribe()
                    }
                }
            }
        }.subscribeOn(Schedulers.io())
    }

    fun groupUpdatedMessage(receivedMessage: ReceivedMessage): Observable<MessageRecord> {
        return Observable.create { e: ObservableEmitter<MessageRecord> ->
            val groupUpdate = gson.fromJson(
                receivedMessage.message,
                GroupUpdate::class.java
            )

            groupUpdate.eventList?.let {
                if (it.isNotEmpty() && it[0].eventType.equals(GroupRecord.STATUS_DELETED)) {
                    return@create
                } else if (it.isNotEmpty() && (it[0].eventType.equals("frozen") || it[0].eventType.equals(
                        "unfrozen"
                    ))
                ) {
                    return@create
                } else if (it.isNotEmpty() && (it[0].eventType.equals("activated") || it[0].eventType.equals(
                        "deactivated"
                    ))
                ) {
                    return@create
                }
            }

            val threadId = groupUpdate.threadId
            val group =
                preferenceManager.tenantId?.let { db.groupDao().getGroupByThreadId(it, threadId) }

            //If group is not there then we need to add first
            if (group == null) {
                getChatUserId()?.let { it ->
                    preferenceManager.tenantId?.let { it1 ->
                        groupUpdate.groupId?.let { it2 ->
                            dataManager.network()
                                .api()
                                .getGroup(it1, it, it2)
                                .map { groupResponse: GroupResponse ->
                                    val groupRecord = GroupMapper.transform(
                                        groupResponse,
                                        preferenceManager.tenantId,
                                        dataManager.db(),
                                        dataManager.preference().chatServer!!,
                                        getAppUserId(),
                                        getChatUserId()
                                    )
                                    // save group into db
                                    dataManager.db().groupDao()
                                        .insertWithReplace(GroupMapper.transform(groupRecord))

                                    val threadUserLinks =
                                        threadId?.let { db.threadUserLinkDao().hasThread(it) }

                                    if (threadUserLinks.isNullOrEmpty()) {
                                        val thread =
                                            ThreadMapper.fromGroupResponse(
                                                groupResponse,
                                                getAppUserId(),
                                                getChatUserId()
                                            )
                                        db.threadDao().insertWithReplace(thread)
                                        val link = ThreadMapper.from(
                                            getAppUserId(), groupResponse.groupId,
                                            groupResponse.threadId
                                        )
                                        db.threadUserLinkDao().insertWithReplace(link)
                                    }

                                    val thread = db.threadDao().getThreadByIdInSync(threadId)

                                    val eventUser: String? =
                                        groupUpdate.eventTriggeredByUser?.appUserId
                                    val chatEventType =
                                        if (getAppUserId().equals(groupUpdate.eventTriggeredByUser?.appUserId)) {
                                            ChatEventType.OUTGOING
                                        } else {
                                            ChatEventType.INCOMING
                                        }

                                    if (!TextUtils.isEmpty(eventUser)) {
                                        for (event: Event in groupUpdate.eventList!!) {
                                            val chatRow = event.eventType?.let { _ ->
                                                getGroupInfoChatRow(
                                                    thread,
                                                    eventUser,
                                                    chatEventType,
                                                    groupUpdate,
                                                    event,
                                                    db.userDao(),
                                                    getChatUserId()
                                                )
                                            }

                                            d(
                                                "ChatWrapper",
                                                "if ==> chatRow message: " + chatRow?.message?.trim()
                                            )
                                            val lastMessage = db.singleChatDao()
                                                .getLastGroupEventMessage(
                                                    ChatEventType.CHAT_META_DATA.type,
                                                    thread.id
                                                )
                                            d("ChatWrapper", "if ==> DB last message: $lastMessage")
                                            if (lastMessage.isNullOrEmpty() || lastMessage?.trim() != chatRow?.message?.trim()) {
                                                db.singleChatDao().insertWithReplace(chatRow)
                                                chatRow?.let {
                                                    transform(
                                                        singleChat = it,
                                                        chatEventType = ChatEventType.CHAT_META_DATA
                                                    )
                                                }?.let {
                                                    e.onNext(
                                                        it
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // save e2e keys when e2e feature is enabled
                                    if (groupResponse.participants != null && isE2EFeatureEnabled()) {
                                        for (participant in groupResponse.participants) {
                                            if (!participant.e2eEncryptionKeys.isNullOrEmpty()) {
                                                val eKeyDao = dataManager.db().ekeyDao()
                                                for (e2eKey in participant.e2eEncryptionKeys!!) {
                                                    if (e2eKey.deviceId == dataManager.preference().deviceId) {

                                                    } else {
                                                        //update Remaining keys
                                                        val updatedRow: Int = eKeyDao.updateKey(
                                                            e2eKey.eRTCUserId,
                                                            e2eKey.publicKey,
                                                            e2eKey.keyId,
                                                            e2eKey.deviceId,
                                                            System.currentTimeMillis()
                                                        )

                                                        if (updatedRow == 0) {
                                                            val eKeyTableUpdated = EKeyTable(
                                                                keyId = e2eKey.keyId,
                                                                deviceId = e2eKey.deviceId,
                                                                publicKey = e2eKey.publicKey,
                                                                privateKey = "",
                                                                ertcUserId = e2eKey.eRTCUserId,
                                                                tenantId = preferenceManager.tenantId
                                                            )
                                                            eKeyDao.save(eKeyTableUpdated)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                .subscribeOn(Schedulers.io())
                                .subscribe()
                        }
                    }
                }
            } else {
                val thread = db.threadDao().getThreadByIdInSync(threadId)

                val eventUser: String? = groupUpdate.eventTriggeredByUser?.appUserId
                val chatEventType =
                    if (getAppUserId().equals(groupUpdate.eventTriggeredByUser?.appUserId)) {
                        ChatEventType.OUTGOING
                    } else {
                        ChatEventType.INCOMING
                    }

                if (!TextUtils.isEmpty(eventUser)) {
                    for (event: Event in groupUpdate.eventList!!) {
                        val chatRow = event.eventType?.let { _ ->
                            getGroupInfoChatRow(
                                thread, eventUser, chatEventType,
                                groupUpdate, event, db.userDao(), getChatUserId()
                            )
                        }

                        d("ChatWrapper", "else ==> chatRow message: " + chatRow?.message?.trim())
                        val lastMessage = db.singleChatDao()
                            .getLastGroupEventMessage(ChatEventType.CHAT_META_DATA.type, thread.id)
                        d("ChatWrapper", "else ==> DB last message: $lastMessage")
                        if (lastMessage.isNullOrEmpty() || lastMessage?.trim() != chatRow?.message?.trim()) {
                            db.singleChatDao().insertWithReplace(chatRow)
                            chatRow?.let {
                                transform(
                                    singleChat = it,
                                    chatEventType = ChatEventType.CHAT_META_DATA
                                )
                            }?.let {
                                e.onNext(
                                    it
                                )
                            }
                        }
                    }
                }
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun getParentMessage(singleChat: SingleChat?): String {
        if (singleChat?.message != null) {
            return singleChat.message!!
        } else if (singleChat?.mediaPath != null) {
            return if (singleChat.mediaName != null) {
                singleChat.mediaName!!
            } else {
                singleChat.mediaPath!!.substring(singleChat.mediaPath!!.lastIndexOf('/') + 1)
            }
        } else if (singleChat?.gifPath != null) {
            return singleChat.gifPath!!.substring(singleChat.gifPath!!.lastIndexOf('/') + 1)
        } else if (singleChat?.contactName != null) {
            return singleChat.contactName!!
        } else if (singleChat?.location != null && singleChat.location!!.address != null) {
            return singleChat.location!!.address!!
        } else {
            return singleChat?.message!!
        }
    }

    fun download(msgId: String?, localPath: String?, status: Status): Observable<MessageRecord> {
        return Observable.create { e: ObservableEmitter<MessageRecord> ->

            var singleChatEmbedded: SingleChatEmbedded? = null
            var chatThread: ChatThread? = null
            if (dataManager.db().singleChatDao().hasMessage(msgId).size > 0) {
                singleChatEmbedded = dataManager.db().singleChatDao().getSingleChatByMsgId(msgId)
            } else if (dataManager.db().chatThreadDao().hasMessage(msgId).size > 0) {
                chatThread = dataManager.db().chatThreadDao().getChatByClientId(msgId)
            }

            when (status) {
                Status.START -> {
                    getDownloadMessageRecord(
                        singleChatEmbedded,
                        chatThread,
                        DownloadStatus.PROGRESS.status
                    )?.let {
                        e.onNext(
                            it
                        )
                    }
                }
                Status.COMPLETED -> {
                    getDownloadMessageRecord(
                        singleChatEmbedded,
                        chatThread,
                        DownloadStatus.SUCCESS.status,
                        localPath
                    )?.let {
                        e.onNext(
                            it
                        )
                    }
                }
                Status.FAILED -> {
                    getDownloadMessageRecord(
                        singleChatEmbedded,
                        chatThread,
                        DownloadStatus.FAILED.status
                    )?.let {
                        e.onNext(
                            it
                        )
                    }
                }
                else -> {
                    // no else handle
                    e.onComplete()
                }
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun getDownloadMessageRecord(
        singleChatEmbedded: SingleChatEmbedded?,
        chatThread: ChatThread?,
        downloadStatus: String,
        localPath: String? = ""
    ): MessageRecord? {
        val singleChat = singleChatEmbedded?.singleChat
        val dbManager = dataManager.db()
        when {
            singleChat != null -> {
                singleChat.downloadStatus = downloadStatus
                val chatEventType = if (singleChat.chatEventType == ChatEventType.OUTGOING.type) {
                    ChatEventType.OUTGOING
                } else {
                    ChatEventType.INCOMING
                }
                if (downloadStatus == DownloadStatus.SUCCESS.status) {
                    singleChat.localMediaPath = localPath
                }
                dbManager.singleChatDao().update(singleChat)

                if (singleChat.sendToChannel != null && singleChat.sendToChannel == 1) {
                    val chatThread = dbManager.chatThreadDao()
                        .getChatByServerMsgId(singleChat.msgUniqueId, singleChat.threadId)
                    chatThread.downloadStatus = downloadStatus
                    if (downloadStatus == DownloadStatus.SUCCESS.status) {
                        chatThread.localMediaPath = localPath
                    }
                    dbManager.chatThreadDao().update(chatThread)
                }
                var senderUserRecord: UserRecord? = null
                if (singleChat.type.equals(ChatType.GROUP.type)) {
                    senderUserRecord = UserMapper.transform(
                        dataManager.preference().tenantId?.let {
                            dbManager.userDao().getUserByIdInSync(
                                it, singleChat.senderAppUserId
                            )
                        })
                }

                return transform(
                    singleChat = singleChat,
                    senderRecord = senderUserRecord,
                    chatThreadList = singleChatEmbedded.listChatThread,
                    chatEventType = chatEventType
                )
            }
            chatThread != null -> {
                chatThread.downloadStatus = downloadStatus
                val chatEventType = if (chatThread.senderAppUserId != getAppUserId()) {
                    ChatEventType.INCOMING
                } else {
                    ChatEventType.OUTGOING
                }
                if (downloadStatus == DownloadStatus.SUCCESS.status) {
                    chatThread.localMediaPath = localPath
                }
                dbManager.chatThreadDao().update(chatThread)

                if (chatThread.sendToChannel == 1) {
                    val singleChat = dbManager.singleChatDao()
                        .getChatByServerMsgId(chatThread.msgUniqueId, chatThread.threadId)
                    singleChat.downloadStatus = downloadStatus
                    if (downloadStatus == DownloadStatus.SUCCESS.status) {
                        singleChat.localMediaPath = localPath
                    }
                    dbManager.singleChatDao().update(singleChat)
                }

                var recipientRecord: UserRecord? = null
                if (chatThread.type.equals(ChatType.GROUP_CHAT_THREAD.type)) {
                    recipientRecord = UserMapper.transform(
                        dataManager.preference().tenantId?.let {
                            dbManager.userDao().getUserByIdInSync(
                                it, chatThread.senderAppUserId
                            )
                        })
                }

                return transform(
                    chatThread = chatThread,
                    receiverRecord = recipientRecord,
                    chatEventType = chatEventType
                )
            }
            else -> {
                return null
            }
        }
    }

    private fun isE2EFeatureEnabled(): Boolean {
        return (dataManager.db().tenantDao().getFeatureEnabled(
            dataManager.preference().tenantId,
            io.inappchat.inappchat.utils.Constants.TenantConfig.E2E_CHAT
        ) == "1")
    }

    private fun composeReactionList(listOfReactionEntities: List<ChatReactionEntity>?): ArrayList<ChatReactionRecord> {
        val chatReactionList = arrayListOf<ChatReactionRecord>()
        val groupBy = listOfReactionEntities?.groupBy { it.unicode }
        if (groupBy != null) {
            for ((k, reactionList) in groupBy) {

                val toList = reactionList.groupBy { it.userChatId }.keys.toList()
                val userEntity = db.userDao().getUserEntityByUserChatIds(toList.toTypedArray())
                val userRecordList = arrayListOf<UserRecord>()
                userEntity.forEach {
                    userRecordList.add(UserMapper.transform(it))
                }
                chatReactionList.add(
                    ChatReactionRecord(
                        emojiCode = k,
                        count = reactionList.size,
                        userRecord = userRecordList
                    )
                )
            }
        }
        return chatReactionList
    }

    fun announcementTrigger(receivedMessage: ReceivedMessage): Observable<AnnouncementRecord> {
        return Observable.create { e: ObservableEmitter<AnnouncementRecord> ->
            val announcement =
                gson.fromJson(receivedMessage.message, AnnouncementResponse::class.java)
            e.onNext(AnnouncementMapper.transform(announcement))
        }.subscribeOn(Schedulers.io())
    }

    fun autoLogout(): Observable<Result> {
        return Observable.create { e: ObservableEmitter<Result> ->
            dataManager.db().tenantDao().deleteAll()
            dataManager.db().threadDao().deleteAll()
            dataManager.db().singleChatDao().deleteAll()
            dataManager.db().groupDao().deleteAll()
            dataManager.db().ekeyDao().deleteAll()
            dataManager.db().userDao().deleteAll()
            dataManager.mqtt().removeConnectionAndSubscription()
            FirebaseMessaging.getInstance().deleteToken()
            InAppChat.fcm().clearNotification()
            dataManager.preference().clearData()
            dataManager.db().downloadMediaDao().clear()
            e.onNext(Result(true, "You are logged out successfully.", ""))
        }.subscribeOn(Schedulers.io())
    }

    fun updateFeatures(): Observable<Result> {
        return Observable.create { e: ObservableEmitter<Result> ->
            dataManager.network()
                .api()
                .validateUrl(dataManager.db().tenantDao().all.tenant.namespace)
                .map { tenant ->
                    preferenceManager.projectType = tenant.projectType
                    val config: TenantDetailResponse.Config = tenant.config
                    val configArrayList: ArrayList<TenantConfig> = getConfigList(tenant, config)

                    dataManager.db().tenantDao().save(configArrayList)
                    e.onNext(Result(true, "", ""))
                }
                .subscribeOn(Schedulers.io())
                .subscribe()
        }.subscribeOn(Schedulers.io())
    }

    fun fetchChatSettings(receivedMessage: ReceivedMessage): Observable<Result> {
        return Observable.create { e: ObservableEmitter<Result> ->
            getTenantId()?.let { tenantId ->
                getChatUserId()?.let { eRTCUserId ->
                    dataManager.network()
                        .api()
                        .getChatSettings(tenantId, eRTCUserId)
                        .map { response: ChatSettingsResponse ->
                            dataManager.db().domainFilterDao().deleteAll()
                            dataManager.db().profanityFilterDao().deleteAll()
                            response.domainFilter?.let {
                                it.domains?.let { domains ->
                                    val domainData = arrayListOf<DomainFilter>()
                                    for (domain in domains) {
                                        domainData.add(
                                            DomainFilter(
                                                tenantId = tenantId,
                                                domain = domain,
                                                actionType = it.actionType
                                            )
                                        )
                                    }
                                    dataManager.db().domainFilterDao().insertWithReplace(domainData)
                                }
                            }

                            response.profanityFilter?.let {
                                it.keywords?.let { keywords ->
                                    val profanityData = arrayListOf<ProfanityFilter>()
                                    for (keyword in keywords) {
                                        profanityData.add(
                                            ProfanityFilter(
                                                tenantId = tenantId,
                                                keyword = keyword,
                                                actionType = it.actionType
                                            )
                                        )
                                    }
                                    dataManager.db().profanityFilterDao()
                                        .insertWithReplace(profanityData)
                                }
                            }
                            e.onNext(Result(true, "", ""))
                        }.subscribeOn(Schedulers.io())
                        .subscribe()
                }
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun getConfigList(
        tenant: TenantDetailResponse, config: TenantDetailResponse.Config
    ): ArrayList<TenantConfig> {
        val configArrayList = ArrayList<TenantConfig>()
        val features = config.features
        configArrayList.add(
            TenantConfig(
                tenant.id,
                io.inappchat.inappchat.utils.Constants.TenantConfig.CHAT_ENABLED,
                if (Objects.requireNonNull(features.chat)?.enabled!!) "1" else "0"
            )
        )
        configArrayList.add(
            TenantConfig(
                tenant.id,
                io.inappchat.inappchat.utils.Constants.TenantConfig.GROUP_ENABLED,
                if (Objects.requireNonNull(features.groupChat)?.enabled!!) "1" else "0"
            )
        )
        configArrayList.add(
            TenantConfig(
                tenant.id,
                io.inappchat.inappchat.utils.Constants.TenantConfig.TYPING_STATUS,
                if (Objects.requireNonNull(features.typingStatus)?.enabled!!) "1" else "0"
            )
        )
        val readReceipt = features.readReceipt
        if (readReceipt != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.READ_RECEIPTS,
                    if (Objects.requireNonNull(features.readReceipt)?.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ReadReceipts.SENT,
                    if (readReceipt.sendAlert!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ReadReceipts.DELIVERED,
                    if (readReceipt.deliveredAlert!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ReadReceipts.READ,
                    if (readReceipt.readAlert!!.enabled!!) "1" else "0"
                )
            )
        }
        if (features.userProfile != null) {
            val userProfile = features.userProfile
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.USER_PROFILE,
                    if (userProfile!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.UserProfile.NAME,
                    if (userProfile.userName!!.enabled != null && userProfile.userName!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.UserProfile.Name.EDITABLE,
                    if (userProfile.userName!!.editable != null && userProfile.userName!!.editable!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.UserProfile.IMAGE,
                    if (userProfile.image!!.enabled != null && userProfile.image!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.UserProfile.Image.EDITABLE,
                    if (userProfile.image!!.editable != null && userProfile.image!!.editable!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.UserProfile.FAV_CONTACTS,
                    if (userProfile.favoriteContacts!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.UserProfile.AVAILABLE_STATUS,
                    if (userProfile.availabilityStatus!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.UserProfile.AvailableStatus.ALLOW_OVERRIDING,
                    if (userProfile.availabilityStatus!!.allowOverriding!!) "1" else "0"
                )
            )
        }
        if (features.blockUser != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.BLOCK_USER,
                    if (features.blockUser!!.enabled!!) "1" else "0"
                )
            )
        }
        if (features.starredChat != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.STAR_MESSAGE,
                    if (features.starredChat!!.enabled!!) "1" else "0"
                )
            )
        }
        if (features.notificationSettings != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.NOTIFICATION,
                    if (features.notificationSettings!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.SingeChat.Notification.MUTE_SETTINGS,
                    if (features.notificationSettings!!.chatMuteSetting != null && Objects.requireNonNull(
                            features.notificationSettings!!.chatMuteSetting
                        )?.enabled!!
                    ) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.GroupChat.Notification.MUTE_SETTINGS,
                    if (features.notificationSettings!!.groupChatMuteSetting != null &&
                        features.notificationSettings!!.groupChatMuteSetting!!.enabled!!
                    ) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.UserProfile.Notification.MUTE_SETTINGS,
                    if (features.notificationSettings!!.globalMuteSetting != null &&
                        features.notificationSettings!!.globalMuteSetting!!.enabled!!
                    ) "1" else "0"
                )
            )
        }
        if (features.searchFilter != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.SEARCH_FILTER,
                    if (features.searchFilter!!.enabled!!) "1" else "0"
                )
            )
        }
        val chatAttachment = features.chatAttachment
        if (chatAttachment != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ATTACHMENT,
                    if (chatAttachment.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.SINGLE_CHAT_AUDIO,
                    if (chatAttachment.audioEnabled!!.enabled!! && chatAttachment.audioEnabled!!.individualChatAudioSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.SINGLE_CHAT_VIDEO,
                    if (chatAttachment.videoEnabled!!.enabled!! && chatAttachment.videoEnabled!!.individualChatVideoSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.SINGLE_CHAT_LOCATION,
                    if (chatAttachment.locationEnabled!!.enabled!! && chatAttachment.locationEnabled!!.individualChatLocationSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.SINGLE_CHAT_CONTACT,
                    if (chatAttachment.contactEnabled!!.enabled!! && chatAttachment.contactEnabled!!.individualChatContactSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.SINGLE_CHAT_DOCUMENT,
                    if (chatAttachment.documentEnabled!!.enabled!! && chatAttachment.documentEnabled!!.individualChatDocumentSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.SINGLE_CHAT_IMAGE,
                    if (chatAttachment.imageEnabled!!.enabled!! && chatAttachment.imageEnabled!!.individualChatImageSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.SINGLE_CHAT_GIPHY,
                    if (chatAttachment.gifyEnabled!!.enabled!! && chatAttachment.gifyEnabled!!.individualChatGifySharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.GROUP_CHAT_AUDIO,
                    if (chatAttachment.audioEnabled!!.enabled!! && chatAttachment.audioEnabled!!.groupChatAudioSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.GROUP_CHAT_VIDEO,
                    if (chatAttachment.videoEnabled!!.enabled!! && chatAttachment.videoEnabled!!.groupChatVideoSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.GROUP_CHAT_LOCATION,
                    if (chatAttachment.locationEnabled!!.enabled!! && chatAttachment.locationEnabled!!.groupChatLocationSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.GROUP_CHAT_CONTACT,
                    if (chatAttachment.contactEnabled!!.enabled!! && chatAttachment.contactEnabled!!.groupChatContactSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.GROUP_CHAT_DOCUMENT,
                    if (chatAttachment.documentEnabled!!.enabled!! && chatAttachment.documentEnabled!!.groupChatDocumentSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.GROUP_CHAT_IMAGE,
                    if (chatAttachment.imageEnabled!!.enabled!! && chatAttachment.imageEnabled!!.groupChatImageSharing!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.GROUP_CHAT_GIPHY,
                    if (chatAttachment.gifyEnabled!!.enabled!! && chatAttachment.gifyEnabled!!.groupChatGifySharing!!.enabled!!) "1" else "0"
                )
            )
        }
        val forwardChat = features.forwardChat
        if (forwardChat != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.FORWARD_CHAT,
                    if (forwardChat.enabled) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ForwardChat.SINGLE_CHAT_TEXT,
                    if (forwardChat.textEnabled.individualChatTextForward.enabled) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ForwardChat.SINGLE_CHAT_MEDIA,
                    if (forwardChat.mediaEnabled.individualChatMediaForward.enabled) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ForwardChat.SINGLE_CHAT_LOCATION,
                    if (forwardChat.locationEnabled.individualChatLocationForward.enabled) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ForwardChat.SINGLE_CHAT_CONTACT,
                    if (forwardChat.contactEnabled.individualChatContactForward.enabled) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ForwardChat.SINGLE_CHAT_GIPHY,
                    if (forwardChat.gifyEnabled.individualChatGifyForward.enabled) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ForwardChat.GROUP_CHAT_TEXT,
                    if (forwardChat.textEnabled.groupChatTextForward.enabled) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ForwardChat.GROUP_CHAT_MEDIA,
                    if (forwardChat.mediaEnabled.groupChatMediaForward.enabled) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ForwardChat.GROUP_CHAT_LOCATION,
                    if (forwardChat.locationEnabled.groupChatLocationForward.enabled) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ForwardChat.GROUP_CHAT_CONTACT,
                    if (forwardChat.contactEnabled.groupChatContactForward.enabled) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ForwardChat.GROUP_CHAT_GIPHY,
                    if (forwardChat.gifyEnabled.groupChatGifyForward.enabled) "1" else "0"
                )
            )
        }
        val editChat = features.editChat
        if (editChat != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.EDIT_CHAT,
                    if (editChat.enabled!!) "1" else "0"
                )
            )
        }
        val e2eEncryption = features.e2EEncryption
        if (e2eEncryption != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.E2E_CHAT,
                    if (e2eEncryption.enabled!!) "1" else "0"
                )
            )

            //if(e2eEncryption.getEnabled()) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.E2E_CHAT_TEXT,
                    if (e2eEncryption.text != null && e2eEncryption.text!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.E2E_CHAT_MEDIA,
                    if (e2eEncryption.media != null && e2eEncryption.media!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.E2E_CHAT_LOCATION,
                    if (e2eEncryption.location != null && e2eEncryption.location!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.E2E_CHAT_CONTACT,
                    if (e2eEncryption.contact != null && e2eEncryption.contact!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.E2E_CHAT_GIFY,
                    if (e2eEncryption.gify != null && e2eEncryption.gify!!.enabled!!) "1" else "0"
                )
            )
            //}
        }
        val deleteChat = features.deleteChat
        if (deleteChat != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.DELETE_CHAT,
                    if (deleteChat.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.DeleteChat.DELETE_FOR_SELF,
                    if (deleteChat.deleteForSelf!!.enabled!!) "1" else "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.DeleteChat.DELETE_FOR_EVERYONE,
                    if (deleteChat.deleteForEveryone!!.enabled!!) "1" else "0"
                )
            )
        } else {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.DELETE_CHAT,
                    "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.DeleteChat.DELETE_FOR_SELF,
                    "0"
                )
            )
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.DeleteChat.DELETE_FOR_EVERYONE,
                    "0"
                )
            )
        }
        configArrayList.add(
            TenantConfig(
                tenant.id,
                io.inappchat.inappchat.utils.Constants.TenantConfig.CHAT_REACTIONS,
                if (Objects.requireNonNull(features.chatReactions)?.enabled!!) "1" else "0"
            )
        )
        configArrayList.add(
            TenantConfig(
                tenant.id,
                io.inappchat.inappchat.utils.Constants.TenantConfig.REPLY_THREAD,
                if (Objects.requireNonNull(features.replyThread)?.enabled!!) "1" else "0"
            )
        )
        if (features.followChat != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.FOLLOW_CHAT,
                    if (features.followChat!!.enabled!!) "1" else "0"
                )
            )
        }
        val unifiedSearch = features.unifiedSearch
        if (unifiedSearch != null) {
            //Channel Search
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.CHANNEL_SEARCH,
                    if (features.unifiedSearch!!.enabled!!) "1" else "0"
                )
            )
            //Global Search
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.GLOBAL_SEARCH,
                    if (features.unifiedSearch!!.enabled!!) "1" else "0"
                )
            )
        }
        val userMentions = features.userMentions
        if (userMentions != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.USER_MENTIONS,
                    if (features.userMentions!!.enabled!!) "1" else "0"
                )
            )
        }
        if (features.announcements != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.ANNOUNCEMENT,
                    if (features.announcements!!.enabled!!) "1" else "0"
                )
            )
        }
        if (features.copyChat != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.COPY,
                    if (features.copyChat!!.enabled!!) "1" else "0"
                )
            )
        }

        if (features.localSearch != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.LOCAL_SEARCH,
                    if (features.localSearch!!.enabled!!) "1" else "0"
                )
            )
        }

        val moderation = features.moderation
        if (moderation != null) {
            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.MODERATION,
                    if (moderation.enabled!!) "1" else "0"
                )
            )

            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.DOMAIN_FILTER,
                    if (moderation.domainFilter!!.enabled!!) "1" else "0"
                )
            )

            configArrayList.add(
                TenantConfig(
                    tenant.id,
                    io.inappchat.inappchat.utils.Constants.TenantConfig.PROFANITY_FILTER,
                    if (moderation.profanityFilter!!.enabled!!) "1" else "0"
                )
            )
        }
        return configArrayList
    }
}