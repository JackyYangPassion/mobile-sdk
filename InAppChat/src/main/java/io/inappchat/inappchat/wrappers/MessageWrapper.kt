package io.inappchat.inappchat.wrappers

import com.google.gson.Gson
import io.inappchat.inappchat.cache.database.entity.ChatReactionEntity
import io.inappchat.inappchat.chat.mapper.ChatEntityMapper
import io.inappchat.inappchat.chat.mapper.ChatReactionRecord
import io.inappchat.inappchat.chat.mapper.MessageMetaDataRecord
import io.inappchat.inappchat.core.type.ChatReactionType
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.user.mapper.UserMapper
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.mqtt.listener.ReceivedMessage
import io.inappchat.inappchat.mqtt.model.ChatReaction
import io.inappchat.inappchat.mqtt.utils.Constants.CHAT_REACTION
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.schedulers.Schedulers

/**
 * Created by DK on 12/06/20.
 */
class MessageWrapper(private val dataManager: DataManager) {

  private val gson: Gson = Gson()
  private var chatUserId: String? = null
  private var deviceId: String? = null
  private fun setChatId(chatUserId: String?) {
    this.chatUserId = chatUserId
  }

  private fun setDeviceId(chatUserId: String?) {
    this.deviceId = chatUserId
  }


  fun messageMetaData(
    receivedMessage: ReceivedMessage,
    topic: String
  ): Observable<MessageMetaDataRecord> {
    return Observable.create { e: ObservableEmitter<MessageMetaDataRecord> ->
      if (chatUserId == null) {
        setChatId(dataManager.preference().chatUserId)
      }
      if (deviceId == null) {
        setDeviceId(dataManager.preference().deviceId)
      }

      when (topic) {
        CHAT_REACTION -> {
          val response =
            gson.fromJson(receivedMessage.message, ChatReaction::class.java)

          if (response != null) {
            val chatReactionDao = dataManager.db().chatReactionDao()
            //Need to query single chat and user DB
            //local msgId, count, emoji, respective user info
            val actionType = if (response.action == ChatReactionType.SET.type) {
              ChatReactionType.SET
            } else {
              ChatReactionType.CLEAR
            }
            if (response.replyThreadFeatureData?.baseMsgUniqueId != null && response.replyThreadFeatureData?.baseMsgUniqueId!!.isNotEmpty()) {
              // need to update thread specific
              val chatThread = dataManager.db().chatThreadDao()
                .getChatByServerMsgId(response.msgUniqueId, response.threadId)

              if (actionType == ChatReactionType.SET) {
                if (response.replyThreadFeatureData?.replyMsgConfig == 1) {
                  chatReactionDao.insertWithReplace(
                    ChatEntityMapper.transform(
                      unicode = response.emojiCode,
                      threadId = response.threadId,
                      chatMsgId = chatThread.id,
                      userChatId = response.eRTCUserId,
                      totalCount = response.totalCount
                    )
                  )
                }
                chatReactionDao.insertWithReplace(
                  ChatEntityMapper.transform(
                    unicode = response.emojiCode,
                    threadId = response.threadId,
                    chatThreadMsgId = chatThread.id,
                    userChatId = response.eRTCUserId,
                    totalCount = response.totalCount
                  )
                )
              } else {
                if (response.replyThreadFeatureData?.replyMsgConfig == 1) {
                  chatReactionDao.clearChatUserReaction(chatThread.id, response.emojiCode, response.eRTCUserId)
                }
                chatReactionDao.clearChatThreadUserReaction(chatThread.id, response.emojiCode, response.eRTCUserId)
              }

              val chatReactionRecord = composeReactionList(
                chatReactionDao.getChatThreadReactionsCount(chatThread.id, response.emojiCode).blockingGet()
              )
              val userRecordList = if (chatReactionRecord.isNullOrEmpty()) {
                null
              } else {
                chatReactionRecord[0].userRecord
              }

              e.onNext(
                MessageMetaDataRecord(
                  ChatReactionRecord(
                    response.threadId,
                    chatThread.id,
                    response.emojiCode,
                    actionType,
                    userRecordList,
                    response.totalCount,
                    chatThread.parentMsgId
                  )
                )
              )
            } else {
              val singleChat = dataManager.db().singleChatDao()
                .getChatByServerMsgId(response.msgUniqueId, response.threadId)

              if (actionType == ChatReactionType.SET) {
                chatReactionDao.insertWithReplace(
                  ChatEntityMapper.transform(
                    unicode = response.emojiCode,
                    threadId = response.threadId,
                    chatMsgId = singleChat.id,
                    userChatId = response.eRTCUserId,
                    totalCount = response.totalCount
                  )
                )
              } else {
                chatReactionDao.clearChatUserReaction(singleChat.id, response.emojiCode, response.eRTCUserId)
              }

              val chatReactionRecord = composeReactionList(
                chatReactionDao.getChatReactionsCount(singleChat.id, response.emojiCode).blockingGet()
              )
              val userRecordList = if (chatReactionRecord.isNullOrEmpty()) {
                null
              } else {
                chatReactionRecord[0].userRecord
              }
              e.onNext(
                MessageMetaDataRecord(
                  ChatReactionRecord(
                    response.threadId,
                    singleChat.id,
                    response.emojiCode,
                    actionType,
                    userRecordList,
                    response.totalCount
                  )
                )
              )
            }
          } else {
            e.onComplete()
          }
        }
      }
      // save data into DataBase
    }.subscribeOn(Schedulers.io())
  }

  private fun composeReactionList(listOfReactionEntities: List<ChatReactionEntity>?): ArrayList<ChatReactionRecord> {
    val chatReactionList = arrayListOf<ChatReactionRecord>()
    val groupBy = listOfReactionEntities?.groupBy { it.unicode }
    if (groupBy != null) {
      for ((k, reactionList) in groupBy) {

        val toList = reactionList.groupBy { it.userChatId }.keys.toList()
        val userEntity = dataManager.db().userDao().getUserEntityByUserChatIds(toList.toTypedArray())
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
}