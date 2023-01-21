package io.inappchat.inappchat.cache.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Query;

import io.inappchat.inappchat.cache.database.entity.ThreadUserLink;

import io.reactivex.rxjava3.core.Single;
import java.util.List;

/** @author meeth */
@Dao
public interface ThreadUserLinkDao extends BaseDao<ThreadUserLink> {
  String TABLE_NAME = "thread_user_link";

  @Query(
      "SELECT * FROM "
          + TABLE_NAME
          + " WHERE sender_id = (:senderId) AND recipient_id = (:recipientId)")
  Single<List<ThreadUserLink>> hasThread(@NonNull String senderId, @NonNull String recipientId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE thread_id = (:threadId)")
  List<ThreadUserLink> hasThread(@NonNull String threadId);

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE thread_id = (:threadId)")
  ThreadUserLink threadUserInfoSync(@NonNull String threadId);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE recipient_id = :recipientUserId")
  void deleteThreadByUserId(String recipientUserId);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE thread_id = :threadId")
  void deleteThreadById(String threadId);
}
