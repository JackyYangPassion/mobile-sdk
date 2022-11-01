package com.ripbull.coresdk.announcement

import android.content.Context
import com.ripbull.coresdk.R
import com.ripbull.coresdk.announcement.mapper.AnnouncementRecord
import com.ripbull.coresdk.core.ChatSDKException
import com.ripbull.coresdk.eRTCSDK
import com.ripbull.coresdk.utils.Constants
import io.reactivex.Observable

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
      return AnnouncementModuleStub(eRTCSDK.getAppContext())
    }
  }
}