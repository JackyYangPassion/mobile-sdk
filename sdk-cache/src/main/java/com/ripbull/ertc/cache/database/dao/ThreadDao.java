package com.ripbull.ertc.cache.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ripbull.ertc.cache.database.entity.ThreadEmbedded;
import com.ripbull.ertc.cache.database.entity.Thread;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

/** @author meeth */
@Dao
public interface ThreadDao extends BaseDao<Thread> {
  String TABLE_NAME = "thread";

  @Transaction
  @Query("SELECT * FROM " + TABLE_NAME + " WHERE tenant_id = (:tenantId)")
  Flowable<List<ThreadEmbedded>> getThreads(@NonNull String tenantId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :threadId")
  Thread getThreadByIdInSync(String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :threadId")
  Single<Thread> getThreadByIdInAsync(String threadId);

  @Query("DELETE FROM thread")
  void deleteAll();

  @Query("DELETE FROM " + TABLE_NAME + " WHERE id = :threadId")
  void deleteThreadById(String threadId);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE recipient_user_id = :recipientUserId")
  void deleteThreadByUserId(String recipientUserId);

  @Query("SELECT id FROM " + TABLE_NAME + " WHERE recipient_user_id = :recipientUserId")
  String getThreadIdByUserId(String recipientUserId);

  @Query("SELECT recipient_user_id FROM " + TABLE_NAME + " WHERE id = :threadId")
  String getUserIdByThreadId(String threadId);

  @Query("UPDATE " + TABLE_NAME + " SET updated_at = :updatedAt WHERE recipient_user_id = :recipientUserId")
  void updateUserUpdatedAt(String recipientUserId, long updatedAt);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :threadId")
  Flowable<Thread> getThreadByIdInFlowable(String threadId);

  @Query("UPDATE " + TABLE_NAME + " SET notification_settings = :notificationSettings WHERE id = :threadId")
  void updateNotificationSettings(String threadId, String notificationSettings);

  @Query("UPDATE " + TABLE_NAME + " SET unread_count = :unreadCount WHERE id = :threadId")
  void updateUnreadCount(String threadId, int unreadCount);

  @Query("SELECT id FROM " + TABLE_NAME + " WHERE recipient_user_id = :recipientUserId")
  List<String> getThreadIdByRecipientUserId(String recipientUserId);

  @Query("SELECT id FROM " + TABLE_NAME + " WHERE recipient_user_id = :recipientUserId")
  Maybe<String> getThreadIdByRecipientId(String recipientUserId);

  @Transaction
  @Query("SELECT * FROM " + TABLE_NAME + " WHERE tenant_id = :tenantId AND id = :threadId")
  Single<ThreadEmbedded> getThread(@NonNull String tenantId, @NonNull String threadId);
}
