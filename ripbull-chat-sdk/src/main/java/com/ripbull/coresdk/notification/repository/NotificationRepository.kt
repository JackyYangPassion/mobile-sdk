package com.ripbull.coresdk.notification.repository

import com.ripbull.coresdk.core.type.SettingAppliedFor
import com.ripbull.coresdk.notification.mapper.NotificationSettingRecord
import io.reactivex.Single

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