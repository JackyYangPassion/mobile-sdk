package io.inappchat.inappchat.notification

import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.MuteNotificationType
import io.inappchat.inappchat.core.type.SettingAppliedFor
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.module.BaseModule
import io.inappchat.inappchat.notification.mapper.NotificationSettingRecord
import io.inappchat.inappchat.notification.repository.NotificationRepository
import io.inappchat.inappchat.notification.repository.NotificationRepositoryImpl
import io.reactivex.rxjava3.core.Single

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