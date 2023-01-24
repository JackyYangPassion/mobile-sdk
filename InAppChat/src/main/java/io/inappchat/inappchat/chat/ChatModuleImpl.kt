package io.inappchat.inappchat.chat

import android.app.Activity
import android.content.Context
import io.inappchat.inappchat.chat.mapper.ChatEvent
import io.inappchat.inappchat.chat.mapper.ChatMetaDataRecord
import io.inappchat.inappchat.chat.mapper.ChatReactionRecord
import io.inappchat.inappchat.chat.mapper.ChatSettingsRecord
import io.inappchat.inappchat.chat.mapper.DomainDataRecord
import io.inappchat.inappchat.chat.mapper.FollowThreadRecord
import io.inappchat.inappchat.chat.mapper.MessageMetaDataRecord
import io.inappchat.inappchat.chat.mapper.MessageRecord
import io.inappchat.inappchat.chat.mapper.ProfanityDataRecord
import io.inappchat.inappchat.chat.model.Message
import io.inappchat.inappchat.chat.model.MessageMetaData
import io.inappchat.inappchat.chat.repository.ChatRepository
import io.inappchat.inappchat.chat.repository.ChatRepositoryImpl.Companion.newInstance
import io.inappchat.inappchat.chat.repository.ChatRestoreRepository
import io.inappchat.inappchat.chat.repository.ChatRestoreRepositoryImpl
import io.inappchat.inappchat.chat.repository.FavoriteMsgRepositoryImpl
import io.inappchat.inappchat.chat.repository.FavoriteMsgRepository
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.NetworkEvent
import io.inappchat.inappchat.core.type.RestoreType
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.download.DownloadRepositoryImpl
import io.inappchat.inappchat.group.mapper.GroupRecord
import io.inappchat.inappchat.module.BaseModule
import io.inappchat.inappchat.thread.handler.ThreadRepository
import io.inappchat.inappchat.thread.handler.ThreadRepositoryImpl
import io.inappchat.inappchat.thread.mapper.ThreadRecord
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.user.repository.UserRepositoryImpl
import io.inappchat.inappchat.cache.database.entity.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.json.JSONArray

/** @author meeth
 */
