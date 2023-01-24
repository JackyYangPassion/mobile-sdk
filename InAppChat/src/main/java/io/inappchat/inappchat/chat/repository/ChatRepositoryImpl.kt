package io.inappchat.inappchat.chat.repository

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RestrictTo
import io.inappchat.inappchat.chat.mapper.ChatEntityMapper.getChatRow
import io.inappchat.inappchat.chat.mapper.ChatEntityMapper.getChatThreadRow
import io.inappchat.inappchat.chat.mapper.ChatEntityMapper.transform
import io.inappchat.inappchat.chat.mapper.ChatEvent
import io.inappchat.inappchat.chat.mapper.ChatMetaDataRecord
import io.inappchat.inappchat.chat.mapper.ChatReactionRecord
import io.inappchat.inappchat.chat.mapper.ChatRecordMapper.transform
import io.inappchat.inappchat.chat.mapper.ChatRequestMapper.mentions
import io.inappchat.inappchat.chat.mapper.ChatRequestMapper.request
import io.inappchat.inappchat.chat.mapper.ChatRequestMapper.requestE2E
import io.inappchat.inappchat.chat.mapper.ChatRequestMapper.requestEditE2E
import io.inappchat.inappchat.chat.mapper.ChatSettingsRecord
import io.inappchat.inappchat.chat.mapper.DomainDataRecord
import io.inappchat.inappchat.chat.mapper.FollowThreadRecord
import io.inappchat.inappchat.chat.mapper.MessageMetaDataRecord
import io.inappchat.inappchat.chat.mapper.MessageRecord
import io.inappchat.inappchat.chat.mapper.ProfanityDataRecord
import io.inappchat.inappchat.chat.model.Message
import io.inappchat.inappchat.chat.model.MessageMetaData
import io.inappchat.inappchat.chat.model.Reaction
import io.inappchat.inappchat.core.base.BaseRepository
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.core.type.ChatEventType
import io.inappchat.inappchat.core.type.ChatEventType.INCOMING
import io.inappchat.inappchat.core.type.ChatEventType.OUTGOING
import io.inappchat.inappchat.core.type.ChatReactionType
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.DeleteType
import io.inappchat.inappchat.core.type.EventType
import io.inappchat.inappchat.core.type.MessageStatus
import io.inappchat.inappchat.core.type.MessageStatus.SENT
import io.inappchat.inappchat.core.type.MessageUpdateType
import io.inappchat.inappchat.core.type.NetworkEvent
import io.inappchat.inappchat.core.type.NotificationSettingsType
import io.inappchat.inappchat.core.type.SettingAppliedFor
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.e2e.ECDHUtils
import io.inappchat.inappchat.group.mapper.GroupMapper
import io.inappchat.inappchat.group.mapper.GroupRecord
import io.inappchat.inappchat.thread.handler.ThreadRepository
import io.inappchat.inappchat.thread.mapper.ThreadMapper
import io.inappchat.inappchat.user.mapper.UserMapper
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.utils.Constants.TenantConfig
import io.inappchat.inappchat.wrappers.UserWrapper
import io.inappchat.inappchat.cache.database.dao.ChatReactionDao
import io.inappchat.inappchat.cache.database.dao.ChatThreadDao
import io.inappchat.inappchat.cache.database.dao.DomainFilterDao
import io.inappchat.inappchat.cache.database.dao.EKeyDao
import io.inappchat.inappchat.cache.database.dao.ProfanityFilterDao
import io.inappchat.inappchat.cache.database.dao.SingleChatDao
import io.inappchat.inappchat.cache.database.dao.UserDao
import io.inappchat.inappchat.cache.database.entity.ChatReactionEntity
import io.inappchat.inappchat.cache.database.entity.ChatThread
import io.inappchat.inappchat.cache.database.entity.DomainFilter
import io.inappchat.inappchat.cache.database.entity.EKeyTable
import io.inappchat.inappchat.cache.database.entity.ProfanityFilter
import io.inappchat.inappchat.cache.database.entity.SingleChat
import io.inappchat.inappchat.cache.database.entity.Thread
import io.inappchat.inappchat.cache.database.entity.ThreadChatEmbedded
import io.inappchat.inappchat.cache.database.entity.User
import io.inappchat.inappchat.cache.preference.PreferenceManager
import io.inappchat.inappchat.mqtt.MqttManager
import io.inappchat.inappchat.remote.NetworkConfig
import io.inappchat.inappchat.remote.NetworkManager
import io.inappchat.inappchat.remote.model.request.BlockUnblockUserRequest
import io.inappchat.inappchat.remote.model.request.ChatReactionRequest
import io.inappchat.inappchat.remote.model.request.CreateThreadRequest
import io.inappchat.inappchat.remote.model.request.FollowThreadRequest
import io.inappchat.inappchat.remote.model.request.ForwardChat
import io.inappchat.inappchat.remote.model.request.MediaReq
import io.inappchat.inappchat.remote.model.request.MessageList
import io.inappchat.inappchat.remote.model.request.MessageRequest
import io.inappchat.inappchat.remote.model.request.ReplyThread
import io.inappchat.inappchat.remote.model.request.ReportMessageRequest
import io.inappchat.inappchat.remote.model.request.SearchQuery
import io.inappchat.inappchat.remote.model.request.SearchRequest
import io.inappchat.inappchat.remote.model.request.UpdateUserRequest
import io.inappchat.inappchat.remote.model.response.ChatSettingsResponse
import io.inappchat.inappchat.remote.model.response.CreateThreadResponse
import io.inappchat.inappchat.remote.model.response.MessageResponse
import io.inappchat.inappchat.remote.util.HeaderUtils
import io.inappchat.inappchat.downloader.handler.DownloadRepository
import io.inappchat.inappchat.downloader.request.DownloadRequestBuilder
import io.inappchat.inappchat.utils.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList

