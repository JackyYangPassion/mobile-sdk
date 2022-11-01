package com.ripbull.coresdk.notification

import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.MuteNotificationType
import com.ripbull.coresdk.core.type.SettingAppliedFor
import com.ripbull.coresdk.notification.mapper.NotificationSettingRecord
import io.reactivex.Single

/** @author Sagar */
interface NotificationModule {

  fun muteNotifications(
    action: String,
    muteNotificationType: MuteNotificationType,
    timePeriod: SettingAppliedFor,
    threadId: String = "",
    chatType: ChatType = ChatType.SINGLE
  ) : Single<NotificationSettingRecord>

  fun getSettings(threadId: String = ""): Single<NotificationSettingRecord>
}