class ChatModuleImpl private constructor(
    dataManager: DataManager,
    private val chatRepository: ChatRepository,
    private val threadRepository: ThreadRepository,
    private val favortieMsgRespository: FavoriteMsgRepository,
    private val chatRestoreRepository: ChatRestoreRepository
) : BaseModule(dataManager), ChatModule {

    override fun getThreads(): Flowable<List<ThreadRecord>> {
        return withTenant<Flowable<List<ThreadRecord>>>(
            { tenantId: String? -> threadRepository.getThreads(tenantId!!) }
        ) { Flowable.empty() }
    }

    override fun createThread(recipientId: String): Single<String> {
        val userDao = data().db().userDao()
        val currentUser =
            userDao.getUserById(tenantId, appUserId)
        var recipientUser =
            userDao.getUserById(tenantId, recipientId)
        return userDao.hasUser(tenantId, recipientId).flatMap { recipientUserList ->
            if (recipientUserList.size <= 0) {
                recipientUser = Single.just(
                    User(
                        id = recipientId,
                        tenantId = tenantId,
                        name = "Third party user"
                    )
                )
            }
            currentUser.flatMap { user ->
                recipientUser.flatMap { recipient ->
                    threadRepository.createThread(
                        tenantId,
                        user,
                        recipient
                    )
                }
            }
        }
    }

    override fun getMessages(
        threadId: String,
        currentMsgId: String?,
        direction: String?,
        pageSize: Int?,
        isGlobalSearched: Boolean?
    ): Single<MutableList<MessageRecord>> {
        return chatRepository.getMessages(
            tenantId,
            threadId,
            currentMsgId,
            direction,
            pageSize,
            isGlobalSearched
        )
    }

    override fun getChatThreadMessages(
        threadId: String,
        parentMsgId: String?
    ): Single<MutableList<MessageRecord>> {
        return chatRepository.getChatThreadMessages(tenantId, threadId, parentMsgId!!)
    }

    override fun sendMessage(
        threadId: String,
        message: Message,
        customData: String?,
        isReplyThreadDisabled: Boolean?,
        isUserMentions: Boolean?
    ): Single<MessageRecord> {
        return chatRepository.sendMessage(message, tenantId, threadId, deviceId, customData)
    }

    override fun sendE2EMessage(
        threadId: String,
        message: Message,
        customData: String?
    ): Single<MessageRecord> {
        return chatRepository.sendE2EMessage(
            message,
            tenantId,
            threadId,
            deviceId,
            customData = customData
        )
    }

    override fun hasThread(): Single<Boolean> {
        return Single.just(java.lang.Boolean.FALSE)
    }

    override fun getChatUserId(): Single<Result> {
        return chatRepository.getChatUserId(
            tenantId, appUserId, fcmToken,
            deviceId
        )
    }

    override fun markAsRead(
        threadId: String,
        parentMsgId: String?
    ): Completable {
        return chatRepository.markAsRead(tenantId, threadId, parentMsgId)
    }

    // Publish subject
    override fun messageOn(
        threadId: String,
        parentMsgId: String?,
        chatType: ChatType
    ): Observable<NetworkEvent> {
        return chatRepository.messageOn(tenantId, threadId, parentMsgId, chatType)
    }

    override fun messageOn(): Observable<NetworkEvent> {
        return chatRepository.messageOn(tenantId)
    }


    override fun msgReadStatus(
        threadId: String,
        parentMsgId: String?,
        chatType: ChatType
    ): Observable<NetworkEvent> {
        return chatRepository.msgReadStatus(tenantId, threadId, parentMsgId, chatType)
    }

    override fun blockUnblock(
        action: String,
        appUserId: String
    ): Single<Result> {
        return chatRepository.blockUnblockUser(tenantId, action, appUserId)
    }

    override fun markAsFavorite(
        threadId: String,
        list: List<MessageRecord>,
        isFavorite: Boolean
    ): Single<List<MessageRecord>> {
        return favortieMsgRespository.markAsFavorite(tenantId, threadId, list, isFavorite)
    }

    override fun getAllFavoriteMessages(): Observable<List<MessageRecord>> {
        return favortieMsgRespository.getAllFavoriteMessages(tenantId)!!
    }

    override fun getAllFavoriteMessages(threadId: String): Observable<List<MessageRecord>> {
        return favortieMsgRespository.getAllFavoriteMessages(tenantId, threadId)!!
    }

    override fun getAllFavoriteMessages(
        threadId: String,
        parentMsgId: String
    ): Flowable<List<MessageRecord>> {
        return favortieMsgRespository.getAllFavoriteMessages(tenantId, threadId, parentMsgId)!!
    }

    override fun downloadMedia(
        msgId: String,
        serverUrl: String,
        dirPath: String,
        mediaType: String
    ): Single<Boolean> {
        return chatRepository.downloadMedia(msgId, serverUrl, dirPath, mediaType)
    }

    override fun downloadOn(
        threadId: String,
        parentMsgId: String?,
        chatType: ChatType
    ): Observable<NetworkEvent> {
        return chatRepository.downloadOn(tenantId, threadId, parentMsgId, chatType)
    }

    override fun chatMetaDataOn(threadId: String): Observable<ChatMetaDataRecord> {
        return chatRepository.chatMetaDataOn(threadId)
    }

    override fun messageMetaDataOn(threadId: String): Observable<MessageMetaDataRecord> {
        return chatRepository.messageMetaDataOn(threadId)
    }

    override fun sendReaction(
        threadId: String,
        messageMetaData: MessageMetaData
    ): Single<ChatReactionRecord> {
        return chatRepository.sendReaction(threadId, messageMetaData)
    }

    override fun sourceOnMain(): Observable<ChatEvent> {
        return chatRepository.sourceOnMain(tenantId)
    }

    override fun forwardChat(
        messageList: List<Message>,
        userList: List<UserRecord>,
        groupList: List<GroupRecord>,
        chatType: ChatType,
        customData: String?,
        isE2E: Boolean
    ): Single<Result> {
        return chatRepository.forwardChat(
            tenantId,
            messageList,
            userList,
            groupList,
            deviceId,
            chatType,
            customData,
            isE2E
        )
    }

    override fun getMessage(
        threadId: String,
        msgId: String
    ): Single<MessageRecord> {
        return chatRepository.getMessage(threadId, msgId)
    }

    override fun getChatThreadMessage(
        threadId: String,
        msgId: String
    ): Single<MessageRecord> {
        return chatRepository.getChatThreadMessage(threadId, msgId)
    }

    override fun deleteMessage(
        deleteType: String,
        threadId: String,
        messageList: ArrayList<Message>
    ): Single<List<MessageRecord>> {
        return chatRepository.deleteMessage(deleteType, threadId, messageList)
    }

    override fun editMessage(
        threadId: String,
        message: Message,
        isUserMentions: Boolean?
    ): Single<MessageRecord> {
        return chatRepository.editMessage(threadId, message)
    }

    override fun editE2EMessage(
        threadId: String,
        message: Message,
        isUserMentions: Boolean?
    ): Single<MessageRecord> {
        return chatRepository.editE2EMessage(threadId, message, deviceId)
    }

    override fun chatRestore(): Single<Result> {
        val chatUserId = _getChatUserId()
        if (chatUserId.isNullOrEmpty()) {
            throw UnsupportedOperationException("Chat user id can't be NULL")
        }
        if (tenantId.isNullOrEmpty()) {
            throw UnsupportedOperationException("Tenant id can't be NULL")
        }
        return chatRestoreRepository.chatRestore()
    }

    override fun chatSkipRestore(): Single<Result> {
        return chatRestoreRepository.chatSkipRestore()
    }

    override fun searchMessages(
        searchedText: String
    ): Single<ArrayList<MessageRecord>> {
        return chatRepository.searchMessages(searchedText)
    }

    override fun globalSearch(
        searchedText: String,
        threadId: String?
    ): Single<ArrayList<MessageRecord>> {
        return chatRepository.globalSearch(searchedText, threadId)
    }

    override fun restore(restoreType: RestoreType): Single<Result> {
        val chatUserId = _getChatUserId()
        if (chatUserId.isNullOrEmpty()) {
            throw UnsupportedOperationException("Chat user id can't be NULL")
        }
        if (tenantId.isNullOrEmpty()) {
            throw UnsupportedOperationException("Tenant id can't be NULL")
        }
        return chatRestoreRepository.restore(restoreType)
    }

    override fun followThread(
        threadId: String,
        messageRecord: MessageRecord,
        isFollowed: Boolean
    ): Single<MessageRecord> {
        return chatRepository.followThread(threadId, messageRecord, isFollowed)
    }

    override fun followThread(
        threadId: String,
        messageId: String,
        isFollowed: Boolean
    ): Single<Result> {
        return chatRepository.followThread(threadId, messageId, isFollowed)
    }

    override fun reportMessage(
        threadId: String,
        messageRecord: MessageRecord,
        reportType: String,
        reason: String
    ): Single<MessageRecord> {
        return chatRepository.reportMessage(threadId, messageRecord, reportType, reason)
    }

    override fun clearChat(threadId: String): Single<Result> {
        return chatRepository.clearChat(threadId)
    }

    override fun sendMessageAgain(
        threadId: String,
        msgId: String,
        parentMsgId: String?
    ): Single<MessageRecord> {
        return chatRepository.sendMessageAgain(threadId, msgId, parentMsgId)
    }

    override fun getMediaGallery(
        threadId: String,
        currentMsgId: String?,
        direction: String?,
        pageSize: Int?
    ): Single<MutableList<MessageRecord>> {
        val mediaType = JSONArray()
        mediaType.put("image")
        mediaType.put("video")
        mediaType.put("audio")
        return chatRepository.getMediaGallery(
            tenantId,
            threadId,
            mediaType.toString(),
            currentMsgId,
            direction,
            pageSize
        )
    }

    override fun forwardMediaChat(
        message: Message,
        userList: List<UserRecord>,
        groupList: List<GroupRecord>,
        chatType: ChatType,
        customData: String?
    ): Single<Result> {
        return chatRepository.forwardMediaChat(
            tenantId,
            message,
            userList,
            groupList,
            deviceId,
            chatType,
            customData
        )
    }

    override fun copyMessage(activity: Activity, message: String): Single<String> {
        return chatRepository.copyMessage(activity, message)
    }

    override fun getFollowThreads(
        threadId: String?,
        currentMsgId: String?,
        followedThread: Boolean,
        direction: String?,
        pageSize: Int?
    ): Single<MutableList<FollowThreadRecord>> {
        return chatRepository.getFollowThreads(
            threadId,
            currentMsgId,
            followedThread,
            direction,
            pageSize
        )
    }

    override fun isChatRestored(): Boolean {
        return chatRepository.isChatRestored()
    }

    override fun getChatSettings(): Single<ChatSettingsRecord> {
        return chatRepository.getChatSettings()
    }

    override fun getProfanityFilters(): Flowable<MutableList<ProfanityDataRecord>> {
        return chatRepository.getProfanityFilters()
    }

    override fun getDomainFilters(): Flowable<MutableList<DomainDataRecord>> {
        return chatRepository.getDomainFilters()
    }

    override fun getThread(threadId: String): Single<ThreadRecord> {
        return threadRepository.getThread(tenantId, threadId)
    }

    companion object {
        fun newInstance(
            dataManager: DataManager,
            eventHandler: EventHandler?
        ): ChatModule {

            val userRepository = UserRepositoryImpl.newInstance(dataManager, eventHandler!!)
            val threadRepository = ThreadRepositoryImpl.newInstance(dataManager)
            val downloadRepository = DownloadRepositoryImpl.newInstance(dataManager)
            val chatRepository = newInstance(
                dataManager,
                threadRepository,
                eventHandler,
                downloadRepository
            )
            val chatRestoreRepository = ChatRestoreRepositoryImpl.newInstance(
                dataManager,
                threadRepository,
                userRepository,
                eventHandler,
                downloadRepository
            )

            val favMsgRepository =
                FavoriteMsgRepositoryImpl.newInstance(dataManager)
            return ChatModuleImpl(
                dataManager,
                chatRepository,
                threadRepository,
                favMsgRepository,
                chatRestoreRepository
            )
        }
    }

}