/** @author meeth
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ChatRepositoryImpl private constructor(
    private val dataManager: DataManager,
    private val threadRepository: ThreadRepository,
    private val eventHandler: EventHandler,
    private val downloadRepository: DownloadRepository,
    private val singleChatDao: SingleChatDao = dataManager.db().singleChatDao(),
    private val chatThreadDao: ChatThreadDao = dataManager.db().chatThreadDao(),
    private val chatReactionDao: ChatReactionDao = dataManager.db().chatReactionDao(),
    private val userDao: UserDao = dataManager.db().userDao(),
    private val ekeyDao: EKeyDao = dataManager.db().ekeyDao(),
    private val networkManager: NetworkManager = dataManager.network(),
    private val preference: PreferenceManager = dataManager.preference(),
    private val mqtt: MqttManager = dataManager.mqtt(),
    private val networkConfig: NetworkConfig = dataManager.networkConfig(),
    private val local: ChatLocalRepository,
    private val remote: ChatRemoteRepository,
    private val domainFilterDao: DomainFilterDao = dataManager.db().domainFilterDao(),
    private val profanityFilterDao: ProfanityFilterDao = dataManager.db().profanityFilterDao()
) : BaseRepository(dataManager), ChatRepository {

    companion object {
        @JvmStatic
        fun newInstance(
            dataManager: DataManager,
            threadRepository: ThreadRepository,
            eventHandler: EventHandler,
            downloadRepository: DownloadRepository
        ): ChatRepository {
            val local: ChatLocalRepository = ChatLocalRepository.newInstance(dataManager)
            val remote: ChatRemoteRepository = ChatRemoteRepository.newInstance(
                dataManager,
                threadRepository,
                eventHandler,
                downloadRepository
            )
            return ChatRepositoryImpl(
                dataManager = dataManager,
                threadRepository = threadRepository,
                eventHandler = eventHandler,
                downloadRepository = downloadRepository,
                local = local,
                remote = remote
            )
        }
    }

    override fun getMessages(
        tenantId: String,
        threadId: String,
        currentMsgId: String?,
        direction: String?,
        pageSize: Int?,
        isGlobalSearched: Boolean?
    ): Single<MutableList<MessageRecord>> {
        return singleChatDao.hasThreadMessage(threadId).flatMap {
            if (preference.skipRestoreFlag) {
                return@flatMap local.getMessages(
                    tenantId,
                    threadId,
                    currentMsgId,
                    direction,
                    pageSize,
                    isGlobalSearched
                )
            } else if (it.size > 1) {
                if (currentMsgId.isNullOrEmpty()) {
                    return@flatMap local.getMessages(
                        tenantId,
                        threadId,
                        currentMsgId,
                        direction,
                        pageSize,
                        isGlobalSearched
                    )
                } else if (pageSize != null && pageSize > 0) {
                    return@flatMap remote.getMessages(
                        tenantId,
                        threadId,
                        currentMsgId,
                        direction,
                        pageSize,
                        isGlobalSearched
                    )
                }
                return@flatMap local.getMessages(
                    tenantId,
                    threadId,
                    currentMsgId,
                    direction,
                    pageSize,
                    isGlobalSearched
                )
            } else {
                return@flatMap remote.getMessages(
                    tenantId,
                    threadId,
                    currentMsgId,
                    direction,
                    pageSize,
                    isGlobalSearched
                )
            }
        }
    }

    private fun composeReactionList(listOfReactionEntities: List<ChatReactionEntity>?): ArrayList<ChatReactionRecord> {
        val chatReactionList = arrayListOf<ChatReactionRecord>()
        val groupBy = listOfReactionEntities?.groupBy { it.unicode }
        if (groupBy != null) {
            for ((k, reactionList) in groupBy) {

                val toList = reactionList.groupBy { it.userChatId }.keys.toList()
                val userEntity = userDao.getUserEntityByUserChatIds(toList.toTypedArray())
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

    override fun getChatThreadMessages(
        tenantId: String,
        threadId: String,
        parentMsgId: String
    ): Single<MutableList<MessageRecord>> {
        return singleChatDao.getChatByMsgIdCount(parentMsgId).flatMap { list ->
            if (list.isNullOrEmpty()) {
                throw UnsupportedOperationException("Please wait for message to send to start a thread")
            }
            val singleChat = list[0]
            if (singleChat.status == MessageStatus.SENDING.status || singleChat.status == MessageStatus.FAILED.status) {
                throw UnsupportedOperationException("Please wait for message to send to start a thread")
            }

            chatThreadDao.getAll(parentMsgId)
                .flatMap { threadChatEmbeded: List<ThreadChatEmbedded?> ->
                    val appUserId = appUserId
                    val eventType = if (singleChat.senderAppUserId != appUserId) {
                        INCOMING
                    } else {
                        OUTGOING
                    }
                    val messageRecords: MutableList<MessageRecord> = java.util.ArrayList()
                    var senderUserRecord: UserRecord? = null
                    if (singleChat.type.equals(ChatType.GROUP.type)) {
                        senderUserRecord = UserMapper.transform(
                            userDao.getUserByIdInSync(tenantId, singleChat.senderAppUserId)
                        )
                    }
                    val reactionsFromDatabase =
                        chatReactionDao.getAllReactions(singleChat.id).blockingGet()
                    val reactionRecords = composeReactionList(reactionsFromDatabase)
                    messageRecords.add(
                        transform(
                            singleChat = singleChat,
                            senderRecord = senderUserRecord,
                            chatEventType = eventType,
                            chatReactions = reactionRecords
                        )
                    )

                    for (threadChatEmbedded in threadChatEmbeded) {

                        threadChatEmbedded?.let { it ->
                            var recipientRecord: UserRecord?
                            recipientRecord = null
                            if (it.chatThread?.type.equals(ChatType.GROUP.type) || it.chatThread?.type.equals(
                                    ChatType.GROUP_CHAT_THREAD.type
                                )
                            ) {
                                recipientRecord = UserMapper.transform(
                                    userDao.getUserByIdInSync(
                                        tenantId, it.chatThread?.senderAppUserId
                                    )
                                )
                            }
                            val eventType = if (it.chatThread?.senderAppUserId != appUserId) {
                                INCOMING
                            } else {
                                OUTGOING
                            }

                            // Compose reactions list
                            val chatThreadReactions = composeReactionList(it.reactionEntities)
                            it.chatThread?.let {
                                transform(
                                    chatThread = it,
                                    senderRecord = recipientRecord,
                                    receiverRecord = recipientRecord,
                                    chatEventType = eventType,
                                    chatReactions = chatThreadReactions
                                )
                            }
                                ?.let { messageRecords.add(it) }
                        }
                    }

                    Single.just(messageRecords)
                }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun sendMessage(
        message: Message,
        tenantId: String,
        threadId: String,
        deviceId: String,
        customData: String?
    ): Single<MessageRecord> {

        return getThread(threadId, message).flatMap { thread: Thread? ->
            if (noInternetConnection() && message.message.isNullOrEmpty()) {
                throw UnsupportedOperationException("Network unavailable")
            }

            var replyThread: ReplyThread? = null
            val msgId = UUID.randomUUID().toString()
            message.clientCreatedAt = System.currentTimeMillis()
            if (message.parentMsgId != null && message.parentMsgId.isNotEmpty()) {
                val singleChat = singleChatDao.getChatByLocalMsgId(message.parentMsgId, threadId)
                val sendToChannel = message.sendToChannel
                val id = singleChat.id
                if (message.sendToChannel == 1) {

                    val chatThread = getChatThreadRow(
                        thread,
                        message,
                        msgId,
                        message.parentMsgId,
                        customData = customData
                    )
                    chatThread.parentMsgId = id
                    chatThread.sendToChannel = sendToChannel
                    chatThreadDao.insertWithReplace(chatThread)

                    val chatRow = getChatRow(
                        thread,
                        message,
                        msgId,
                        id,
                        getParentMessage(singleChat),
                        customData = customData
                    )
                    singleChatDao.insertWithReplace(chatRow)

                } else {
                    val chatThread = getChatThreadRow(
                        thread,
                        message,
                        msgId,
                        message.parentMsgId,
                        customData = customData
                    )
                    chatThread.parentMsgId = id
                    chatThread.sendToChannel = sendToChannel
                    chatThreadDao.insertWithReplace(chatThread)
                }

                singleChat.isFollowed = 1
                singleChatDao.update(singleChat)
                replyThread = ReplyThread(singleChat.msgUniqueId, message.sendToChannel)
                // Add thread changes
            } else {
                //Add channel message changes
                singleChatDao.insertWithReplace(
                    getChatRow(
                        thread,
                        message,
                        msgId,
                        customData = customData
                    )
                )
            }

            val response: Single<MessageResponse>
            response = when {
                message.media != null -> {
                    networkManager.api()
                        .sendMedia(
                            tenantId,
                            thread!!.id,
                            thread.senderChatId,
                            message.media.mediaPath,
                            msgId,
                            message.media.type.type,
                            preference.chatUserId!!,
                            replyThread,
                            Utils.getMimeType(message.media.mediaPath, message.media.type.type),
                            message.clientCreatedAt,
                            customData = customData
                        )
                }
                message.file != null -> {
                    networkManager.api()
                        .sendMedia(
                            tenantId,
                            thread!!.id,
                            thread.senderChatId,
                            message.file.filePath,
                            msgId,
                            message.file.type.type,
                            preference.chatUserId!!,
                            replyThread,
                            Utils.getMimeType(message.file.filePath, message.file.type.type),
                            message.clientCreatedAt,
                            customData = customData
                        )
                }
                else -> {
                    networkManager.api()
                        .sendMessage(
                            tenantId, request(
                                thread!!,
                                message,
                                msgId,
                                replyThread,
                                getMentionedChatUserIds(message),
                                customData = customData
                            ), Objects.requireNonNull(preference.chatUserId)!!
                        )
                }
            }
            response.flatMap { messageResponse ->

                val msgId1 = messageResponse.data?.requestId
                val singleChatDao = dataManager.db().singleChatDao()

                if (messageResponse.replyThreadFeatureData?.parentMsgId != null && messageResponse.replyThreadFeatureData?.parentMsgId!!.isNotEmpty()) {

                    val chatThreadDao = dataManager.db().chatThreadDao()
                    val chatThread = chatThreadDao.getChatByClientId(msgId1, threadId)
                    chatThread.msgUniqueId = messageResponse.msgUniqueId
                    chatThread.status = SENT.status
                    if (messageResponse.media != null) {
                        chatThread.mediaName = messageResponse.media?.name
                        chatThread.mediaPath =
                            dataManager.preference()?.chatServer + messageResponse.media?.path
                        chatThread.mediaThumbnail =
                            dataManager.preference()?.chatServer + messageResponse.media?.thumbnail
                    }
                    Logger.d(
                        "thread_debug ###",
                        "API : METADATA_ID : $msgId1 ::: Thread_Id : $threadId"
                    )
                    Logger.d("thread_debug ###", "API : chatThread : $chatThread")
                    chatThreadDao.insertWithReplace(chatThread)

                    if (messageResponse.replyThreadFeatureData?.replyMsgConfig == 1) {
                        val singleChat = singleChatDao.getChatByLocalMsgId(msgId1, threadId)
                        singleChat.msgUniqueId = messageResponse.msgUniqueId
                        singleChat.status = SENT.status
                        if (messageResponse.media != null) {
                            singleChat.mediaName = messageResponse.media?.name
                            singleChat.mediaPath =
                                dataManager.preference()?.chatServer + messageResponse.media?.path
                            singleChat.mediaThumbnail =
                                dataManager.preference()?.chatServer + messageResponse.media?.thumbnail
                        }
                        singleChatDao.insertWithReplace(singleChat)
                        Logger.d("thread_debug ###", "API : singleChat : $singleChat")
                        // Here both main and chat thread should be given back to the app
                        Single.just(transform(chatThread = chatThread))
                        //}
                    } else {
                        Single.just(transform(chatThread = chatThread))
                    }
                    //}.doOnError { it.printStackTrace() }
                } else {
                    singleChatDao.getChatByClientIdByRxSingle(msgId1, threadId)
                        .flatMap { singleChat ->
                            singleChat.msgUniqueId = messageResponse.msgUniqueId
                            singleChat.status = SENT.status
                            if (messageResponse.media != null) {
                                singleChat.mediaName = messageResponse.media?.name
                                singleChat.mediaPath =
                                    dataManager.preference()?.chatServer + messageResponse.media?.path
                                singleChat.mediaThumbnail =
                                    dataManager.preference()?.chatServer + messageResponse.media?.thumbnail
                            }
                            singleChatDao.insertWithReplace(singleChat)
                            Single.just(transform(singleChat = singleChat))
                        }
                }

            }.onErrorReturn {
                it.cause?.let { cause ->
                    if (cause.toString() == "java.net.SocketTimeoutException: timeout") {
                        if (message.media != null || message.file != null) {
                            singleChatDao.setStatus(MessageStatus.FAILED.status, msgId)
                            chatThreadDao.setStatus(MessageStatus.FAILED.status, msgId)

                            if (message.parentMsgId != null && message.parentMsgId.isNotEmpty()) {
                                return@onErrorReturn transform(
                                    chatThread = chatThreadDao.getChatByClientId(
                                        msgId,
                                        threadId
                                    )
                                )
                            } else {
                                return@onErrorReturn transform(
                                    singleChat = singleChatDao.getChatByLocalMsgId(
                                        msgId,
                                        threadId
                                    )
                                )
                            }
                        }
                    }
                }
                throw it
            }.doOnError {
                it.printStackTrace()
                it.message?.let { error ->
                    if (error.contains(Constants.ErrorMessage.ERROR_403)) {
                        singleChatDao.deleteByMsgId(msgId)
                        chatThreadDao.deleteByMsgId(msgId)
                    }
                }
            }
        }.doOnError {
            it.printStackTrace()
        }
    }

    private fun getMentionedChatUserIds(message: Message): MutableList<String>? {
        var mentionedChatUserIds: MutableList<String>? = null
        return if (message.mentioned_users != null && message.mentioned_users.isNotEmpty()) {
            val toArray = message.mentioned_users.toTypedArray()
            mentionedChatUserIds = userDao.getUserChatIdByUserAppIds(toArray)
            mentionedChatUserIds
        } else {
            mentionedChatUserIds
        }
    }

    override fun sendE2EMessage(
        message: Message,
        tenantId: String,
        threadId: String,
        deviceId: String,
        parallelDeviceList: ArrayList<EKeyTable>?,
        customData: String?
    ): Single<MessageRecord> {

        return getThread(threadId).flatMap { thread: Thread ->
            if (noInternetConnection()) {
                throw UnsupportedOperationException("Network unavailable")
            }

            var replyThread: ReplyThread? = null
            val msgId = UUID.randomUUID().toString()
            message.clientCreatedAt = System.currentTimeMillis()
            if (message.parentMsgId != null && message.parentMsgId.isNotEmpty()) {
                val singleChat = singleChatDao.getChatByLocalMsgId(message.parentMsgId, threadId)
                val sendToChannel = message.sendToChannel
                val id = singleChat.id

                if (message.sendToChannel == 1) {

                    val chatThread =
                        getChatThreadRow(
                            thread,
                            message,
                            msgId,
                            message.parentMsgId,
                            customData = customData
                        )
                    chatThread.parentMsgId = id
                    chatThread.sendToChannel = sendToChannel
                    chatThreadDao.insertWithReplace(chatThread)

                    val chatRow = getChatRow(
                        thread,
                        message,
                        msgId,
                        id,
                        getParentMessage(singleChat),
                        customData = customData
                    )
                    singleChatDao.insertWithReplace(chatRow)

                } else {
                    val chatThread =
                        getChatThreadRow(
                            thread,
                            message,
                            msgId,
                            message.parentMsgId,
                            customData = customData
                        )
                    chatThread.parentMsgId = id
                    chatThread.sendToChannel = sendToChannel
                    chatThreadDao.insertWithReplace(chatThread)
                }

                singleChat.isFollowed = 1
                singleChatDao.update(singleChat)
                replyThread = ReplyThread(singleChat.msgUniqueId, message.sendToChannel)
                // Add thread changes
            } else {
                //Add channel message changes
                singleChatDao.insertWithReplace(
                    getChatRow(
                        thread,
                        message,
                        msgId,
                        customData = customData
                    )
                )
            }
            val eKeyTable =
                ekeyDao.getMyLatestPrivateKey(thread?.senderChatId, deviceId, thread?.tenantId)

            val e2eRequest = requestE2E(
                thread!!,
                message,
                msgId,
                replyThread,
                eKeyTable,
                ekeyDao,
                dataManager.db().groupDao(),
                getMentionedChatUserIds(message),
                parallelDeviceList,
                customData
            )

            val response = networkManager.api()
                .sendE2EMessage(
                    tenantId, e2eRequest, Objects.requireNonNull(preference.chatUserId)!!
                )
            response.flatMap { response ->

                val msgId1 = response.data?.requestId
                val chatStatus = response.chatStatus
                if (chatStatus == null || !chatStatus.retryRequired) {

                    // save key id in case of new key generation
                    if (chatStatus != null && chatStatus.keyList!!.isNotEmpty()) {
                        // my key updated with key_id
                        for (e2eKey in chatStatus.keyList!!) {
                            if (e2eKey.returnCode != null && e2eKey.returnCode.equals("receiverKeyNotValid")) {
                                //need to remove all data from local DB
                                ekeyDao.deleteInActiveDevice(e2eKey.eRTCUserId, e2eKey.deviceId)
                            } else {
                                if (e2eKey.deviceId == deviceId) {
                                    ekeyDao.setKeyId(
                                        preference.chatUserId,
                                        e2eKey.publicKey,
                                        e2eKey.keyId,
                                        eKeyTable.time,
                                        deviceId
                                    )
                                } else {
                                    //update Remaining keys
                                    val updatedRow: Int = ekeyDao.updateKey(
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
                                            tenantId = tenantId
                                        )
                                        ekeyDao.save(eKeyTableUpdated)
                                    }
                                }
                            }
                        }

                    }

                    if (response.replyThreadFeatureData?.parentMsgId != null && response.replyThreadFeatureData?.parentMsgId!!.isNotEmpty()) {

                        val chatThread =
                            dataManager.db().chatThreadDao().getChatByClientId(msgId1, threadId)
                        chatThread.msgUniqueId = response.msgUniqueId
                        chatThread.status = SENT.status
                        Logger.d(
                            "thread_debug ###",
                            "API : METADATA_ID : $msgId1 ::: Thread_Id : $threadId"
                        )
                        Logger.d("thread_debug ###", "API : chatThread : $chatThread")
                        dataManager.db().chatThreadDao().insertWithReplace(chatThread)

                        if (response.replyThreadFeatureData?.replyMsgConfig == 1) {
                            val singleChat =
                                dataManager.db().singleChatDao()
                                    .getChatByLocalMsgId(msgId1, threadId)
                            singleChat.msgUniqueId = response.msgUniqueId
                            singleChat.status = SENT.status
                            dataManager.db().singleChatDao().insertWithReplace(singleChat)
                            Logger.d("thread_debug ###", "API : singleChat : $singleChat")
                            // Here both main and chat thread should be given back to the app
                            Single.just(transform(chatThread = chatThread))
                            //}
                        } else {
                            Single.just(transform(chatThread = chatThread))
                        }
                        //}.doOnError { it.printStackTrace() }
                    } else {
                        singleChatDao.getChatByClientIdByRxSingle(msgId1, threadId)
                            .flatMap { singleChat ->
                                singleChat.msgUniqueId = response.msgUniqueId
                                singleChat.status = SENT.status
                                singleChatDao.insertWithReplace(singleChat)
                                Single.just(transform(singleChat = singleChat))
                            }
                    }
                } else {
                    // add retry using operator
                    val returnCode = chatStatus.returnCode
                    if (!TextUtils.isEmpty(returnCode)) {
                        when (returnCode) {
                            "senderKeyValidityExpired" -> {
                                Log.i("ChatRepo", "senderKeyValidityExpired")
                                val keyPair = ECDHUtils.generateKeyPair()
                                val eKeyTableUpdated = EKeyTable(
                                    deviceId = preference.deviceId!!,
                                    publicKey = keyPair[0],
                                    privateKey = keyPair[1],
                                    ertcUserId = preference.chatUserId!!,
                                    tenantId = tenantId
                                )
                                ekeyDao.save(eKeyTableUpdated)
                                // need to do retry here
                                singleChatDao.deleteByMsgId(msgId)
                                chatThreadDao.deleteByMsgId(msgId)
                                if (chatStatus.keyList!!.isNotEmpty()) {
                                    val newDeviceList: ArrayList<EKeyTable> = ArrayList()
                                    Log.i("ChatRepo", "senderKeyValidityExpired - 1")
                                    for (e2eKey in chatStatus.keyList!!) {
                                        if (e2eKey.returnCode != null && e2eKey.returnCode.equals("receiverKeyNotValid")) {
                                            //need to remove inactive device from local DB
                                            ekeyDao.deleteInActiveDevice(
                                                e2eKey.eRTCUserId,
                                                e2eKey.deviceId
                                            )
                                        } else {
                                            if (e2eKey.deviceId == deviceId) {
                                                Log.i("ChatRepo", "senderKeyValidityExpired - 2")
                                                ekeyDao.setKeyId(
                                                    preference.chatUserId,
                                                    e2eKey.publicKey,
                                                    e2eKey.keyId,
                                                    eKeyTable.time,
                                                    deviceId
                                                )
                                            } else {
                                                Log.i("ChatRepo", "senderKeyValidityExpired - 3")
                                                val updatedRow = ekeyDao.updateKey(
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
                                                        tenantId = tenantId
                                                    )
                                                    ekeyDao.save(eKeyTableUpdated)
                                                }

                                                newDeviceList.add(
                                                    EKeyTable(
                                                        keyId = e2eKey.keyId,
                                                        deviceId = e2eKey.deviceId,
                                                        publicKey = e2eKey.publicKey,
                                                        privateKey = "",
                                                        ertcUserId = e2eKey.eRTCUserId,
                                                        tenantId = tenantId
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                                return@flatMap sendE2EMessage(
                                    message,
                                    tenantId,
                                    threadId,
                                    deviceId,
                                    customData = customData
                                )
                            }
                            "receiverKeyValidationError" -> {
                                // need to handle
                                Log.i("ChatRepo", "receiverKeyValidationError")
                                if (chatStatus.keyList!!.isNotEmpty()) {
                                    Log.i("ChatRepo", "receiverKeyValidationError - 1")
                                    for (e2eKey in chatStatus.keyList!!) {
                                        if (e2eKey.returnCode != null && e2eKey.returnCode.equals("receiverKeyNotValid")) {
                                            //need to remove inactive device from local DB
                                            ekeyDao.deleteInActiveDevice(
                                                e2eKey.eRTCUserId,
                                                e2eKey.deviceId
                                            )
                                        } else {
                                            if (e2eKey.deviceId == deviceId) {
                                                Log.i("ChatRepo", "receiverKeyValidationError - 2")
                                                ekeyDao.setKeyId(
                                                    preference.chatUserId,
                                                    e2eKey.publicKey,
                                                    e2eKey.keyId,
                                                    eKeyTable.time,
                                                    deviceId
                                                )
                                            } else {
                                                Log.i("ChatRepo", "receiverKeyValidationError - 3")
                                                //receiverNewDeviceKeyAvailable is handle here. we will add new device details in eKey table
                                                val updatedRow = ekeyDao.updateKey(
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
                                                        tenantId = tenantId
                                                    )
                                                    ekeyDao.save(eKeyTableUpdated)
                                                }
                                            }
                                        }
                                    }
                                    // need to do retry here
                                    singleChatDao.deleteByMsgId(msgId)
                                    chatThreadDao.deleteByMsgId(msgId)
                                    return@flatMap sendE2EMessage(
                                        message,
                                        tenantId,
                                        threadId,
                                        deviceId,
                                        customData = customData
                                    )
                                }
                            }
                            "senderNewDeviceKeyAvailable" -> {
                                // need to handle
                                Log.i("ChatRepo", "senderNewDeviceKeyAvailable")
                                if (chatStatus.keyList!!.isNotEmpty()) {
                                    val newDeviceList: ArrayList<EKeyTable> = ArrayList()
                                    Log.i("ChatRepo", "senderNewDeviceKeyAvailable - 1")
                                    for (e2eKey in chatStatus.keyList!!) {
                                        if (e2eKey.deviceId == deviceId) {
                                            Log.i("ChatRepo", "senderNewDeviceKeyAvailable - 2")
                                            ekeyDao.setKeyId(
                                                preference.chatUserId,
                                                e2eKey.publicKey,
                                                e2eKey.keyId,
                                                eKeyTable.time,
                                                deviceId
                                            )
                                        } else {
                                            Log.i("ChatRepo", "senderNewDeviceKeyAvailable - 3")
                                            val updatedRow = ekeyDao.updateKey(
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
                                                    tenantId = tenantId
                                                )
                                                ekeyDao.save(eKeyTableUpdated)
                                            }

                                            newDeviceList.add(
                                                EKeyTable(
                                                    keyId = e2eKey.keyId,
                                                    deviceId = e2eKey.deviceId,
                                                    publicKey = e2eKey.publicKey,
                                                    privateKey = "",
                                                    ertcUserId = e2eKey.eRTCUserId,
                                                    tenantId = tenantId
                                                )
                                            )
                                        }
                                    }
                                    // need to do retry here
                                    singleChatDao.deleteByMsgId(msgId)
                                    chatThreadDao.deleteByMsgId(msgId)
                                    return@flatMap sendE2EMessage(
                                        message,
                                        tenantId,
                                        threadId,
                                        deviceId,
                                        newDeviceList,
                                        customData = customData
                                    )
                                }
                            }
                        }
                    }
                    Single.just(MessageRecord("ID"))
                }
            }.doOnError {
                it.printStackTrace()
                it.message?.let { error ->
                    if (error.contains(Constants.ErrorMessage.ERROR_403)) {
                        singleChatDao.deleteByMsgId(msgId)
                        chatThreadDao.deleteByMsgId(msgId)
                    }
                }
            }
        }.doOnError { it.printStackTrace() }
    }

    override fun getChatUserId(
        tenantId: String,
        userId: String?,
        fcmToken: String?,
        deviceId: String?
    ): Single<Result> {
        if (noInternetConnection()) {
            throw UnsupportedOperationException("Network unavailable")
        }
        return networkManager.api()
            .getChatUser(
                tenantId, UpdateUserRequest(
                    userId, fcmToken, deviceId, TenantConfig.DEVICE_TYPE
                )
            )
            .flatMap { (eRTCUserId, _, name) ->
                val userDao = userDao
                val userByIdInSync = userDao.getUserByIdInSync(tenantId, userId)
                userByIdInSync.userChatId = eRTCUserId
                userDao.insertWithReplace(userByIdInSync)
                preference.chatUserId = eRTCUserId
                preference.name = name
                // username : <tenantId>/<X-Request-Signature>/<X-Nonce>
                val time = System.currentTimeMillis()
                val requestSignature = HeaderUtils.getHeaderSignature(
                    preference.mqttApiKey!!, preference.packageName!!, time
                )
                val userName = "$tenantId"
                val password = "$requestSignature:$time:${preference.chatToken}"
                mqtt.createConnection(
                    tenantId, eRTCUserId, preference.mqttServer, userName, password, deviceId
                )
                networkManager.api(networkConfig)
                Single.just(Result(true, "", ""))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun markAsRead(
        tenantId: String,
        threadId: String?,
        parentMsgId: String?
    ): Completable =
        eventHandler.markAsRead(threadId, appUserId, parentMsgId)

    override fun blockUnblockUser(
        tenantId: String,
        action: String?,
        appUserId: String?
    ): Single<Result> {
        if (noInternetConnection()) {
            throw UnsupportedOperationException("Network unavailable")
        }
        return networkManager.api()
            .blockUnblockUser(
                tenantId, preference.chatUserId!!, action, BlockUnblockUserRequest(appUserId)
            )
            .map {
                val blockedUser = this.userDao.getUserByIdInSync(tenantId, appUserId)
                if ("block".equals(action, ignoreCase = true)) {
                    blockedUser.blockedStatus = "blocked"
                } else {
                    blockedUser.blockedStatus = "unblocked"
                }
                userDao.update(blockedUser)
                userBlockStatusUpdated(blockedUser)
                Result(true, "", "")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun messageOn(
        tenantId: String,
        threadId: String,
        parentMsgId: String?,
        chatType: ChatType
    ): Observable<NetworkEvent> {
        return eventHandler.source()
            .filter(NetworkEvent.filterType(EventType.MESSAGE_ADDED))
            .filter {
                when (chatType) {
                    ChatType.SINGLE -> chatType.type == it.messageRecord().type && threadId == it.messageRecord().threadId
                    ChatType.GROUP -> chatType.type == it.messageRecord().type && threadId == it.messageRecord().threadId
                    ChatType.SINGLE_CHAT_THREAD -> chatType.type == it.messageRecord().type && it.messageRecord().chatThreadMetadata?.parentMsgId == parentMsgId
                    ChatType.GROUP_CHAT_THREAD -> chatType.type == it.messageRecord().type && it.messageRecord().chatThreadMetadata?.parentMsgId == parentMsgId
                    else -> false
                }
            }
    }

    override fun messageOn(tenantId: String): Observable<NetworkEvent> = eventHandler.source()

    override fun msgReadStatus(
        tenantId: String,
        threadId: String,
        parentMsgId: String?,
        chatType: ChatType
    ): Observable<NetworkEvent> {
        return eventHandler.source()
            .filter(NetworkEvent.filterType(EventType.MESSAGE_UPDATED))
            .filter {
                when (chatType) {
                    ChatType.SINGLE -> chatType.type == it.messageRecord().type && threadId == it.messageRecord().threadId
                    ChatType.GROUP -> chatType.type == it.messageRecord().type && threadId == it.messageRecord().threadId
                    ChatType.SINGLE_CHAT_THREAD -> chatType.type == it.messageRecord().type && it.messageRecord().chatThreadMetadata?.parentMsgId == parentMsgId
                    ChatType.GROUP_CHAT_THREAD -> chatType.type == it.messageRecord().type && it.messageRecord().chatThreadMetadata?.parentMsgId == parentMsgId
                    else -> false
                }
            }
    }

    override fun downloadOn(
        tenantId: String,
        threadId: String,
        parentMsgId: String?,
        chatType: ChatType
    ): Observable<NetworkEvent> {
        val source = eventHandler.source()

        return source
            .filter(NetworkEvent.filterType(EventType.MESSAGE_MEDIA_DOWNLOAD))
            .filter {
                when (chatType) {
                    ChatType.SINGLE -> chatType.type == it.messageRecord().type
                    ChatType.GROUP -> chatType.type == it.messageRecord().type
                    ChatType.SINGLE_CHAT_THREAD -> chatType.type == it.messageRecord().type && it.messageRecord().chatThreadMetadata?.parentMsgId == parentMsgId
                    ChatType.GROUP_CHAT_THREAD -> chatType.type == it.messageRecord().type && it.messageRecord().chatThreadMetadata?.parentMsgId == parentMsgId
                    else -> false
                }
            }
    }

    override fun downloadMedia(
        msgId: String,
        serverUrl: String,
        dirPath: String,
        mediaType: String
    ): Single<Boolean> {
        val fileName = serverUrl.substring(serverUrl.lastIndexOf('/') + 1)
        return Single.create<Boolean> {
            DownloadRequestBuilder(serverUrl, dirPath, fileName, msgId)
                .callback(downloadRepository)
                .eventHandler(eventHandler)
                .build()
                .start()
        }
    }

    override fun chatMetaDataOn(threadId: String): Observable<ChatMetaDataRecord> {
        return eventHandler.source()
            .filter(NetworkEvent.filterType(EventType.THREAD_META_DATA_UPDATED))
            .filter { threadId == it.chatMetaDataRecord().threadId }
            .flatMap { networkEvent ->
                Observable.just(networkEvent.chatMetaDataRecord())
            }
    }

    override fun messageMetaDataOn(threadId: String): Observable<MessageMetaDataRecord> {
        return eventHandler.source()
            .filter(NetworkEvent.filterType(EventType.MESSAGE_META_DATA))
            .filter { threadId == it.messageMetaData().chatReactionRecord?.threadId }
            .flatMap {
                Observable.just(it.messageMetaData())
            }
    }

    override fun sourceOnMain(tenantId: String): Observable<ChatEvent> {
        return eventHandler.sourceOnMain()
    }

    override fun editMessage(threadId: String, message: Message): Single<MessageRecord> {
        return getThread(threadId, message).flatMap { _: Thread? ->
            if (noInternetConnection()) {
                throw UnsupportedOperationException("Network unavailable")
            }

            val msgId =
                if (message.chatType.type == ChatType.SINGLE.type || message.chatType.type == ChatType.GROUP.type) {
                    val singleChat =
                        singleChatDao.getSingleChatByMsgId(message.msgId).singleChat
                    singleChat?.msgUniqueId
                } else {
                    val chatThread = chatThreadDao.getChatByClientId(
                        message.msgId,
                        threadId
                    )
                    chatThread.msgUniqueId
                }

            val messageRequest = MessageRequest(
                msgUniqueId = msgId,
                threadId = threadId,
                message = message.message,
                mentionsList = mentions(message, getMentionedChatUserIds(message))
            )

            networkManager.api()
                .editMessage(
                    tenantId,
                    chatUserId,
                    messageRequest
                ).flatMap { msgUpdateResponse ->
                    if (msgUpdateResponse.replyThreadFeatureData != null) {
                        val chatThread =
                            chatThreadDao.getChatByServerMsgId(
                                msgUpdateResponse.msgUniqueId,
                                threadId
                            )
                        chatThread.updateType = MessageUpdateType.EDIT.type
                        chatThread.message = msgUpdateResponse.message
                        chatThreadDao.update(chatThread)
                        val reactionsFromDatabase =
                            chatReactionDao.getAllThreadReactions(chatThread.id).blockingGet()
                        val reactionRecords = composeReactionList(reactionsFromDatabase)

                        if (msgUpdateResponse.replyThreadFeatureData!!.replyMsgConfig == 1) {
                            val singleChat =
                                singleChatDao.getChatByServerMsgId(
                                    msgUpdateResponse.msgUniqueId,
                                    threadId
                                )
                            val copy = singleChat.copy(
                                message = msgUpdateResponse.message,
                                updateType = MessageUpdateType.EDIT.type
                            )
                            singleChatDao.update(copy)
                            Single.just(
                                transform(
                                    singleChat = copy,
                                    chatReactions = reactionRecords
                                )
                            )
                        } else {
                            Single.just(
                                transform(
                                    chatThread = chatThread,
                                    chatReactions = reactionRecords
                                )
                            )
                        }
                    } else {
                        val singleChat =
                            singleChatDao.getChatByServerMsgId(
                                msgUpdateResponse.msgUniqueId,
                                threadId
                            )
                        singleChat.updateType = MessageUpdateType.EDIT.type
                        singleChat.message = msgUpdateResponse.message
                        singleChatDao.update(singleChat)
                        val reactionsFromDatabase =
                            chatReactionDao.getAllReactions(singleChat.id).blockingGet()
                        val reactionRecords = composeReactionList(reactionsFromDatabase)
                        val chatThreads = chatThreadDao.getChatThreads(singleChat.id).blockingGet()
                        Single.just(
                            transform(
                                singleChat = singleChat,
                                chatThreadList = chatThreads,
                                chatReactions = reactionRecords
                            )
                        )
                    }
                }

        }.doOnError {
            it.printStackTrace()
        }
    }

    override fun editE2EMessage(
        threadId: String,
        message: Message,
        deviceId: String,
        parallelDeviceList: ArrayList<EKeyTable>?
    ): Single<MessageRecord> {
        return getThread(threadId, message).flatMap { thread: Thread ->

            val msgId =
                if (message.chatType.type == ChatType.SINGLE.type || message.chatType.type == ChatType.GROUP.type) {
                    val singleChat = singleChatDao.getSingleChatByMsgId(message.msgId).singleChat
                    singleChat?.msgUniqueId
                } else {
                    val chatThread = chatThreadDao.getChatByClientId(message.msgId, threadId)
                    chatThread.msgUniqueId
                }

            val eKeyTable =
                ekeyDao.getMyLatestPrivateKey(thread?.senderChatId, deviceId, thread?.tenantId)

            val messageRequest = requestEditE2E(
                thread = thread!!,
                message = message,
                msgUniqueId = msgId!!,
                eKeyTable = eKeyTable,
                ekeyDao = ekeyDao,
                groupDao = dataManager.db().groupDao(),
                mentionedChatUserIds = getMentionedChatUserIds(message),
                parallelDeviceList = parallelDeviceList
            )

            networkManager.api()
                .editE2EMessage(
                    tenantId,
                    chatUserId,
                    messageRequest
                ).flatMap { msgUpdateResponse ->

                    val chatStatus = msgUpdateResponse.chatStatus
                    if (chatStatus == null || !chatStatus.retryRequired) {

                        // save key id in case of new key generation
                        if (chatStatus != null && chatStatus.keyList!!.isNotEmpty()) {
                            // my key updated with key_id
                            for (e2eKey in chatStatus.keyList!!) {
                                if (e2eKey.returnCode != null && e2eKey.returnCode.equals("receiverKeyNotValid")) {
                                    //need to remove all data from local DB
                                    ekeyDao.deleteInActiveDevice(e2eKey.eRTCUserId, e2eKey.deviceId)
                                } else {
                                    if (e2eKey.deviceId == deviceId) {
                                        ekeyDao.setKeyId(
                                            preference.chatUserId,
                                            e2eKey.publicKey,
                                            e2eKey.keyId,
                                            eKeyTable.time,
                                            deviceId
                                        )
                                    } else {
                                        //update Remaining keys
                                        val updatedRow: Int = ekeyDao.updateKey(
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
                                                tenantId = tenantId
                                            )
                                            ekeyDao.save(eKeyTableUpdated)
                                        }
                                    }
                                }
                            }
                        }

                        if (msgUpdateResponse.replyThreadFeatureData != null) {
                            val chatThread =
                                chatThreadDao.getChatByServerMsgId(msgId, threadId)
                            chatThread.updateType = MessageUpdateType.EDIT.type
                            chatThread.message = message.message
                            chatThreadDao.update(chatThread)
                            val reactionsFromDatabase =
                                chatReactionDao.getAllThreadReactions(chatThread.id).blockingGet()
                            val reactionRecords = composeReactionList(reactionsFromDatabase)

                            if (msgUpdateResponse.replyThreadFeatureData!!.replyMsgConfig == 1) {
                                val singleChat =
                                    singleChatDao.getChatByServerMsgId(
                                        msgId,
                                        threadId
                                    )
                                val copy = singleChat.copy(
                                    message = message.message,
                                    updateType = MessageUpdateType.EDIT.type
                                )
                                singleChatDao.update(copy)
                                Single.just(
                                    transform(
                                        singleChat = copy,
                                        chatReactions = reactionRecords
                                    )
                                )
                            } else {
                                Single.just(
                                    transform(
                                        chatThread = chatThread,
                                        chatReactions = reactionRecords
                                    )
                                )
                            }
                        } else {
                            val singleChat =
                                singleChatDao.getChatByServerMsgId(msgId, threadId)
                            singleChat.updateType = MessageUpdateType.EDIT.type
                            singleChat.message = message.message
                            singleChatDao.update(singleChat)
                            val reactionsFromDatabase =
                                chatReactionDao.getAllReactions(singleChat.id).blockingGet()
                            val reactionRecords = composeReactionList(reactionsFromDatabase)
                            val chatThreads =
                                chatThreadDao.getChatThreads(singleChat.id).blockingGet()
                            Single.just(
                                transform(
                                    singleChat = singleChat,
                                    chatThreadList = chatThreads,
                                    chatReactions = reactionRecords
                                )
                            )
                        }
                    } else {
                        // add retry using operator
                        val returnCode = chatStatus.returnCode
                        if (!TextUtils.isEmpty(returnCode)) {
                            when (returnCode) {
                                "senderKeyValidityExpired" -> {
                                    Log.i("ChatRepo", "senderKeyValidityExpired")
                                    val keyPair = ECDHUtils.generateKeyPair()
                                    val eKeyTableUpdated = EKeyTable(
                                        deviceId = preference.deviceId!!,
                                        publicKey = keyPair[0],
                                        privateKey = keyPair[1],
                                        ertcUserId = preference.chatUserId!!,
                                        tenantId = tenantId
                                    )
                                    ekeyDao.save(eKeyTableUpdated)
                                    if (chatStatus.keyList!!.isNotEmpty()) {
                                        val newDeviceList: ArrayList<EKeyTable> = ArrayList()
                                        Log.i("ChatRepo", "senderKeyValidityExpired - 1")
                                        for (e2eKey in chatStatus.keyList!!) {
                                            if (e2eKey.returnCode != null && e2eKey.returnCode.equals(
                                                    "receiverKeyNotValid"
                                                )
                                            ) {
                                                //need to remove inactive device from local DB
                                                ekeyDao.deleteInActiveDevice(
                                                    e2eKey.eRTCUserId,
                                                    e2eKey.deviceId
                                                )
                                            } else {
                                                if (e2eKey.deviceId == deviceId) {
                                                    Log.i(
                                                        "ChatRepo",
                                                        "senderKeyValidityExpired - 2"
                                                    )
                                                    ekeyDao.setKeyId(
                                                        preference.chatUserId,
                                                        e2eKey.publicKey,
                                                        e2eKey.keyId,
                                                        eKeyTable.time,
                                                        deviceId
                                                    )
                                                } else {
                                                    Log.i(
                                                        "ChatRepo",
                                                        "senderKeyValidityExpired - 3"
                                                    )
                                                    val updatedRow = ekeyDao.updateKey(
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
                                                            tenantId = tenantId
                                                        )
                                                        ekeyDao.save(eKeyTableUpdated)
                                                    }

                                                    newDeviceList.add(
                                                        EKeyTable(
                                                            keyId = e2eKey.keyId,
                                                            deviceId = e2eKey.deviceId,
                                                            publicKey = e2eKey.publicKey,
                                                            privateKey = "",
                                                            ertcUserId = e2eKey.eRTCUserId,
                                                            tenantId = tenantId
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    return@flatMap editE2EMessage(
                                        threadId,
                                        message,
                                        deviceId
                                    )
                                }
                                "receiverKeyValidationError" -> {
                                    // need to handle
                                    Log.i("ChatRepo", "receiverKeyValidationError")
                                    if (chatStatus.keyList!!.isNotEmpty()) {
                                        Log.i("ChatRepo", "receiverKeyValidationError - 1")
                                        for (e2eKey in chatStatus.keyList!!) {
                                            if (e2eKey.returnCode != null && e2eKey.returnCode.equals(
                                                    "receiverKeyNotValid"
                                                )
                                            ) {
                                                //need to remove inactive device from local DB
                                                ekeyDao.deleteInActiveDevice(
                                                    e2eKey.eRTCUserId,
                                                    e2eKey.deviceId
                                                )
                                            } else {
                                                if (e2eKey.deviceId == deviceId) {
                                                    Log.i(
                                                        "ChatRepo",
                                                        "receiverKeyValidationError - 2"
                                                    )
                                                    ekeyDao.setKeyId(
                                                        preference.chatUserId,
                                                        e2eKey.publicKey,
                                                        e2eKey.keyId,
                                                        eKeyTable.time,
                                                        deviceId
                                                    )
                                                } else {
                                                    Log.i(
                                                        "ChatRepo",
                                                        "receiverKeyValidationError - 3"
                                                    )
                                                    //receiverNewDeviceKeyAvailable is handle here. we will add new device details in eKey table
                                                    val updatedRow = ekeyDao.updateKey(
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
                                                            tenantId = tenantId
                                                        )
                                                        ekeyDao.save(eKeyTableUpdated)
                                                    }
                                                }
                                            }
                                        }
                                        // need to do retry here
                                        return@flatMap editE2EMessage(
                                            threadId,
                                            message,
                                            deviceId
                                        )
                                    }
                                }
                                "senderNewDeviceKeyAvailable" -> {
                                    // need to handle
                                    Log.i("ChatRepo", "senderNewDeviceKeyAvailable")
                                    if (chatStatus.keyList!!.isNotEmpty()) {
                                        val newDeviceList: ArrayList<EKeyTable> = ArrayList()
                                        Log.i("ChatRepo", "senderNewDeviceKeyAvailable - 1")
                                        for (e2eKey in chatStatus.keyList!!) {
                                            if (e2eKey.deviceId == deviceId) {
                                                Log.i("ChatRepo", "senderNewDeviceKeyAvailable - 2")
                                                ekeyDao.setKeyId(
                                                    preference.chatUserId,
                                                    e2eKey.publicKey,
                                                    e2eKey.keyId,
                                                    eKeyTable.time,
                                                    deviceId
                                                )
                                            } else {
                                                Log.i("ChatRepo", "senderNewDeviceKeyAvailable - 3")
                                                val updatedRow = ekeyDao.updateKey(
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
                                                        tenantId = tenantId
                                                    )
                                                    ekeyDao.save(eKeyTableUpdated)
                                                }

                                                newDeviceList.add(
                                                    EKeyTable(
                                                        keyId = e2eKey.keyId,
                                                        deviceId = e2eKey.deviceId,
                                                        publicKey = e2eKey.publicKey,
                                                        privateKey = "",
                                                        ertcUserId = e2eKey.eRTCUserId,
                                                        tenantId = tenantId
                                                    )
                                                )
                                            }
                                        }
                                        // need to do retry here
                                        return@flatMap editE2EMessage(
                                            threadId,
                                            message,
                                            deviceId,
                                            newDeviceList
                                        )
                                    }
                                }
                            }
                        }
                        Single.just(MessageRecord("ID"))
                    }
                }
        }.doOnError {
            it.printStackTrace()
        }
    }

    private fun getThread(id: String, message: Message? = null) = Single.fromCallable {
        threadRepository.getThreadByIdSync(id)
            ?: if (message?.chatType == ChatType.SINGLE || message?.chatType == ChatType.SINGLE_CHAT_THREAD) {
                throw UnsupportedOperationException("User not found")
            } else {
                throw UnsupportedOperationException("Channel not found")
            }
    }

    private fun getThread(id: String, message: MessageRecord) = Single.fromCallable {
        threadRepository.getThreadByIdSync(id)
            ?: if (message.type == ChatType.SINGLE.type || message.type == ChatType.SINGLE_CHAT_THREAD.type) {
                throw UnsupportedOperationException("User not found")
            } else {
                throw UnsupportedOperationException("Channel not found")
            }
    }


    override fun deleteMessage(
        deleteType: String,
        threadId: String,
        messageList: ArrayList<Message>
    ): Single<List<MessageRecord>> {
        return getThread(threadId, messageList.getOrNull(0)).flatMap { _: Thread? ->

            if (noInternetConnection()) {
                throw UnsupportedOperationException("Network unavailable")
            }
            val singleChatDao = dataManager.db().singleChatDao()
            val chatThreadDao = dataManager.db().chatThreadDao()
            val deletedMessages = ArrayList<MessageRecord>()
            val messageListRequest = ArrayList<MessageList>()
            val messageUniqIdList = ArrayList<String>()
            messageList.forEach { message ->
                if (message.chatType.type == ChatType.SINGLE.type || message.chatType.type == ChatType.GROUP.type) {
                    val msgId =
                        singleChatDao.getSingleChatByMsgId(message.parentMsgId).singleChat!!.msgUniqueId!!
                    messageUniqIdList.add(msgId)
                    messageListRequest.add(MessageList(msgId))
                } else {
                    val msgId = chatThreadDao.getChatByClientId(
                        message.parentMsgId,
                        threadId
                    ).msgUniqueId!!
                    messageUniqIdList.add(msgId)
                    messageListRequest.add(MessageList(msgId))
                }
            }
            networkManager.api()
                .deleteMessage(
                    tenantId,
                    chatUserId,
                    MessageRequest(
                        chats = messageListRequest,
                        threadId = threadId,
                        deleteType = deleteType
                    )
                ).flatMap { messageUpdateResponse ->
                    val chatThreadDao = dataManager.db().chatThreadDao()
                    val reactionDao = dataManager.db().chatReactionDao()
                    val data = messageUpdateResponse.chats
                    data?.size?.let {
                        0.until(it).forEach { i ->

                            if (data[i].replyThreadFeatureData != null) {
                                val chatThread = chatThreadDao.getChatByClientId(
                                    messageList[i].parentMsgId,
                                    threadId
                                )

                                if (data[i].replyThreadFeatureData!!.replyMsgConfig == 1) {
                                    val singleChatParent =
                                        singleChatDao!!.getChatByServerMsgId(
                                            data[i].msgUniqueId,
                                            threadId
                                        )
                                    if (deleteType == DeleteType.DELETE_FOR_EVERYONE.type) {
                                        singleChatParent.message =
                                            Constants.Features.THIS_MESSAGE_WAS_DELETED
                                        singleChatParent.deleteType =
                                            DeleteType.DELETE_FOR_EVERYONE.type
                                    } else {
                                        singleChatParent.message =
                                            Constants.Features.THIS_MESSAGE_WAS_DELETED_FOR_YOU
                                        singleChatParent.deleteType =
                                            DeleteType.DELETE_FOR_USER.type
                                    }
                                    singleChatParent.updateType = MessageUpdateType.DELETE.type
                                    singleChatParent.mediaPath = ""
                                    singleChatParent.localMediaPath = ""
                                    singleChatParent.mediaThumbnail = ""
                                    singleChatParent.isStarredChat = "0"
                                    reactionDao.clearAllThreadUserReaction(
                                        chatThread.id,
                                        chatUserId
                                    )
                                    reactionDao.clearAllUserReaction(chatThread.id, chatUserId)
                                    singleChatDao.update(singleChatParent)
                                    transform(singleChat = singleChatParent)
                                        .let { deletedMessages.add(it) }
                                }
                                when (deleteType) {
                                    DeleteType.DELETE_FOR_EVERYONE.type -> {
                                        chatThread.message =
                                            Constants.Features.THIS_MESSAGE_WAS_DELETED
                                        chatThread.deleteType = DeleteType.DELETE_FOR_EVERYONE.type
                                    }
                                    else -> {
                                        chatThread.deleteType = DeleteType.DELETE_FOR_USER.type
                                        chatThread.message =
                                            Constants.Features.THIS_MESSAGE_WAS_DELETED_FOR_YOU
                                    }
                                }
                                chatThread.updateType = MessageUpdateType.DELETE.type
                                chatThread.mediaPath = ""
                                chatThread.localMediaPath = ""
                                chatThread.mediaThumbnail = ""
                                chatThread.isStarredChat = "0"
                                reactionDao.clearAllUserReaction(chatThread.id, chatUserId)
                                chatThreadDao.update(chatThread)
                                transform(chatThread = chatThread).let { deletedMessages.add(it) }
                            } else {
                                val singleChatParent =
                                    singleChatDao!!.getChatByServerMsgId(
                                        data[i].msgUniqueId,
                                        threadId
                                    )
                                when (deleteType) {
                                    DeleteType.DELETE_FOR_EVERYONE.type -> {
                                        singleChatParent.message =
                                            Constants.Features.THIS_MESSAGE_WAS_DELETED
                                        singleChatParent.deleteType =
                                            DeleteType.DELETE_FOR_EVERYONE.type
                                    }
                                    else -> {
                                        singleChatParent.deleteType =
                                            DeleteType.DELETE_FOR_USER.type
                                        singleChatParent.message =
                                            Constants.Features.THIS_MESSAGE_WAS_DELETED_FOR_YOU
                                    }
                                }
                                singleChatParent.updateType = MessageUpdateType.DELETE.type
                                singleChatParent.mediaPath = ""
                                singleChatParent.localMediaPath = ""
                                singleChatParent.mediaThumbnail = ""
                                singleChatParent.isStarredChat = "0"
                                reactionDao.clearAllUserReaction(singleChatParent.id, chatUserId)
                                singleChatDao.update(singleChatParent)
                                transform(singleChat = singleChatParent)
                                    .let { deletedMessages.add(it) }
                            }
                        }
                    }
                    Single.just(deletedMessages.toList())
                }
        }.doOnError {
            it.printStackTrace()
        }
    }

    override fun sendReaction(
        threadId: String,
        messageMetaDataRecord: MessageMetaData
    ): Single<ChatReactionRecord> {
        if (noInternetConnection()) {
            throw UnsupportedOperationException("Network unavailable")
        }

        if (messageMetaDataRecord.reaction == null) {
            throw UnsupportedOperationException("reaction can't be null")
        }
        val reaction: Reaction = messageMetaDataRecord.reaction

        if (reaction.parentMsgId != null && reaction.parentMsgId.isNotEmpty()) {
            // thread message
            return chatThreadDao.getChatByClientIdBySingleCount(reaction.msgId, threadId)
                .flatMap { list ->
                    if (list.isNullOrEmpty()) {
                        throw UnsupportedOperationException("Please wait for message to send to add reaction")
                    }
                    val chatThread = list[0]
                    if (chatThread.status == MessageStatus.SENDING.status || chatThread.status == MessageStatus.FAILED.status) {
                        throw UnsupportedOperationException("Please wait for message to send to add reaction")
                    }
                    networkManager.api()
                        .sendReaction(
                            tenantId,
                            chatUserId,
                            ChatReactionRequest(
                                chatThread.msgUniqueId,
                                reaction.emojiCode,
                                reaction.chatReactionType.type
                            )
                        )
                        .flatMap {
                            if (reaction.chatReactionType == ChatReactionType.SET) {
                                if (reaction.sendToChannel == 1) {
                                    chatReactionDao.insertWithReplace(
                                        transform(
                                            unicode = reaction.emojiCode,
                                            threadId = threadId,
                                            chatMsgId = chatThread.id,
                                            userChatId = chatUserId
                                        )
                                    )
                                }
                                chatReactionDao.insertWithReplace(
                                    transform(
                                        unicode = reaction.emojiCode,
                                        threadId = threadId,
                                        chatThreadMsgId = chatThread.id,
                                        userChatId = chatUserId
                                    )
                                )
                            } else {
                                if (reaction.sendToChannel == 1) {
                                    chatReactionDao.clearChatUserReaction(
                                        chatThread.id,
                                        reaction.emojiCode,
                                        chatUserId
                                    )
                                }
                                chatReactionDao.clearChatThreadUserReaction(
                                    chatThread.id,
                                    reaction.emojiCode,
                                    chatUserId
                                )
                            }

                            val chatThreadReactions =
                                chatReactionDao.getChatThreadReactionsCount(
                                    reaction.msgId,
                                    reaction.emojiCode
                                )
                                    .blockingGet()
                            val chatReactionRecord = composeReactionList(chatThreadReactions)
                            val userRecordList = if (chatReactionRecord.isNullOrEmpty()) {
                                null
                            } else {
                                chatReactionRecord[0].userRecord
                            }

                            Single.just(
                                ChatReactionRecord(
                                    threadId = threadId,
                                    chatReactionType = reaction.chatReactionType,
                                    msgUniqueId = reaction.msgId,
                                    emojiCode = reaction.emojiCode,
                                    count = chatThreadReactions.size,
                                    userRecord = userRecordList
                                )
                            )
                        }.doOnError {
                            it.printStackTrace()
                        }
                }.subscribeOn(Schedulers.io())
        } else {
            // normal message
            return singleChatDao.getChatByMsgIdCount(reaction.msgId).flatMap { list ->
                if (list.isNullOrEmpty()) {
                    throw UnsupportedOperationException("Please wait for message to send to add reaction")
                }
                val singleChat = list[0]
                if (singleChat.status == MessageStatus.SENDING.status || singleChat.status == MessageStatus.FAILED.status) {
                    throw UnsupportedOperationException("Please wait for message to send to add reaction")
                }
                networkManager.api()
                    .sendReaction(
                        tenantId,
                        chatUserId,
                        ChatReactionRequest(
                            singleChat.msgUniqueId,
                            reaction.emojiCode,
                            reaction.chatReactionType.type
                        )
                    )
                    .flatMap {
                        if (reaction.chatReactionType == ChatReactionType.SET) {
                            chatReactionDao.insertWithReplace(
                                transform(
                                    unicode = reaction.emojiCode,
                                    threadId = threadId,
                                    chatMsgId = singleChat.id,
                                    userChatId = chatUserId
                                )
                            )
                        } else {
                            chatReactionDao.clearChatUserReaction(
                                singleChat.id,
                                reaction.emojiCode,
                                chatUserId
                            )
                        }

                        val chatThreadReactions =
                            chatReactionDao.getChatReactionsCount(
                                reaction.msgId,
                                reaction.emojiCode
                            )
                                .blockingGet()
                        val chatReactionRecord = composeReactionList(chatThreadReactions)
                        val userRecordList = if (chatReactionRecord.isNullOrEmpty()) {
                            null
                        } else {
                            chatReactionRecord[0].userRecord
                        }

                        Single.just(
                            ChatReactionRecord(
                                threadId = threadId,
                                chatReactionType = reaction.chatReactionType,
                                msgUniqueId = reaction.msgId,
                                emojiCode = reaction.emojiCode,
                                count = chatThreadReactions.size,
                                userRecord = userRecordList
                            )
                        )
                    }
            }.subscribeOn(Schedulers.io())
        }
    }

    override fun forwardChat(
        tenantId: String,
        messageList: List<Message>,
        userList: List<UserRecord>,
        groupList: List<GroupRecord>,
        deviceId: String,
        chatType: ChatType,
        customData: String?,
        isE2E: Boolean
    ): Single<Result> {

        return Single.create {
            val messageRequests = ArrayList<MessageRequest>()

            if (messageList.isEmpty())
                throw UnsupportedOperationException("Message list is empty")

            if (noInternetConnection()) {
                throw UnsupportedOperationException("Network unavailable")
            }

            val message = messageList[0]
            var singleChat: SingleChat? = null
            var chatThread: ChatThread? = null
            message.clientCreatedAt = System.currentTimeMillis()
            if (chatType == ChatType.SINGLE || chatType == ChatType.GROUP) {
                val list = singleChatDao.getChatByMsgIdCount(message.forwardMsgId).blockingGet()
                if (list.isNullOrEmpty()) {
                    throw UnsupportedOperationException("No such message found")
                }
                singleChat = list[0]
                if (singleChat.status == MessageStatus.SENDING.status || singleChat.status == MessageStatus.FAILED.status) {
                    throw UnsupportedOperationException("Please wait for message to send to forward message")
                }

                var mediaReq: MediaReq? = null
                if (message.file != null || message.media != null) {
                    var mediaPath = singleChat.mediaPath
                    var mediaThumbnailPath = singleChat.mediaThumbnail
                    mediaPath = preference.chatServer?.let { it1 -> mediaPath?.replace(it1, "") }
                    mediaThumbnailPath =
                        preference.chatServer?.let { it1 -> mediaThumbnailPath?.replace(it1, "") }
                    if (mediaThumbnailPath.isNullOrEmpty()) {
                        mediaThumbnailPath = null
                    }
                    mediaReq =
                        MediaReq(mediaPath, mediaThumbnailPath, singleChat.mediaName)
                }
                val forwardChat = ForwardChat(singleChat.msgUniqueId)
                if (userList.isNotEmpty()) {
                    userList.forEach { user ->
                        if (isE2E) {
                            // here we have to handle e2e for forward chat
                            var threadId =
                                dataManager.db().threadDao().getThreadIdByRecipientId(user.id)
                                    .blockingGet()
                            if (threadId == null) {
                                val currentUser = userDao.getUserByIdInSync(tenantId, appUserId)
                                val recipientUser = userDao.getUserByIdInSync(tenantId, user.id)
                                threadId = createThread(tenantId, currentUser, recipientUser)
                                forwardE2EMessage(
                                    message = message.copy(chatType = ChatType.SINGLE),
                                    tenantId = tenantId,
                                    threadId = threadId,
                                    deviceId = deviceId,
                                    customData = customData,
                                    forwardChat = forwardChat
                                )
                            } else {
                                forwardE2EMessage(
                                    message = message.copy(chatType = ChatType.SINGLE),
                                    tenantId = tenantId,
                                    threadId = threadId,
                                    deviceId = deviceId,
                                    customData = customData,
                                    forwardChat = forwardChat
                                )
                            }
                        } else {
                            //val threadList = dataManager.db().threadDao().getThreadIdByRecipientUserId(user.id)
                            val msgId = UUID.randomUUID().toString()
                            val msgRequest = request(
                                message = message,
                                msgId = msgId,
                                forwardChat = forwardChat,
                                senderId = chatUserId,
                                recipientAppUserId = user.id,
                                mediaReq = mediaReq,
                                customData = customData
                            )
                            messageRequests.add(msgRequest)
                        }
                    }
                }

                if (groupList.isNotEmpty()) {
                    for (group in groupList) {
                        if (isE2E) {
                            // here we have to handle e2e for forward chat
                            forwardE2EMessage(
                                message = message.copy(chatType = ChatType.GROUP),
                                tenantId = tenantId,
                                threadId = group.threadId,
                                deviceId = deviceId,
                                customData = customData,
                                forwardChat = forwardChat
                            )
                        } else {
                            val msgId = UUID.randomUUID().toString()
                            val msgRequest = request(
                                message = message,
                                msgId = msgId,
                                forwardChat = forwardChat,
                                senderId = chatUserId,
                                threadId = group.threadId,
                                mediaReq = mediaReq,
                                customData = customData
                            )
                            messageRequests.add(msgRequest)
                        }
                    }
                }
            } else {
                val list = chatThreadDao.getChatByMsgIdCount(message.forwardMsgId).blockingGet()
                if (list.isNullOrEmpty()) {
                    throw UnsupportedOperationException("Please wait for message to send to forward message")
                }
                chatThread = list[0]
                if (chatThread.status == MessageStatus.SENDING.status || chatThread.status == MessageStatus.FAILED.status) {
                    throw UnsupportedOperationException("Please wait for message to send to forward message")
                }

                var mediaReq: MediaReq? = null
                if (message.file != null || message.media != null) {
                    var mediaPath = chatThread.mediaPath
                    var mediaThumbnailPath = chatThread.mediaThumbnail
                    mediaPath = preference.chatServer?.let { it1 -> mediaPath?.replace(it1, "") }
                    mediaThumbnailPath =
                        preference.chatServer?.let { it1 -> mediaThumbnailPath?.replace(it1, "") }
                    if (mediaThumbnailPath.isNullOrEmpty()) {
                        mediaThumbnailPath = null
                    }
                    mediaReq = MediaReq(mediaPath, mediaThumbnailPath, chatThread.mediaName)
                }
                val forwardChat = ForwardChat(chatThread.msgUniqueId)
                if (userList.isNotEmpty()) {
                    userList.forEach { user ->
                        if (isE2E) {
                            // here we have to handle e2e for forward chat
                            val threadId =
                                dataManager.db().threadDao().getThreadIdByRecipientId(user.id)
                                    .blockingGet()
                            if (threadId == null) {
                                val currentUser = userDao.getUserByIdInSync(tenantId, appUserId)
                                val recipientUser = userDao.getUserByIdInSync(tenantId, user.id)
                                val nextThreadId =
                                    createThread(tenantId, currentUser, recipientUser)
                                forwardE2EMessage(
                                    message = message.copy(chatType = ChatType.SINGLE),
                                    tenantId = tenantId,
                                    threadId = nextThreadId,
                                    deviceId = deviceId,
                                    customData = customData,
                                    forwardChat = forwardChat
                                )
                            } else {
                                forwardE2EMessage(
                                    message = message.copy(chatType = ChatType.SINGLE),
                                    tenantId = tenantId,
                                    threadId = threadId,
                                    deviceId = deviceId,
                                    customData = customData,
                                    forwardChat = forwardChat
                                )
                            }
                        } else {
                            //val threadList = dataManager.db().threadDao().getThreadIdByRecipientUserId(user.id)
                            val msgId = UUID.randomUUID().toString()
                            val msgRequest = request(
                                message = message,
                                msgId = msgId,
                                forwardChat = forwardChat,
                                senderId = chatUserId,
                                recipientAppUserId = user.id,
                                mediaReq = mediaReq,
                                customData = customData
                            )
                            messageRequests.add(msgRequest)
                        }
                    }
                }

                if (groupList.isNotEmpty()) {
                    for (group in groupList) {
                        if (isE2E) {
                            // here we have to handle e2e for forward chat
                            forwardE2EMessage(
                                message = message.copy(chatType = ChatType.GROUP),
                                tenantId = tenantId,
                                threadId = group.threadId,
                                deviceId = deviceId,
                                customData = customData,
                                forwardChat = forwardChat
                            )
                        } else {
                            val msgId = UUID.randomUUID().toString()
                            val msgRequest = request(
                                message = message,
                                msgId = msgId,
                                forwardChat = forwardChat,
                                senderId = chatUserId,
                                threadId = group.threadId,
                                mediaReq = mediaReq,
                                customData = customData
                            )
                            messageRequests.add(msgRequest)
                        }
                    }
                }
            }

            if (isE2E) {
                it.onSuccess(Result(true))
            } else {
                val apiKey = dataManager.preference().apiKey
                val response =
                    networkManager.api()
                        .forwardChat(apiKey!!, tenantId, chatUserId, messageRequests)
                        .blockingGet()
                if (response.messageResponse.isNullOrEmpty()) {
                    it.onSuccess(Result(false))
                } else {
                    response.messageResponse.forEach { messageResponse ->
                        val threadUserLinks =
                            dataManager.db().threadUserLinkDao()
                                .hasThread(messageResponse.threadId!!)
                        if (threadUserLinks.size <= 0) {
                            var recipientChatId = ""
                            var recipientAppUserId = ""

                            for (participant in messageResponse.thread?.participantsList!!) {
                                if (!TextUtils.isEmpty(participant.eRTCRecipientId)
                                    && participant.eRTCRecipientId != chatUserId
                                ) {
                                    val userByIdInSync =
                                        userDao.getUserByIdInSync(tenantId, participant.appUserId)
                                    userByIdInSync.userChatId = participant.eRTCRecipientId
                                    userDao.insertWithReplace(userByIdInSync)
                                    recipientChatId = participant.eRTCRecipientId.toString()
                                    recipientAppUserId = participant.appUserId.toString()
                                }
                            }

                            dataManager.db().threadDao().insertWithReplace(
                                ThreadMapper.from(
                                    messageResponse.threadId,
                                    chatUserId,
                                    tenantId,
                                    appUserId,
                                    recipientAppUserId,
                                    recipientChatId,
                                    NotificationSettingsType.ALL.mute,
                                    0,
                                    SettingAppliedFor.ALWAYS.duration,
                                    messageResponse.createdAt
                                )
                            )

                            dataManager.db().threadUserLinkDao().insertWithReplace(
                                ThreadMapper.from(
                                    appUserId,
                                    recipientAppUserId,
                                    messageResponse.threadId
                                )
                            )
                        }

                        if (chatType == ChatType.SINGLE || chatType == ChatType.GROUP) {
                            // Create thread
                            val singleChatData = singleChat?.copy(
                                id = messageResponse.data?.requestId!!,
                                threadId = messageResponse.threadId,
                                msgUniqueId = messageResponse.msgUniqueId,
                                read = 0,
                                senderAppUserId = appUserId,
                                type = messageResponse.thread?.threadType,
                                chatEventType = OUTGOING.type,
                                forwardMsgUniqueId = singleChat.msgUniqueId,
                                isForwardMessage = 1,
                                isStarredChat = "0",
                                parentMsgId = "",
                                parentMsg = "",
                                createdAt = message.clientCreatedAt,
                                clientCreatedAt = message.clientCreatedAt,
                                customData = customData,
                                isFollowed = 1
                            )
                            singleChatDao.insertWithReplace(singleChatData)
                        } else {
                            val msgChatType = if (chatType == ChatType.SINGLE_CHAT_THREAD) {
                                ChatType.SINGLE.type
                            } else {
                                ChatType.GROUP.type
                            }

                            val singleChat = chatThread?.let { it1 ->
                                transform(
                                    chatThread = it1,
                                    msgResponse = messageResponse,
                                    senderAppUserId = appUserId,
                                    clientCreatedAt = message.clientCreatedAt,
                                    customData = customData
                                )
                            }
                            singleChatDao.insertWithReplace(singleChat)
                        }
                    }
                    it.onSuccess(Result(true))
                }
            }
        }
    }

    override fun getMessage(
        threadId: String,
        msgId: String
    ): Single<MessageRecord> {
        return singleChatDao.getChatByClientIdByRxSingle(msgId, threadId)
            .flatMap { singleChat ->
                val appUserId = appUserId

                val eventType = if (singleChat.senderAppUserId != appUserId) {
                    INCOMING
                } else {
                    OUTGOING
                }

                Single.just(transform(singleChat = singleChat, chatEventType = eventType))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getChatThreadMessage(
        threadId: String,
        msgId: String
    ): Single<MessageRecord> {
        return chatThreadDao.getChatByClientIdBySingle(msgId, threadId)
            .flatMap { chatThread ->
                val appUserId = appUserId

                val eventType = if (chatThread.senderAppUserId != appUserId) {
                    INCOMING
                } else {
                    OUTGOING
                }

                Single.just(transform(chatThread = chatThread, chatEventType = eventType))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun searchMessages(
        searchedText: String
    ): Single<ArrayList<MessageRecord>> {
        return Single.zip(
            singleChatDao.searchedMessageList(searchedText),
            chatThreadDao.searchedMessageList(searchedText),
            BiFunction { singleChatList: List<SingleChat>, chatThreadList: List<ChatThread> ->
                val messageRecords = ArrayList<MessageRecord>()
                val appUserId = appUserId
                for (singleChat in singleChatList) {
                    var senderUserRecord: UserRecord? = null
                    var receiverUserRecord: UserRecord? = null
                    var groupRecord: GroupRecord? = null
                    val eventType = if (singleChat.chatEventType != null &&
                        singleChat.chatEventType == ChatEventType.CHAT_META_DATA.type
                    ) {
                        ChatEventType.CHAT_META_DATA
                    } else if (singleChat.senderAppUserId != appUserId) {
                        INCOMING
                    } else {
                        OUTGOING
                    }

                    if (singleChat.type.equals(ChatType.GROUP.type)) {
                        groupRecord = GroupMapper.transform(
                            dataManager.db().groupDao()
                                .getGroupByThreadId(tenantId, singleChat.threadId),
                            tenantId,
                            appUserId
                        )
                    } else {
                        val userId =
                            dataManager.db().threadDao().getUserIdByThreadId(singleChat.threadId)
                        if (eventType == INCOMING) {
                            senderUserRecord =
                                UserMapper.transform(userDao.getUserByIdInSync(tenantId, userId))
                        } else if (eventType == OUTGOING) {
                            receiverUserRecord =
                                UserMapper.transform(userDao.getUserByIdInSync(tenantId, userId))
                        }
                    }

                    messageRecords.add(
                        transform(
                            singleChat = singleChat,
                            senderRecord = senderUserRecord,
                            receiverRecord = receiverUserRecord,
                            groupRecord = groupRecord,
                            chatEventType = eventType
                        )
                    )
                }

                for (chatThread in chatThreadList) {
                    var senderUserRecord: UserRecord? = null
                    var receiverUserRecord: UserRecord? = null
                    var groupRecord: GroupRecord? = null
                    val eventType = if (chatThread.senderAppUserId != appUserId) {
                        INCOMING
                    } else {
                        OUTGOING
                    }

                    if (chatThread.type.equals(ChatType.GROUP_CHAT_THREAD.type)) {
                        groupRecord = GroupMapper.transform(
                            dataManager.db().groupDao()
                                .getGroupByThreadId(tenantId, chatThread.threadId),
                            tenantId,
                            appUserId
                        )
                    } else {
                        val userId =
                            dataManager.db().threadDao().getUserIdByThreadId(chatThread.threadId)
                        if (eventType == INCOMING) {
                            senderUserRecord =
                                UserMapper.transform(userDao.getUserByIdInSync(tenantId, userId))
                        } else if (eventType == OUTGOING) {
                            receiverUserRecord =
                                UserMapper.transform(userDao.getUserByIdInSync(tenantId, userId))
                        }
                    }

                    messageRecords.add(
                        transform(
                            chatThread = chatThread,
                            senderRecord = senderUserRecord,
                            receiverRecord = receiverUserRecord,
                            groupRecord = groupRecord,
                            chatEventType = eventType
                        )
                    )
                }
                Single.just(messageRecords)
            }
        ).flatMap { messageRecords: Single<ArrayList<MessageRecord>> -> messageRecords }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun globalSearch(
        searchedText: String,
        threadId: String?
    ): Single<ArrayList<MessageRecord>> {
        if (noInternetConnection()) {
            throw UnsupportedOperationException("Network unavailable")
        }
        return networkManager.api().globalSearch(
            tenantId,
            chatUserId,
            SearchRequest(SearchQuery(searchedText, threadId), listOf("messages"))
        )
            .flatMap { searchChatResponse ->
                val messageRecords = ArrayList<MessageRecord>()
                if (searchChatResponse.searchMessageResponse.total == 0) {
                    return@flatMap Single.just(messageRecords)
                }

                for (searchedMessageResponse in searchChatResponse.searchMessageResponse.messageResponse) {
                    if (searchedMessageResponse.replyThreadFeatureData != null &&
                        searchedMessageResponse.replyThreadFeatureData?.replyMsgConfig == 0
                    ) {
                        continue
                    }
                    var senderUserRecord: UserRecord? = null
                    var receiverUserRecord: UserRecord? = null
                    var groupRecord: GroupRecord? = null
                    var threadType: String = ChatType.SINGLE.type
                    val eventType = if (searchedMessageResponse.sendereRTCUserId != chatUserId) {
                        INCOMING
                    } else {
                        OUTGOING
                    }

                    for (threadResponse in searchChatResponse.searchMessageResponse.threads) {
                        if (searchedMessageResponse.threadId.equals(threadResponse.threadId)) {
                            if (threadResponse.user != null) {
                                if (threadResponse.threadType == ChatType.GROUP.type) {
                                    threadType = ChatType.GROUP.type
                                    groupRecord = GroupMapper.transform(
                                        dataManager.db().groupDao()
                                            .getGroupByThreadId(tenantId, threadResponse.threadId),
                                        tenantId,
                                        chatUserId
                                    )
                                } else {
                                    val userId = threadResponse.user?.appUserId
                                    if (eventType == INCOMING) {
                                        senderUserRecord = UserMapper.transform(
                                            userDao.getUserByIdInSync(
                                                tenantId,
                                                userId
                                            )
                                        )
                                    } else if (eventType == OUTGOING) {
                                        receiverUserRecord = UserMapper.transform(
                                            userDao.getUserByIdInSync(
                                                tenantId,
                                                userId
                                            )
                                        )
                                    }
                                }

                                messageRecords.add(
                                    transform(
                                        messageResponse = searchedMessageResponse,
                                        senderRecord = senderUserRecord,
                                        receiverRecord = receiverUserRecord,
                                        groupRecord = groupRecord,
                                        threadType = threadType,
                                        chatEventType = eventType
                                    )
                                )
                            }
                            break
                        }
                    }
                }
                Single.just(messageRecords)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun followThread(
        threadId: String,
        messageRecord: MessageRecord,
        isFollowed: Boolean
    ): Single<MessageRecord> {
        return getThread(threadId, messageRecord).flatMap { _: Thread? ->
            if (noInternetConnection()) {
                throw UnsupportedOperationException("Network unavailable")
            }

            val msgId =
                if (messageRecord.type == ChatType.SINGLE.type || messageRecord.type == ChatType.GROUP.type) {
                    val singleChat =
                        singleChatDao.getSingleChatByMsgId(messageRecord.id).singleChat
                    singleChat?.msgUniqueId
                } else {
                    val chatThread = chatThreadDao.getChatByClientId(
                        messageRecord.id,
                        threadId
                    )
                    chatThread.msgUniqueId
                }

            networkManager.api()
                .followThread(
                    tenantId,
                    chatUserId,
                    FollowThreadRequest(msgUniqueId = msgId, threadId = threadId, isFollowed)
                ).flatMap { msgUpdateResponse ->
                    val singleChat =
                        singleChatDao.getChatByServerMsgId(msgUpdateResponse.msgUniqueId, threadId)
                    singleChat.isFollowed = if (isFollowed) 1 else 0
                    singleChatDao.update(singleChat)
                    val eventType = if (singleChat.senderAppUserId != appUserId) {
                        INCOMING
                    } else {
                        OUTGOING
                    }
                    val reactionsFromDatabase =
                        chatReactionDao.getAllReactions(singleChat.id).blockingGet()
                    val reactionRecords = composeReactionList(reactionsFromDatabase)
                    val chatThreads = chatThreadDao.getChatThreads(singleChat.id).blockingGet()
                    Single.just(
                        transform(
                            singleChat = singleChat,
                            chatThreadList = chatThreads,
                            chatReactions = reactionRecords,
                            chatEventType = eventType
                        )
                    )
                }

        }.doOnError {
            it.printStackTrace()
        }
    }

    override fun followThread(
        threadId: String,
        messageId: String,
        isFollowed: Boolean
    ): Single<Result> {
        return networkManager.api()
            .followThread(
                tenantId,
                chatUserId,
                FollowThreadRequest(msgUniqueId = messageId, threadId = threadId, isFollowed)
            ).flatMap { response ->
                singleChatDao.setFollowThread(if (isFollowed) 1 else 0, messageId)
                Single.just(Result(true, response.message, ""))
            }
    }

    override fun reportMessage(
        threadId: String,
        messageRecord: MessageRecord,
        reportType: String,
        reason: String
    ): Single<MessageRecord> {
        return getThread(threadId, messageRecord).flatMap { _: Thread? ->
            val msgId =
                if (messageRecord.type == ChatType.SINGLE.type || messageRecord.type == ChatType.GROUP.type) {
                    val singleChat =
                        singleChatDao.getSingleChatByMsgId(messageRecord.id).singleChat
                    singleChat?.msgUniqueId
                } else {
                    val chatThread = chatThreadDao.getChatByClientId(
                        messageRecord.id,
                        threadId
                    )
                    chatThread.msgUniqueId
                }

            networkManager.api()
                .reportMessage(
                    tenantId,
                    chatUserId,
                    ReportMessageRequest(msgUniqueId = msgId, category = reportType, reason)
                ).flatMap { msgReportResponse ->
                    if (messageRecord.chatThreadMetadata != null) {
                        if (messageRecord.chatThreadMetadata!!.sendToChannel == 1) {
                            val singleChat = singleChatDao.getChatByServerMsgId(msgId, threadId)
                            singleChat.chatReportId = msgReportResponse.chatReportId
                            singleChat.reportType = reportType
                            singleChat.reason = reason
                            singleChatDao.update(singleChat)
                            val reactionsFromDatabase =
                                chatReactionDao.getAllReactions(singleChat.id).blockingGet()
                            val reactionRecords = composeReactionList(reactionsFromDatabase)
                            Single.just(
                                transform(
                                    singleChat = singleChat,
                                    chatReactions = reactionRecords
                                )
                            )
                        }
                        val chatThread = chatThreadDao.getChatByServerMsgId(msgId, threadId)
                        chatThread.chatReportId = msgReportResponse.chatReportId
                        chatThread.reportType = reportType
                        chatThread.reason = reason
                        chatThreadDao.update(chatThread)
                        val reactionsFromDatabase =
                            chatReactionDao.getAllThreadReactions(chatThread.id).blockingGet()
                        val reactionRecords = composeReactionList(reactionsFromDatabase)
                        Single.just(
                            transform(
                                chatThread = chatThread,
                                chatReactions = reactionRecords
                            )
                        )
                    } else {
                        val singleChat = singleChatDao.getChatByServerMsgId(msgId, threadId)
                        singleChat.chatReportId = msgReportResponse.chatReportId
                        singleChat.reportType = reportType
                        singleChat.reason = reason
                        singleChatDao.update(singleChat)
                        val reactionsFromDatabase =
                            chatReactionDao.getAllReactions(singleChat.id).blockingGet()
                        val reactionRecords = composeReactionList(reactionsFromDatabase)
                        Single.just(
                            transform(
                                singleChat = singleChat,
                                chatReactions = reactionRecords
                            )
                        )
                    }
                }

        }.doOnError {
            it.printStackTrace()
        }
    }

    override fun clearChat(threadId: String): Single<Result> {
        return dataManager.db().tenantDao()
            .getTenantConfigValue(preference.tenantId, TenantConfig.E2E_CHAT)
            .flatMap { value: String ->
                if (value == "1") {
                    singleChatDao.deleteByThreadId(threadId)
                    chatThreadDao.deleteByThreadId(threadId)
                    chatReactionDao.deleteByThreadId(threadId)
                    Single.just(Result(true, "Chat cleared", ""))
                } else {
                    networkManager.api()
                        .clearChatHistory(tenantId, chatUserId, threadId)
                        .flatMap { response ->
                            singleChatDao.deleteByThreadId(threadId)
                            chatThreadDao.deleteByThreadId(threadId)
                            chatReactionDao.deleteByThreadId(threadId)
                            Single.just(Result(true, "Chat cleared", ""))
                        }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun sendMessageAgain(
        threadId: String,
        msgId: String,
        parentMsgId: String?
    ): Single<MessageRecord> {
        return getThread(threadId).flatMap { thread: Thread? ->

            if (thread == null) {
                throw UnsupportedOperationException("There is no thread with this threadId")
            }

            var replyThread: ReplyThread? = null
            val singleChatDao = dataManager.db().singleChatDao()
            val chatThreadDao = dataManager.db().chatThreadDao()

            val response: Single<MessageResponse>
            response = if (parentMsgId != null && parentMsgId.isNotEmpty()) {
                val singleChatParent = singleChatDao.getChatByLocalMsgId(parentMsgId, threadId)
                val threadChat = chatThreadDao.getChatByClientId(msgId, threadId)
                val sendToChannel = threadChat.sendToChannel
                replyThread = ReplyThread(singleChatParent.msgUniqueId, sendToChannel)

                if (threadChat.mediaPath == null || threadChat.msgType == null) {
                    throw UnsupportedOperationException("Media is undefined")
                }

                networkManager.api()
                    .sendMedia(
                        tenantId,
                        threadId,
                        thread.senderChatId,
                        threadChat.mediaPath!!,
                        msgId,
                        threadChat.msgType!!,
                        preference.chatUserId!!,
                        replyThread,
                        Utils.getMimeType(threadChat.mediaPath!!, threadChat.msgType!!),
                        threadChat.clientCreatedAt,
                        customData = threadChat.customData
                    )
            } else {
                val singleChat = singleChatDao.getChatByLocalMsgId(msgId, threadId)
                if (singleChat.sendToChannel != null && singleChat.sendToChannel == 1) {
                    val singleChatParent =
                        singleChatDao.getChatByLocalMsgId(singleChat.parentMsgId, threadId)
                    replyThread =
                        ReplyThread(singleChatParent.msgUniqueId, singleChat.sendToChannel)
                }

                if (singleChat.mediaPath == null || singleChat.msgType == null) {
                    throw UnsupportedOperationException("Media is undefined")
                }

                networkManager.api()
                    .sendMedia(
                        tenantId,
                        threadId,
                        thread.senderChatId,
                        singleChat.mediaPath!!,
                        msgId,
                        singleChat.msgType!!,
                        preference.chatUserId!!,
                        replyThread,
                        Utils.getMimeType(singleChat.mediaPath!!, singleChat.msgType!!),
                        singleChat.clientCreatedAt,
                        customData = singleChat.customData
                    )
            }
            response.flatMap { messageResponse ->

                val msgId1 = messageResponse.data?.requestId
                if (messageResponse.replyThreadFeatureData?.parentMsgId != null && messageResponse.replyThreadFeatureData?.parentMsgId!!.isNotEmpty()) {

                    val chatThread = chatThreadDao.getChatByClientId(msgId1, threadId)
                    chatThread.msgUniqueId = messageResponse.msgUniqueId
                    chatThread.status = SENT.status
                    if (messageResponse.media != null) {
                        chatThread.mediaName = messageResponse.media?.name
                        chatThread.mediaPath =
                            dataManager.preference()?.chatServer + messageResponse.media?.path
                        chatThread.mediaThumbnail =
                            dataManager.preference()?.chatServer + messageResponse.media?.thumbnail
                    }
                    Logger.d(
                        "thread_debug ###",
                        "API : METADATA_ID : $msgId1 ::: Thread_Id : $threadId"
                    )
                    Logger.d("thread_debug ###", "API : chatThread : $chatThread")
                    chatThreadDao.insertWithReplace(chatThread)

                    if (messageResponse.replyThreadFeatureData?.replyMsgConfig == 1) {
                        val singleChat = singleChatDao.getChatByLocalMsgId(msgId1, threadId)
                        singleChat.msgUniqueId = messageResponse.msgUniqueId
                        singleChat.status = SENT.status
                        if (messageResponse.media != null) {
                            singleChat.mediaName = messageResponse.media?.name
                            singleChat.mediaPath =
                                dataManager.preference()?.chatServer + messageResponse.media?.path
                            singleChat.mediaThumbnail =
                                dataManager.preference()?.chatServer + messageResponse.media?.thumbnail
                        }
                        singleChatDao.insertWithReplace(singleChat)
                        Logger.d("thread_debug ###", "API : singleChat : $singleChat")
                        // Here both main and chat thread should be given back to the app
                        Single.just(transform(chatThread = chatThread))
                        //}
                    } else {
                        Single.just(transform(chatThread = chatThread))
                    }
                    //}.doOnError { it.printStackTrace() }
                } else {
                    singleChatDao.getChatByClientIdByRxSingle(msgId1, threadId)
                        .flatMap { singleChat ->
                            singleChat.msgUniqueId = messageResponse.msgUniqueId
                            singleChat.status = SENT.status
                            if (messageResponse.media != null) {
                                singleChat.mediaName = messageResponse.media?.name
                                singleChat.mediaPath =
                                    dataManager.preference()?.chatServer + messageResponse.media?.path
                                singleChat.mediaThumbnail =
                                    dataManager.preference()?.chatServer + messageResponse.media?.thumbnail
                            }
                            singleChatDao.insertWithReplace(singleChat)
                            Single.just(transform(singleChat = singleChat))
                        }
                }

            }.onErrorReturn {
                it.cause?.let { cause ->
                    if (cause.toString() == "java.net.SocketTimeoutException: timeout") {
                        singleChatDao.setStatus(MessageStatus.FAILED.status, msgId)
                        chatThreadDao.setStatus(MessageStatus.FAILED.status, msgId)

                        if (parentMsgId != null && parentMsgId.isNotEmpty()) {
                            return@onErrorReturn transform(
                                chatThread = chatThreadDao.getChatByClientId(
                                    msgId,
                                    threadId
                                )
                            )
                        } else {
                            return@onErrorReturn transform(
                                singleChat = singleChatDao.getChatByLocalMsgId(
                                    msgId,
                                    threadId
                                )
                            )
                        }
                    }
                }
                throw it
            }.doOnError {
                it.printStackTrace()
                it.message?.let { error ->
                    if (error.contains(Constants.ErrorMessage.ERROR_403)) {
                        singleChatDao.deleteByMsgId(msgId)
                        chatThreadDao.deleteByMsgId(msgId)
                    }
                }
            }
        }.doOnError {
            it.printStackTrace()
        }
    }

    override fun getMediaGallery(
        tenantId: String,
        threadId: String,
        msgType: String,
        currentMsgId: String?,
        direction: String?,
        pageSize: Int?
    ): Single<MutableList<MessageRecord>> {
        return remote.getMediaGallery(
            tenantId,
            threadId,
            msgType,
            currentMsgId,
            direction,
            pageSize
        )
    }

    override fun forwardMediaChat(
        tenantId: String,
        message: Message,
        userList: List<UserRecord>,
        groupList: List<GroupRecord>,
        deviceId: String,
        chatType: ChatType,
        customData: String?
    ): Single<Result> {
        return Single.create {
            val messageRequests = ArrayList<MessageRequest>()

            if (noInternetConnection()) {
                throw UnsupportedOperationException("Network unavailable")
            }

            message.clientCreatedAt = System.currentTimeMillis()
            if (chatType == ChatType.SINGLE || chatType == ChatType.GROUP) {
                var mediaReq: MediaReq? = null
                if (message.file != null || message.media != null) {
                    var mediaPath = message.media?.mediaPath
                    var mediaThumbnailPath = message.media?.mediaPath
                    mediaPath = preference.chatServer?.let { it1 -> mediaPath?.replace(it1, "") }
                    mediaThumbnailPath =
                        preference.chatServer?.let { it1 -> mediaThumbnailPath?.replace(it1, "") }
                    if (mediaThumbnailPath.isNullOrEmpty()) {
                        mediaThumbnailPath = null
                    }
                    mediaReq =
                        MediaReq(mediaPath, mediaThumbnailPath,
                            mediaPath?.let { path -> Utils.getFileName(path) })
                }
                val forwardChat = ForwardChat(message.forwardMsgId)
                if (userList.isNotEmpty()) {
                    userList.forEach { user ->
                        //val threadList = dataManager.db().threadDao().getThreadIdByRecipientUserId(user.id)
                        val msgId = UUID.randomUUID().toString()
                        val msgRequest = request(
                            message = message,
                            msgId = msgId,
                            forwardChat = forwardChat,
                            senderId = chatUserId,
                            recipientAppUserId = user.id,
                            mediaReq = mediaReq,
                            customData = customData
                        )
                        messageRequests.add(msgRequest)
                    }
                }

                if (groupList.isNotEmpty()) {
                    for (group in groupList) {
                        val msgId = UUID.randomUUID().toString()
                        val msgRequest = request(
                            message = message,
                            msgId = msgId,
                            forwardChat = forwardChat,
                            senderId = chatUserId,
                            threadId = group.threadId,
                            mediaReq = mediaReq,
                            customData = customData
                        )
                        messageRequests.add(msgRequest)
                    }
                }
            }

            val apiKey = dataManager.preference().apiKey
            val response =
                networkManager.api().forwardChat(apiKey!!, tenantId, chatUserId, messageRequests)
                    .blockingGet()
            if (response.messageResponse.isNullOrEmpty()) {
                it.onSuccess(Result(false))
            } else {
                response.messageResponse.forEach { messageResponse ->
                    val threadUserLinks =
                        dataManager.db().threadUserLinkDao().hasThread(messageResponse.threadId!!)
                    if (threadUserLinks.size <= 0) {
                        var recipientChatId = ""
                        var recipientAppUserId = ""

                        for (participant in messageResponse.thread?.participantsList!!) {
                            if (!TextUtils.isEmpty(participant.eRTCRecipientId)
                                && participant.eRTCRecipientId != chatUserId
                            ) {
                                val userByIdInSync =
                                    userDao.getUserByIdInSync(tenantId, participant.appUserId)
                                userByIdInSync.userChatId = participant.eRTCRecipientId
                                userDao.insertWithReplace(userByIdInSync)
                                recipientChatId = participant.eRTCRecipientId.toString()
                                recipientAppUserId = participant.appUserId.toString()
                            }
                        }

                        dataManager.db().threadDao().insertWithReplace(
                            ThreadMapper.from(
                                messageResponse.threadId,
                                chatUserId,
                                tenantId,
                                appUserId,
                                recipientAppUserId,
                                recipientChatId,
                                NotificationSettingsType.ALL.mute,
                                0,
                                SettingAppliedFor.ALWAYS.duration,
                                messageResponse.createdAt
                            )
                        )

                        dataManager.db().threadUserLinkDao().insertWithReplace(
                            ThreadMapper.from(
                                appUserId,
                                recipientAppUserId,
                                messageResponse.threadId
                            )
                        )
                    }

                    if (chatType == ChatType.SINGLE || chatType == ChatType.GROUP) {
                        val singleChatData = SingleChat(
                            id = messageResponse.data?.requestId!!,
                            threadId = messageResponse.threadId,
                            msgUniqueId = messageResponse.msgUniqueId,
                            read = 0,
                            senderAppUserId = appUserId,
                            type = chatType.type,
                            status = MessageStatus.SENT.status,
                            msgType = messageResponse.msgType,
                            mediaPath = message.media?.mediaPath,
                            mediaThumbnail = message.media?.mediaPath,
                            mediaName = message.media?.let { media -> Utils.getFileName(media.mediaPath) },
                            chatEventType = OUTGOING.type,
                            forwardMsgUniqueId = message.forwardMsgId,
                            isForwardMessage = 1,
                            isStarredChat = "0",
                            createdAt = message.clientCreatedAt,
                            clientCreatedAt = message.clientCreatedAt,
                            customData = customData,
                            isFollowed = 1
                        )
                        singleChatDao.insertWithReplace(singleChatData)
                    }
                }
                it.onSuccess(Result(true))
            }
        }
    }

    override fun copyMessage(activity: Activity, message: String): Single<String> {
        var copiedMessage = message
        activity.runOnUiThread {
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            if (Utils.copyPattern.containsMatchIn(message)) {
                copiedMessage = Utils.pattern.replace(message) { matchResult ->
                    matchResult.value.substring(1, matchResult.value.length - 1)
                }
            }

            val clip: ClipData = ClipData.newPlainText("Copied", copiedMessage)
            clipboard.setPrimaryClip(clip)
        }
        return Single.just(copiedMessage)
    }

    override fun getFollowThreads(
        threadId: String?,
        currentMsgId: String?,
        followedThread: Boolean,
        direction: String?,
        pageSize: Int?
    ): Single<MutableList<FollowThreadRecord>> {
        return remote.getFollowThreads(threadId, currentMsgId, followedThread, direction, pageSize)
    }

    override fun isChatRestored(): Boolean {
        return !preference.skipRestoreFlag
    }

    override fun getChatSettings(): Single<ChatSettingsRecord> {
        return networkManager.api()
            .getChatSettings(tenantId, chatUserId)
            .flatMap { response: ChatSettingsResponse ->
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
                        dataManager.db().profanityFilterDao().insertWithReplace(profanityData)
                    }
                }
                Single.just(ChatSettingsRecord(null, null))
            }
    }

    override fun getProfanityFilters(): Flowable<MutableList<ProfanityDataRecord>> {
        return profanityFilterDao.getProfanityFilter(tenantId)
            .flatMap { list ->
                val profanityList: MutableList<ProfanityDataRecord> = ArrayList()
                for (data in list) {
                    profanityList.add(ProfanityDataRecord(data.keyword, data.actionType))
                }
                Flowable.just(profanityList)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getDomainFilters(): Flowable<MutableList<DomainDataRecord>> {
        return domainFilterDao.getDomainFilter(tenantId)
            .flatMap { list ->
                val domainList: MutableList<DomainDataRecord> = ArrayList()
                for (data in list) {
                    domainList.add(DomainDataRecord(data.domain, data.actionType))
                }
                Flowable.just(domainList)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getParentMessage(singleChat: SingleChat): String {
        if (singleChat.message != null) {
            return singleChat.message!!
        } else if (singleChat.mediaPath != null) {
            return if (singleChat.mediaName != null) {
                singleChat.mediaName!!
            } else {
                singleChat.mediaPath!!.substring(singleChat.mediaPath!!.lastIndexOf('/') + 1)
            }
        } else if (singleChat.gifPath != null) {
            return singleChat.gifPath!!.substring(singleChat.gifPath!!.lastIndexOf('/') + 1)
        } else if (singleChat.contactName != null) {
            return singleChat.contactName!!
        } else if (singleChat.location != null && singleChat.location!!.address != null) {
            return singleChat.location!!.address!!
        } else {
            return singleChat.message!!
        }
    }

    private fun noInternetConnection(): Boolean {
        return try {
            // Connect to Google DNS to check for connection
            val timeoutMs = 1500
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)

            socket.connect(socketAddress, timeoutMs)
            socket.close()

            false
        } catch (ex: IOException) {
            true
        }
    }

    private fun userBlockStatusUpdated(user: User) {
        val userWrapper = UserWrapper(dataManager)
        val disposableList = DisposableList()

        userWrapper
            .userBlockStatusUpdatedBySelf(user)
            .doOnNext(Consumer { userRecord ->
                eventHandler.source().onNext(NetworkEvent.userMetaDataUpdated(userRecord))
            })
            .subscribe(CrashReporter<UserRecord>(disposableList))
    }

    private fun forwardE2EMessage(
        message: Message,
        tenantId: String,
        threadId: String,
        deviceId: String,
        parallelDeviceList: ArrayList<EKeyTable>? = null,
        customData: String? = null,
        forwardChat: ForwardChat? = null
    ): Boolean {
        val thread = threadRepository.getThreadByIdSync(threadId)
        val msgId = UUID.randomUUID().toString()
        message.clientCreatedAt = System.currentTimeMillis()

        singleChatDao.insertWithReplace(
            getChatRow(
                thread,
                message,
                msgId,
                customData = customData,
                forwardChat = forwardChat
            )
        )

        val eKeyTable =
            ekeyDao.getMyLatestPrivateKey(thread?.senderChatId, deviceId, thread?.tenantId)

        val e2eRequest = requestE2E(
            thread!!, message, msgId, null, eKeyTable, ekeyDao, dataManager.db().groupDao(),
            getMentionedChatUserIds(message), parallelDeviceList, customData, forwardChat
        )

        val response = networkManager.api()
            .sendE2EMessage(tenantId, e2eRequest, Objects.requireNonNull(preference.chatUserId)!!)
            .blockingGet()

        val msgId1 = response.data?.requestId
        val chatStatus = response.chatStatus
        if (chatStatus == null || !chatStatus.retryRequired) {

            // save key id in case of new key generation
            if (chatStatus != null && chatStatus.keyList!!.isNotEmpty()) {
                // my key updated with key_id
                for (e2eKey in chatStatus.keyList!!) {
                    if (e2eKey.returnCode != null && e2eKey.returnCode.equals("receiverKeyNotValid")) {
                        //need to remove all data from local DB
                        ekeyDao.deleteInActiveDevice(e2eKey.eRTCUserId, e2eKey.deviceId)
                    } else {
                        if (e2eKey.deviceId == deviceId) {
                            ekeyDao.setKeyId(
                                preference.chatUserId,
                                e2eKey.publicKey,
                                e2eKey.keyId,
                                eKeyTable.time,
                                deviceId
                            )
                        } else {
                            //update Remaining keys
                            val updatedRow: Int = ekeyDao.updateKey(
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
                                    tenantId = tenantId
                                )
                                ekeyDao.save(eKeyTableUpdated)
                            }
                        }
                    }
                }
            }

            val singleChat = singleChatDao.getChatByLocalMsgId(msgId1, threadId)
            singleChat.msgUniqueId = response.msgUniqueId
            singleChat.status = SENT.status
            singleChatDao.insertWithReplace(singleChat)
            return true
        } else {
            // add retry using operator
            val returnCode = chatStatus.returnCode
            if (!TextUtils.isEmpty(returnCode)) {
                when (returnCode) {
                    "senderKeyValidityExpired" -> {
                        Log.i("ChatRepo", "senderKeyValidityExpired")
                        val keyPair = ECDHUtils.generateKeyPair()
                        val eKeyTableUpdated = EKeyTable(
                            deviceId = preference.deviceId!!,
                            publicKey = keyPair[0],
                            privateKey = keyPair[1],
                            ertcUserId = preference.chatUserId!!,
                            tenantId = tenantId
                        )
                        ekeyDao.save(eKeyTableUpdated)
                        // need to do retry here
                        singleChatDao.deleteByMsgId(msgId)
                        chatThreadDao.deleteByMsgId(msgId)
                        if (chatStatus.keyList!!.isNotEmpty()) {
                            val newDeviceList: ArrayList<EKeyTable> = ArrayList()
                            Log.i("ChatRepo", "senderKeyValidityExpired - 1")
                            for (e2eKey in chatStatus.keyList!!) {
                                if (e2eKey.returnCode != null && e2eKey.returnCode.equals("receiverKeyNotValid")) {
                                    //need to remove inactive device from local DB
                                    ekeyDao.deleteInActiveDevice(e2eKey.eRTCUserId, e2eKey.deviceId)
                                } else {
                                    if (e2eKey.deviceId == deviceId) {
                                        Log.i("ChatRepo", "senderKeyValidityExpired - 2")
                                        ekeyDao.setKeyId(
                                            preference.chatUserId,
                                            e2eKey.publicKey,
                                            e2eKey.keyId,
                                            eKeyTable.time,
                                            deviceId
                                        )
                                    } else {
                                        Log.i("ChatRepo", "senderKeyValidityExpired - 3")
                                        val updatedRow = ekeyDao.updateKey(
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
                                                tenantId = tenantId
                                            )
                                            ekeyDao.save(eKeyTableUpdated)
                                        }

                                        newDeviceList.add(
                                            EKeyTable(
                                                keyId = e2eKey.keyId,
                                                deviceId = e2eKey.deviceId,
                                                publicKey = e2eKey.publicKey,
                                                privateKey = "",
                                                ertcUserId = e2eKey.eRTCUserId,
                                                tenantId = tenantId
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        return forwardE2EMessage(
                            message,
                            tenantId,
                            threadId,
                            deviceId,
                            customData = customData,
                            forwardChat = forwardChat
                        )
                    }
                    "receiverKeyValidationError" -> {
                        // need to handle
                        Log.i("ChatRepo", "receiverKeyValidationError")
                        if (chatStatus.keyList!!.isNotEmpty()) {
                            Log.i("ChatRepo", "receiverKeyValidationError - 1")
                            for (e2eKey in chatStatus.keyList!!) {
                                if (e2eKey.returnCode != null && e2eKey.returnCode.equals("receiverKeyNotValid")) {
                                    //need to remove inactive device from local DB
                                    ekeyDao.deleteInActiveDevice(e2eKey.eRTCUserId, e2eKey.deviceId)
                                } else {
                                    if (e2eKey.deviceId == deviceId) {
                                        Log.i("ChatRepo", "receiverKeyValidationError - 2")
                                        ekeyDao.setKeyId(
                                            preference.chatUserId,
                                            e2eKey.publicKey,
                                            e2eKey.keyId,
                                            eKeyTable.time,
                                            deviceId
                                        )
                                    } else {
                                        Log.i("ChatRepo", "receiverKeyValidationError - 3")
                                        //receiverNewDeviceKeyAvailable is handle here. we will add new device details in eKey table
                                        val updatedRow = ekeyDao.updateKey(
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
                                                tenantId = tenantId
                                            )
                                            ekeyDao.save(eKeyTableUpdated)
                                        }
                                    }
                                }
                            }
                            // need to do retry here
                            singleChatDao.deleteByMsgId(msgId)
                            chatThreadDao.deleteByMsgId(msgId)
                            return forwardE2EMessage(
                                message,
                                tenantId,
                                threadId,
                                deviceId,
                                customData = customData
                            )
                        }
                    }
                    "senderNewDeviceKeyAvailable" -> {
                        // need to handle
                        Log.i("ChatRepo", "senderNewDeviceKeyAvailable")
                        if (chatStatus.keyList!!.isNotEmpty()) {
                            val newDeviceList: ArrayList<EKeyTable> = ArrayList()
                            Log.i("ChatRepo", "senderNewDeviceKeyAvailable - 1")
                            for (e2eKey in chatStatus.keyList!!) {
                                if (e2eKey.deviceId == deviceId) {
                                    Log.i("ChatRepo", "senderNewDeviceKeyAvailable - 2")
                                    ekeyDao.setKeyId(
                                        preference.chatUserId,
                                        e2eKey.publicKey,
                                        e2eKey.keyId,
                                        eKeyTable.time,
                                        deviceId
                                    )
                                } else {
                                    Log.i("ChatRepo", "senderNewDeviceKeyAvailable - 3")
                                    val updatedRow = ekeyDao.updateKey(
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
                                            tenantId = tenantId
                                        )
                                        ekeyDao.save(eKeyTableUpdated)
                                    }

                                    newDeviceList.add(
                                        EKeyTable(
                                            keyId = e2eKey.keyId,
                                            deviceId = e2eKey.deviceId,
                                            publicKey = e2eKey.publicKey,
                                            privateKey = "",
                                            ertcUserId = e2eKey.eRTCUserId,
                                            tenantId = tenantId
                                        )
                                    )
                                }
                            }
                            // need to do retry here
                            singleChatDao.deleteByMsgId(msgId)
                            chatThreadDao.deleteByMsgId(msgId)
                            return forwardE2EMessage(
                                message,
                                tenantId,
                                threadId,
                                deviceId,
                                newDeviceList,
                                customData = customData
                            )
                        }
                    }
                }
            }
            return true
        }
    }

    private fun createThread(
        tenantId: String?,
        currentUser: User?,
        recipientUser: User?
    ): String {
        val chatUserId = data().preference().chatUserId
        val response = dataManager
            .network()
            .api()
            .createThread(
                tenantId!!,
                CreateThreadRequest(Objects.requireNonNull(chatUserId)!!, recipientUser!!.id),
                chatUserId!!
            ).blockingGet()

        insertThreadData(response, chatUserId, tenantId, currentUser!!, recipientUser)

        val e2eKeys = response.e2eKeys
        if (e2eKeys != null && e2eKeys.isNotEmpty()) {
            for (e2EKey in e2eKeys) {
                if (e2EKey.deviceId == data().preference().deviceId) {
                    //you can update keyId here but now don't need to update, once we implement things for parallel device
                } else {
                    //update Remaining keys
                    val updatedRow: Int = data().db().ekeyDao().updateKey(
                        e2EKey.eRTCUserId,
                        e2EKey.publicKey,
                        e2EKey.keyId,
                        e2EKey.deviceId,
                        System.currentTimeMillis()
                    )

                    if (updatedRow == 0) {
                        val eKeyTableUpdated = EKeyTable(
                            keyId = e2EKey.keyId,
                            deviceId = e2EKey.deviceId,
                            publicKey = e2EKey.publicKey,
                            privateKey = "",
                            ertcUserId = e2EKey.eRTCUserId,
                            tenantId = tenantId
                        )
                        data().db().ekeyDao().save(eKeyTableUpdated)
                    }
                }
            }
        }
        return response.threadId
    }

    private fun insertThreadData(
        response: CreateThreadResponse,
        chatUserId: String,
        tenantId: String,
        currentUser: User,
        recipientUser: User
    ) {
        val participantsList = response.participantsList
        var recipientAppUserId = response.recipientAppUserId
        val userDao = dataManager.db().userDao()
        var recipientChatId = ""
        var muteSettings = NotificationSettingsType.ALL.mute
        var validTillValue = SettingAppliedFor.ALWAYS.duration

        for (participant in participantsList) {
            if ((!TextUtils.isEmpty(participant.eRTCRecipientId)
                        && participant.eRTCRecipientId != chatUserId) || (!TextUtils.isEmpty(
                    participant.eRTCUserId
                )
                        && participant.eRTCUserId != chatUserId)
            ) {
                if (recipientAppUserId.isNullOrEmpty()) {
                    recipientAppUserId = participant.appUserId
                }
                val userByIdInSync =
                    userDao.getUserByIdInSync(tenantId, recipientAppUserId)
                if (participant.eRTCRecipientId != null) {
                    userByIdInSync.userChatId = participant.eRTCRecipientId
                } else if (participant.eRTCUserId != null) {
                    userByIdInSync.userChatId = participant.eRTCUserId
                }
                recipientChatId = userByIdInSync.userChatId.toString()
                userDao.insertWithReplace(userByIdInSync)

            }

            if (participant.notificationSettings != null
                && participant.eRTCRecipientId == chatUserId
            ) {
                muteSettings = participant.notificationSettings!!.allowFrom
            } else if (participant.notificationSettings != null
                && participant.eRTCUserId == chatUserId
            ) {
                muteSettings = participant.notificationSettings!!.allowFrom
            }
        }

        val thread = ThreadMapper.from(
            response,
            chatUserId,
            tenantId,
            currentUser,
            recipientUser,
            recipientChatId,
            muteSettings,
            0,
            validTillValue
        )
        dataManager.db().threadDao().insertWithReplace(thread)

        val threadUserLinkDao = ThreadMapper.from(
            currentUser.id, recipientUser.id, response.threadId
        )
        dataManager.db().threadUserLinkDao().insertWithReplace(threadUserLinkDao)
    }
}