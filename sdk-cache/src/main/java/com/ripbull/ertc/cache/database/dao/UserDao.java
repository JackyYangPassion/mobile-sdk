package com.ripbull.ertc.cache.database.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Query;

import com.ripbull.ertc.cache.database.entity.ThreadUserLink;
import com.ripbull.ertc.cache.database.entity.User;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface UserDao extends BaseDao<User> {
  String TABLE_NAME = "user";

  @Query("SELECT * FROM user ORDER BY name")
  Flowable<User> getUser();

  @Query("SELECT * FROM user WHERE tenant_id = (:tenantId) ORDER BY id DESC LIMIT 1")
  Single<User> getLastUser(@NonNull String tenantId);

  @Nullable
  @Query("SELECT * FROM user WHERE tenant_id = (:tenantId) ORDER BY id DESC LIMIT 1")
  User getLastUserInSync(@NonNull String tenantId);

  @Query("SELECT * FROM user WHERE tenant_id = (:tenantId) AND id = (:userId)")
  Single<User> getUserById(@NonNull String tenantId, String userId);

  @Query("SELECT * FROM user WHERE tenant_id = (:tenantId) AND id = (:userId)")
  Single<List<User>> hasUser(@NonNull String tenantId, String userId);

  @Query("SELECT * FROM user WHERE tenant_id = (:tenantId) AND id = (:userId)")
  Flowable<User> getUserByIdFlowable(@NonNull String tenantId, String userId);

  @Query("SELECT * FROM user WHERE tenant_id = (:tenantId) AND id = (:userId)")
  User getUserByIdInSync(@NonNull String tenantId, String userId);

  @Query(
      "SELECT * FROM user WHERE tenant_id = (:tenantId) AND id != (:userId) AND app_state = 'active' ORDER BY name COLLATE NOCASE")
  Flowable<List<User>> getUsers(@NonNull String tenantId, String userId);

  @Query(
          "SELECT * FROM user WHERE tenant_id = (:tenantId) AND id != (:userId) AND app_state = 'active' ORDER BY name COLLATE NOCASE")
  Single<List<User>> getUsersBySingle(@NonNull String tenantId, String userId);

  @Query("SELECT user_chat_id FROM user WHERE id IN (:ids)")
  List<String> getUserChatIdByUserAppIds(@NonNull String[] ids);

  @Query("SELECT * FROM user WHERE user_chat_id IN (:ids)")
  List<User> getUserEntityByUserChatIds(@NonNull String[] ids);

  @Query("SELECT * FROM user WHERE id IN (:ids)")
  Single<List<User>> getUserEntitiesByUserAppIds(@NonNull String[] ids);


  // @Query("SELECT * FROM user WHERE tenant_id = (:tenantId) ORDER BY name COLLATE NOCASE")
  // DataSource.Factory<Integer, User> getUsersFactory(@NonNull String tenantId);

  @Query("SELECT * FROM user WHERE tenant_id = (:tenantId) AND name LIKE :query")
  Flowable<List<User>> getUsersWithQuery(@NonNull String tenantId, String query);

  // @Query("SELECT * FROM user WHERE tenant_id = (:tenantId) AND name LIKE :query")
  // DataSource.Factory<Integer, User> getUsersWithQueryFactory(@NonNull String tenantId, String
  // query);

  @Query("SELECT availability_status FROM user WHERE tenant_id = (:tenantId) AND id = (:userId)")
  String getUserStatus(@NonNull String tenantId, String userId);

  @Query("DELETE FROM user")
  void deleteAll();

  @Query("DELETE FROM user WHERE id = (:userId)")
  void deleteUser(@NonNull String userId);

  @Query("SELECT blocked_status FROM user WHERE tenant_id = (:tenantId) AND id = (:userId)")
  String getUserBlockStatus(@NonNull String tenantId, String userId);

  @Query("SELECT * FROM user WHERE tenant_id = (:tenantId) AND user_chat_id = (:chatUserId)")
  User getUserByChatUserId(@NonNull String tenantId, String chatUserId);

  @Query("SELECT * FROM user WHERE user_chat_id = (:chatUserId)")
  User getUserByChatUserId(@NonNull String chatUserId);

  @Query("UPDATE " + TABLE_NAME + " SET notification_settings = :notificationSettings WHERE user_chat_id = :chatUserId")
  void updateNotificationSettings(String chatUserId, String notificationSettings);

  @Query("UPDATE " + TABLE_NAME + " SET user_chat_id = :userChatId WHERE id = :userAppId")
  void updateUserChatId(String userAppId, String userChatId);

  @Query("SELECT name FROM user WHERE id = (:userId)")
  String getName(String userId);

  @Query("SELECT id FROM user WHERE user_chat_id = (:userChatId)")
  String getAppUserId(String userChatId);

  @Query("UPDATE " + TABLE_NAME + " SET availability_status = :availabilityStatus WHERE user_chat_id = :chatUserId")
  void updateAvailabilityStatus(String chatUserId, String availabilityStatus);

  @Query("SELECT id FROM user WHERE id != (:userId) AND app_state = 'active'")
  Single<List<String>> getUserIds(String userId);

  @Query("UPDATE " + TABLE_NAME + " SET availability_status = :availabilityStatus WHERE id = :userId")
  void updateAvailabilityStatusById(String userId, String availabilityStatus);

  @Query("DELETE FROM user WHERE user_chat_id = (:userChatId)")
  void deleteUserByChatId(@NonNull String userChatId);

  @Query("SELECT * FROM user WHERE id = (:userId)")
  Maybe<User> getUserById(String userId);
}
