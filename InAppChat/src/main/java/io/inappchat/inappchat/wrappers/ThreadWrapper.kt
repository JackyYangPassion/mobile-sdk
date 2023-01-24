package io.inappchat.inappchat.wrappers

import com.google.gson.Gson
import io.inappchat.inappchat.remote.model.response.UserSelfUpdateResponse
import io.inappchat.inappchat.chat.mapper.ChatMetaDataRecord
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.mqtt.listener.ReceivedMessage
import io.inappchat.inappchat.remote.model.response.ClearChatHistoryResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by DK on 12/06/20.
 */
class ThreadWrapper (private val dataManager: DataManager){
  private val gson: Gson = Gson()

  fun threadMetaDataUpdated(eventItem: UserSelfUpdateResponse.EventItem): Observable<ChatMetaDataRecord> {
    return Observable.create { e: ObservableEmitter<ChatMetaDataRecord> ->

      val chatMetaDataRecord = ChatMetaDataRecord(
        eventItem.eventData.threadId,
        eventItem.eventData.notificationSettings?.allowFrom
      )

      // save Notification Settings into db
      dataManager.db().threadDao().updateNotificationSettings(
        eventItem.eventData.threadId,
        eventItem.eventData.notificationSettings?.allowFrom
      )

      e.onNext(chatMetaDataRecord)
    }.subscribeOn(Schedulers.io())
  }

  fun chatCleared(receivedMessage: ReceivedMessage): Observable<ChatMetaDataRecord> {
    return Observable.create { e: ObservableEmitter<ChatMetaDataRecord> ->
      val chatMetaDataRecord = gson.fromJson(receivedMessage.message, ClearChatHistoryResponse::class.java)
      //Delete local data of thread
      //dataManager.db().singleChatDao().deleteByThreadId(chatMetaDataRecord.threadId)
      //dataManager.db().chatThreadDao().deleteByThreadId(chatMetaDataRecord.threadId)
      //dataManager.db().chatReactionDao().deleteByThreadId(chatMetaDataRecord.threadId)
      e.onNext(
        ChatMetaDataRecord(
          threadId = chatMetaDataRecord.threadId,
          chatCleared = true
        )
      )
    }.subscribeOn(Schedulers.io())
  }
}