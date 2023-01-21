package io.inappchat.inappchat.notification

import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.MuteNotificationType
import io.inappchat.inappchat.core.type.SettingAppliedFor
import io.inappchat.inappchat.notification.mapper.NotificationSettingRecord
import io.reactivex.rxjava3.core.Single

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