package com.ripbull.ertc.cache.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Query;

import com.ripbull.ertc.cache.database.entity.ChatReactionWithUsers;
import com.ripbull.ertc.cache.database.entity.ChatReactionEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ChatReactionDao extends BaseDao<ChatReactionEntity> {
  String TABLE_NAME = "chat_reaction";

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE chat_msg_id = :chatMsgId")
  Single<List<ChatReactionEntity>> getAllReactions(String chatMsgId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE chat_msg_id = :chatMsgId AND unicode = :unicode")
  Single<List<ChatReactionEntity>> getChatReactionsCount(String chatMsgId, String unicode);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE chat_thread_msg_id = :chatThreadMsgId AND unicode = :unicode")
  Single<List<ChatReactionEntity>> getChatThreadReactionsCount(String chatThreadMsgId, String unicode);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE chat_msg_id = :chatMsgId AND unicode = :unicode AND user_chat_id = :userChatId")
  void clearChatUserReaction(String chatMsgId, String unicode, String userChatId);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE chat_thread_msg_id = :chatThreadMsgId AND unicode = :unicode AND user_chat_id = :userChatId")
  void clearChatThreadUserReaction(String chatThreadMsgId, String unicode, String userChatId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE unicode IN (:unicodes) AND chat_thread_msg_id = :msgId AND thread_id = :threadId")
  Single<List<ChatReactionWithUsers>> getReactionedUsersOnThread(@NonNull String[] unicodes, String msgId, String threadId);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE chat_thread_msg_id = :chatThreadMsgId  AND user_chat_id = :userChatId")
  void clearAllThreadUserReaction(String chatThreadMsgId,  String userChatId);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE chat_msg_id = :chatMsgId  AND user_chat_id = :userChatId")
  void clearAllUserReaction(String chatMsgId,  String userChatId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE unicode IN (:unicodes) AND chat_msg_id = :msgId AND thread_id = :threadId")
  Single<List<ChatReactionWithUsers>> getReactionedUsersOnChat(@NonNull String[] unicodes, String msgId, String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE chat_thread_msg_id = :chatThreadMsgId")
  Single<List<ChatReactionEntity>> getAllThreadReactions(String chatThreadMsgId);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE thread_id = :threadId")
  void deleteByThreadId(String threadId);
}
