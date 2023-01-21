package io.inappchat.inappchat.notification.repository

import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.MuteNotificationType
import io.inappchat.inappchat.core.type.SettingAppliedFor
import io.inappchat.inappchat.core.type.SettingAppliedFor.ALWAYS
import io.inappchat.inappchat.notification.mapper.NotificationSettingRecord
import io.reactivex.rxjava3.core.Single

/** @author Sagar */
interface NotificationModuleHook {

  fun provideModule(): NotificationModuleHook?

  fun muteNotifications(
    action: String,
    muteNotificationType: MuteNotificationType,
    timePeriod: SettingAppliedFor = ALWAYS,
    threadId: String = "",
    chatType: ChatType = ChatType.SINGLE
  ) : Single<NotificationSettingRecord>

  fun getSettings(threadId: String = ""): Single<NotificationSettingRecord>
}