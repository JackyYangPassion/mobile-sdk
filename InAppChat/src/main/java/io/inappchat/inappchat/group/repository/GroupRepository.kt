package io.inappchat.inappchat.group.repository

import androidx.annotation.RestrictTo
import io.inappchat.inappchat.cache.database.entity.Group
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.group.mapper.GroupRecord
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface GroupRepository {

  fun getGroups(tenantId: String): Flowable<List<GroupRecord>>?

  fun saveGroupsInSync(groupList: List<Group>): Boolean

  fun createPrivateGroup(group: GroupRecord): Single<GroupRecord>

  fun updateGroupDetail(
    groupId: String,
    groupName: String,
    groupDesc: String,
    groupImgPath: String
  ): Single<GroupRecord>

  fun removeParticipants(
    groupId: String,
    users: List<String>
  ): Single<GroupRecord>

  fun addParticipants(
    groupId: String,
    users: List<String>
  ): Single<GroupRecord>

  fun getGroupById(groupId: String): Flowable<GroupRecord>?

  fun getGroupByThreadId(threadId: String): Single<GroupRecord>

  fun addAdmin(
    groupId: String,
    user: String
  ): Single<GroupRecord>

  fun removeAdmin(
    groupId: String,
    user: String
  ): Single<GroupRecord>

  fun exitGroup(groupId: String): Single<GroupRecord>

  fun removeGroupPic(groupId: String): Single<Result>

  fun subscribeToGroupUpdate(): Observable<GroupRecord>

  fun createPublicGroup(group: GroupRecord): Single<GroupRecord>

  fun getSearchedChannels(
    keyword: String,
    channelType: String?,
    joined: String?
  ): Single<List<GroupRecord>>

  fun getActiveGroups(tenantId: String): Flowable<List<GroupRecord>>?
}