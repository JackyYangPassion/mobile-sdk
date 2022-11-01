package com.ripbull.coresdk.notification

import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.MuteNotificationType
import com.ripbull.coresdk.core.type.SettingAppliedFor
import com.ripbull.coresdk.data.DataManager
import com.ripbull.coresdk.module.BaseModule
import com.ripbull.coresdk.notification.mapper.NotificationSettingRecord
import com.ripbull.coresdk.notification.repository.NotificationRepository
import com.ripbull.coresdk.notification.repository.NotificationRepositoryImpl
import io.reactivex.Single

/** @author Sagar */
class NotificationModuleImpl private constructor(
  dataManager: DataManager,
  private val notificationRepository: NotificationRepository
) : BaseModule(dataManager), NotificationModule {

  companion object {
    fun newInstance(
      dataManager: DataManager
    ): NotificationModule {
      val notificationRepository = NotificationRepositoryImpl.newInstance(dataManager)
      return NotificationModuleImpl(dataManager, notificationRepository)
    }
  }

  override fun muteNotifications(
    action: String,
    muteNotificationType: MuteNotificationType,
    timePeriod: SettingAppliedFor,
    threadId: String,
    chatType: ChatType
  ) : Single<NotificationSettingRecord> {
    return if (muteNotificationType == MuteNotificationType.USER_LEVEL) {
      notificationRepository.muteAppNotifications(action, timePeriod)
    } else {
      notificationRepository.muteNotifications(threadId, action, timePeriod)
    }
  }

  override fun getSettings(threadId: String): Single<NotificationSettingRecord> {
    return notificationRepository.getSettings(threadId)
  }

}