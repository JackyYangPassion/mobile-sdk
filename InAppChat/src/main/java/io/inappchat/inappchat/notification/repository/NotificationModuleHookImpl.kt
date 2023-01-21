package io.inappchat.inappchat.notification.repository

import io.inappchat.inappchat.cache.database.dao.TenantDao
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.MuteNotificationType
import io.inappchat.inappchat.core.type.SettingAppliedFor
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.notification.NotificationModule
import io.inappchat.inappchat.notification.NotificationModuleImpl
import io.inappchat.inappchat.notification.NotificationModuleStub
import io.inappchat.inappchat.notification.mapper.NotificationSettingRecord
import io.inappchat.inappchat.utils.Constants
import io.reactivex.rxjava3.core.Single

/** @author Sagar */
class NotificationModuleHookImpl private constructor(
  private val notificationModule: NotificationModule,
  private val stub: NotificationModule,
  private val dataManager: DataManager,
  private val tenantDao: TenantDao = dataManager.db().tenantDao()
) : NotificationModuleHook {

  companion object {
    @JvmStatic
    fun newInstance(
      dataManager: DataManager
    ): NotificationModuleHook {
      val notificationModule = NotificationModuleImpl.newInstance(dataManager)
      val stub = NotificationModuleStub.newInstance()
      return NotificationModuleHookImpl(notificationModule, stub, dataManager)
    }
  }

  override fun provideModule(): NotificationModuleHook {
    return this
  }

  override fun muteNotifications(
    action: String,
    muteNotificationType: MuteNotificationType,
    timePeriod: SettingAppliedFor,
    threadId: String,
    chatType: ChatType
  ): Single<NotificationSettingRecord> {
    if (muteNotificationType == MuteNotificationType.USER_LEVEL) {
      return isFeatureEnabled(Constants.TenantConfig.UserProfile.Notification.MUTE_SETTINGS)
        .flatMap { aBoolean: Boolean ->
          if (aBoolean) {
            notificationModule.muteNotifications(action, muteNotificationType, timePeriod)
          } else {
            stub.muteNotifications(action, muteNotificationType, timePeriod)
          }
        }
    } else {
      when (chatType) {
        ChatType.SINGLE           -> {
          return isFeatureEnabled(Constants.TenantConfig.SingeChat.Notification.MUTE_SETTINGS)
            .flatMap { aBoolean: Boolean ->
              if (aBoolean) {
                notificationModule.muteNotifications(action, muteNotificationType, timePeriod, threadId, chatType)
              } else {
                stub.muteNotifications(action, muteNotificationType, timePeriod, threadId, chatType)
              }
            }
        }
        ChatType.GROUP -> {
          return isFeatureEnabled(Constants.TenantConfig.GroupChat.Notification.MUTE_SETTINGS)
            .flatMap { aBoolean: Boolean ->
              if (aBoolean) {
                notificationModule.muteNotifications(action, muteNotificationType, timePeriod, threadId, chatType)
              } else {
                stub.muteNotifications(action, muteNotificationType, timePeriod, threadId, chatType)
              }
            }
        }
        else                      -> {
          return isFeatureEnabled(Constants.TenantConfig.SingeChat.Notification.MUTE_SETTINGS)
            .flatMap { aBoolean: Boolean ->
              if (aBoolean) {
                notificationModule.muteNotifications(action, muteNotificationType, timePeriod, threadId, chatType)
              } else {
                stub.muteNotifications(action, muteNotificationType, timePeriod, threadId, chatType)
              }
            }
        }
      }
    }

  }

  override fun getSettings(threadId: String): Single<NotificationSettingRecord> {
    return isFeatureEnabled(Constants.TenantConfig.UserProfile.Notification.MUTE_SETTINGS)
      .flatMap { aBoolean: Boolean ->
        if (aBoolean) {
          notificationModule.getSettings(threadId)
        } else {
          stub.getSettings(threadId)
        }
      }
  }

  private fun isFeatureEnabled(feature: String): Single<Boolean> {
    return tenantDao.getTenantConfigValue(dataManager.preference().tenantId, feature)
      .flatMap { s: String ->
        Single.just(
          s == "1"
        )
      }
  }
}