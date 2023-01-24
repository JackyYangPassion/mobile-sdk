package io.inappchat.inappchat.announcement

import android.content.Context
import io.inappchat.inappchat.R
import io.inappchat.inappchat.announcement.mapper.AnnouncementRecord
import io.inappchat.inappchat.core.ChatSDKException
import io.inappchat.inappchat.InAppChat
import io.inappchat.inappchat.utils.Constants
import io.reactivex.rxjava3.core.Observable

class AnnouncementModuleStub private constructor(private val appContext: Context): AnnouncementModule{

  override fun subscribeToAnnouncement(): Observable<AnnouncementRecord> {
    return Observable.error(
      ChatSDKException(
        ChatSDKException.Error.InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.ANNOUNCEMENT
        )
      )
    )
  }

  companion object {
    fun newInstance(): AnnouncementModule {
      return AnnouncementModuleStub(InAppChat.appContext)
    }
  }
}