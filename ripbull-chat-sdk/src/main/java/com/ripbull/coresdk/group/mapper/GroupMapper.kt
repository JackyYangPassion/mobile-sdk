package com.ripbull.coresdk.group.mapper

import com.ripbull.ertc.cache.database.DataSource
import com.ripbull.ertc.cache.database.entity.Group
import com.ripbull.ertc.cache.database.entity.User
import com.ripbull.ertc.remote.model.response.GroupResponse
import com.ripbull.coresdk.user.mapper.UserMapper
import com.ripbull.coresdk.user.mapper.UserRecord
import java.util.*

object GroupMapper {
  fun transform(
    group: GroupResponse?,
    tenantId: String?,
    db: DataSource,
    userMedia: String,
    myAppUserId: String?,
    myChatUserId: String?
  ): GroupRecord {
    val groupUserRecords: MutableList<UserRecord> = ArrayList()

    var participantsCount = 0
    group?.let {
      participantsCount = it.participantsCount
    }

    var isNotInGroup = true
    if (group?.joined == true) {
      isNotInGroup = false
    }

    if (group?.participants != null && group.participants.isNotEmpty()) {
      participantsCount = group.participants.size
      for (participant in group.participants) {
        if (participant.isDeleted) {
          if (participant.eRTCUserId != myChatUserId) {
            //remove this user from your db
            db.userDao().deleteUserByChatId(participant.eRTCUserId)
            db.ekeyDao().deleteUserDetails(participant.eRTCUserId)
          }
        } else {
          if(myAppUserId.equals(participant.appUserId)) {
            isNotInGroup = false
          }
          val user = db.userDao().getUserById(participant.appUserId)
          val my = user.blockingGet()
          if (my != null) {
            my.ertcId = participant.eRTCUserId
            my.role = participant.role
            groupUserRecords.add(UserMapper.transform(my))
          }
        }
      }
    }
    var groupStatus = GroupRecord.STATUS_ACTIVE
    if (group?.freeze?.enabled == true) {
      groupStatus = GroupRecord.STATUS_FROZEN
    }
    return if (!group!!.profilePic.isNullOrEmpty() && !group.profilePic.contains(userMedia)) {
      GroupRecord(
        group.groupId, tenantId, group.name, "",
        userMedia + group.profilePic, userMedia + group.profilePicThumb,
        group.description, 0L, groupUserRecords,
        group.groupType, group.threadId, isNotInGroup, participantsCount, groupStatus
      )
    } else GroupRecord(
      group.groupId, tenantId, group.name, "", group.profilePic,
      group.profilePicThumb, group.description, 0L, groupUserRecords,
      group.groupType, group.threadId, isNotInGroup, participantsCount, groupStatus
    )
  }

  fun transform(
    group: Group,
    tenantId: String? = null,
    myAppUserId: String?
  ): GroupRecord {
    val groupUserRecords: MutableList<UserRecord> = ArrayList()
    var participantCount = group.participantsCount
    var isNotInGroup = true
    for (groupParticipant in group.groupUsers) {
      if(myAppUserId.equals(groupParticipant.id)) {
        isNotInGroup = false
      }
      groupUserRecords.add(UserMapper.transform(groupParticipant))
    }

    if (groupUserRecords.isNotEmpty()) {
      participantCount = groupUserRecords.size
    }

    return GroupRecord(
      group.groupId, tenantId, group.name, "", group.groupPic,
      group.groupThumb, group.groupDesc, 0L, groupUserRecords, group.groupType,
      group.threadId, isNotInGroup, participantCount, group.groupStatus
    )
  }

  @JvmStatic
  fun transform(groupRecord: GroupRecord?): Group {
    val group =
      Group()
    group.groupDesc = groupRecord!!.groupDesc
    group.name = groupRecord.name
    group.groupDesc = groupRecord.groupDesc
    group.groupId = groupRecord.groupId
    group.groupPic = groupRecord.groupPic
    group.groupThumb = groupRecord.groupThumb
    group.loginTimestamp = groupRecord.loginTimestamp
    group.loginType = groupRecord.loginType
    group.tenantId = groupRecord.tenantId
    group.threadId = groupRecord.threadId
    group.groupType = groupRecord.groupType
    group.participantsCount = groupRecord.participantsCount
    group.groupStatus = groupRecord.groupStatus
    val users =
      ArrayList<User>()
    if (groupRecord.groupUsers != null && groupRecord.groupUsers.isNotEmpty()) {
      for (userRecord in groupRecord.groupUsers) {
        users.add(UserMapper.transform(userRecord))
      }
    }
    group.groupUsers = users
    return group
  }
}