package com.ripbull.coresdk.notification

import android.content.Context
import com.ripbull.coresdk.R
import com.ripbull.coresdk.core.ChatSDKException
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.MuteNotificationType
import com.ripbull.coresdk.core.type.SettingAppliedFor
import com.ripbull.coresdk.eRTCSDK
import com.ripbull.coresdk.notification.mapper.NotificationSettingRecord
import com.ripbull.coresdk.utils.Constants
import io.reactivex.Single

class NotificationModuleStub private constructor(private val appContext: Context) : NotificationModule {

  companion object {
    fun newInstance(): NotificationModule {
      return NotificationModuleStub(eRTCSDK.getAppContext())
    }
  }

  override fun muteNotifications(
    action: String,
    muteNotificationType: MuteNotificationType,
    timePeriod: SettingAppliedFor,
    threadId: String,
    chatType: ChatType
  ) : Single<NotificationSettingRecord> {
    if (muteNotificationType == MuteNotificationType.USER_LEVEL) {
      return Single.error(
        ChatSDKException(
          ChatSDKException.Error.InvalidModule(),
          appContext.getString(
            R.string.alert_message,
            Constants.Features.MUTE_NOTIFICATIONS
          )
        )
      )
    } else {
      return Single.error(
        ChatSDKException(
          ChatSDKException.Error.InvalidModule(),
          appContext.getString(
            R.string.alert_message,
            Constants.Features.CHAT_MUTE_NOTIFICATIONS
          )
        )
      )
    }
  }

  override fun getSettings(threadId: String): Single<NotificationSettingRecord> {
    return Single.error(
      ChatSDKException(
        ChatSDKException.Error.InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.MUTE_NOTIFICATIONS
        )
      )
    )
  }
}