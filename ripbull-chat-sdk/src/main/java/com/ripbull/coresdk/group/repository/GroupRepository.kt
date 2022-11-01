package com.ripbull.coresdk.group.repository

import androidx.annotation.RestrictTo
import com.ripbull.ertc.cache.database.entity.Group
import com.ripbull.coresdk.data.common.Result
import com.ripbull.coresdk.group.mapper.GroupRecord
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

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