package io.inappchat.inappchat.cache.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Query;

import io.inappchat.inappchat.cache.database.entity.Group;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface GroupDao extends BaseDao<Group> {

  @Query("SELECT * FROM `group` WHERE tenant_id = (:tenantId) ORDER BY name")
  Flowable<List<Group>> getAllGroup(@NonNull String tenantId);

  @Query("SELECT * FROM `group` WHERE tenant_id = (:tenantId) AND group_id = (:groupId)")
  Flowable<Group> getGroupById(@NonNull String tenantId, String groupId);

  @Query("SELECT * FROM `group` WHERE tenant_id = (:tenantId) AND thread_id = (:threadId)")
  Group getGroupByThreadId(@NonNull String tenantId, String threadId);

  @Query("SELECT * FROM `group` WHERE tenant_id = (:tenantId) AND group_id = (:groupId)")
  Group getGroupByIdInSync(String tenantId, String groupId);

  @Query("DELETE FROM `group`")
  void deleteAll();

  @Query("DELETE FROM `group` WHERE group_id = (:groupId)")
  void deleteByGroupId(String groupId);

  @Query("SELECT * FROM `group` WHERE group_id = (:groupId)")
  Group getGroupById(String groupId);

  @Query("UPDATE `group` SET group_status = :groupStatus WHERE group_id = :groupId")
  void updateGroupStatus(String groupId, String groupStatus);

  @Query("SELECT * FROM `group` WHERE group_status = :status ORDER BY name")
  Flowable<List<Group>> getActiveGroup(String status);

  @Query("SELECT name FROM `group` WHERE thread_id = (:threadId)")
  String getGroupName(String threadId);
}
