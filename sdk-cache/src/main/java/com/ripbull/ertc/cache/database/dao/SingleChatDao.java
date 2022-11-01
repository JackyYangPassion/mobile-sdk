package com.ripbull.ertc.cache.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.ripbull.ertc.cache.database.entity.SingleChat;
import com.ripbull.ertc.cache.database.entity.SingleChatEmbedded;

import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

@Dao
public interface SingleChatDao extends BaseDao<SingleChat> {
  String TABLE_NAME = "single_chat";


  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :messageId AND thread_id = :threadId")
  SingleChat getChatByLocalMsgId(String messageId, String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :messageId AND thread_id = :threadId")
  Single<SingleChat> getChatByClientIdByRxSingle(String messageId, String threadId);


  @Query("SELECT * FROM " + TABLE_NAME + " WHERE msg_unique_id = :msgId AND thread_id = :threadId")
  SingleChat getChatByServerMsgId(String msgId, String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE thread_id = :threadId ORDER BY createdAt ASC")
  Single<List<SingleChatEmbedded>> getAll(String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE thread_id = :threadId AND status = :status AND sender_id != :appUserId")
  List<SingleChat> getDeliveredMessages(String threadId, String status, String appUserId);

  @Query("SELECT * FROM "
      + TABLE_NAME
      + " WHERE thread_id = (:threadId) AND  msg_unique_id = :messageId AND status = :status")
  List<SingleChat> hasMessage(String threadId, String messageId, String status);

  @Query("SELECT * FROM "
          + TABLE_NAME
          + " WHERE id = :msgId")
  List<SingleChat> hasMessage(String msgId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE thread_id = :threadId")
  Single<List<SingleChat>> hasThreadMessage(String threadId);


  @Query("SELECT * FROM " + TABLE_NAME + " WHERE status = :status AND msg_type IN (:msgType)")
  List<SingleChat> getSendingMessages(String status, List<String> msgType);


  @Query("DELETE FROM single_chat")
  void deleteAll();


  @Query("DELETE FROM " + TABLE_NAME + " WHERE thread_id = :threadId")
  void deleteByThreadId(String threadId);


  @Query("SELECT * FROM " + TABLE_NAME + " WHERE isStarredChat = :isStarred AND thread_id = :threadId ORDER BY createdAt ASC")
  Observable<List<SingleChat>> getFavoriteMessages(String isStarred, String threadId);


  @Query("SELECT * FROM " + TABLE_NAME + " WHERE isStarredChat = :isStarred ORDER BY createdAt ASC")
  Observable<List<SingleChat>> getAllFavoriteMessages(String isStarred);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :messageId")
  Single<SingleChat> getChatByMsgId(String messageId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE msg_unique_id = :msgId AND thread_id = :threadId")
  SingleChatEmbedded getSingleChatByServerMsgId(String msgId, String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :msgId")
  SingleChatEmbedded getSingleChatByMsgId(String msgId);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE id = :msgId")
  void deleteByMsgId(String msgId);

  @Query("SELECT message FROM " + TABLE_NAME + " WHERE event_type = :chatEventType AND thread_id = :threadId ORDER BY createdAt DESC LIMIT 1")
  String getLastGroupEventMessage(String chatEventType, String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :messageId")
  Single<List<SingleChat>> getChatByMsgIdCount(String messageId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE message LIKE '%' || :searchedText || '%'")
  Single<List<SingleChat>> searchedMessageList(String searchedText);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE thread_id = :threadId ORDER BY createdAt DESC LIMIT 1")
  SingleChat getLastMessage(String threadId);

  @Query("UPDATE " + TABLE_NAME + " SET status = :status WHERE id = :id")
  void setStatus(String status, String id);

  @Query("UPDATE " + TABLE_NAME + " SET is_followed = :followThread WHERE msg_unique_id = :msgUniqueId")
  void setFollowThread(Integer followThread, String msgUniqueId);
}
