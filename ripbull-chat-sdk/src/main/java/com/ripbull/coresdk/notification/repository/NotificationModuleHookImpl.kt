package com.ripbull.coresdk.notification.repository

import com.ripbull.ertc.cache.database.dao.TenantDao
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.MuteNotificationType
import com.ripbull.coresdk.core.type.SettingAppliedFor
import com.ripbull.coresdk.data.DataManager
import com.ripbull.coresdk.notification.NotificationModule
import com.ripbull.coresdk.notification.NotificationModuleImpl
import com.ripbull.coresdk.notification.NotificationModuleStub
import com.ripbull.coresdk.notification.mapper.NotificationSettingRecord
import com.ripbull.coresdk.utils.Constants
import io.reactivex.Single

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