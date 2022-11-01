package com.ripbull.ertc.cache.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.ripbull.ertc.cache.database.entity.ThreadChatEmbedded;
import com.ripbull.ertc.cache.database.entity.ChatThread;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface ChatThreadDao extends BaseDao<ChatThread> {
  String TABLE_NAME = "chat_thread";

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :messageId AND thread_id = :threadId")
  ChatThread getChatByClientId(String messageId, String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :messageId AND thread_id = :threadId")
  Single<ChatThread> getChatByClientIdBySingle(String messageId, String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :messageId")
  ChatThread getChatByClientId(String messageId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE parent_msg_id = :parentMsgId ORDER BY createdAt ASC")
  Single<List<ThreadChatEmbedded>> getAll(String parentMsgId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE parent_msg_id = :parentMsgId ORDER BY createdAt ASC")
  Single<List<ChatThread>> getChatThreads(String parentMsgId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE msg_unique_id = :msgUniqueId AND thread_id = :threadId")
  ChatThread getChatByServerMsgId(String msgUniqueId, String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE parent_msg_id = :parentMsgId AND status = :status AND sender_id != :appUserId")
  List<ChatThread> getDeliveredMessages(String parentMsgId, String status, String appUserId);

  @Query("SELECT * FROM "
      + TABLE_NAME
      + " WHERE msg_unique_id = :messageId AND status = :status")
  List<ChatThread> hasMessage(String messageId, String status);

  @Query("SELECT * FROM "
          + TABLE_NAME
          + " WHERE id = :msgId")
  List<ChatThread> hasMessage(String msgId);

  @Query("DELETE FROM "+TABLE_NAME)
  void deleteAll();

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE isStarredChat = :isStarred ORDER BY createdAt ASC")
  Observable<List<ChatThread>> getAllFavoriteMessages(String isStarred);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE id = :msgId")
  void deleteByMsgId(String msgId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :messageId AND thread_id = :threadId")
  Single<List<ChatThread>> getChatByClientIdBySingleCount(String messageId, String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :messageId")
  Single<List<ChatThread>> getChatByMsgIdCount(String messageId);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE thread_id = :threadId")
  void deleteByThreadId(String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE isStarredChat = :isStarred AND thread_id = :threadId AND parent_msg_id = :parentMsgId ORDER BY createdAt ASC")
  Flowable<List<ChatThread>> getFavoriteMessages(String isStarred, String threadId, String parentMsgId);

  @Query("UPDATE " + TABLE_NAME + " SET status = :status WHERE id = :id")
  void setStatus(String status, String id);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE message LIKE '%' || :searchedText || '%'")
  Single<List<ChatThread>> searchedMessageList(String searchedText);
}
