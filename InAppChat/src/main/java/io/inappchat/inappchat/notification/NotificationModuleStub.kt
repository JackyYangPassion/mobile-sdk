package io.inappchat.inappchat.notification

import android.content.Context
import io.inappchat.inappchat.R
import io.inappchat.inappchat.core.ChatSDKException
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.MuteNotificationType
import io.inappchat.inappchat.core.type.SettingAppliedFor
import io.inappchat.inappchat.eRTCSDK
import io.inappchat.inappchat.notification.mapper.NotificationSettingRecord
import io.inappchat.inappchat.utils.Constants
import io.reactivex.rxjava3.core.Single

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