package io.inappchat.inappchat.notification.repository

import io.inappchat.inappchat.core.type.SettingAppliedFor
import io.inappchat.inappchat.notification.mapper.NotificationSettingRecord
import io.reactivex.rxjava3.core.Single

/** @author Sagar */
interface NotificationRepository {

  fun muteAppNotifications(
    action: String,
    timePeriod: SettingAppliedFor
  ): Single<NotificationSettingRecord>

  fun muteNotifications(
    threadId: String,
    action: String,
    timePeriod: SettingAppliedFor
  ): Single<NotificationSettingRecord>

  fun getSettings(threadId: String): Single<NotificationSettingRecord>
}