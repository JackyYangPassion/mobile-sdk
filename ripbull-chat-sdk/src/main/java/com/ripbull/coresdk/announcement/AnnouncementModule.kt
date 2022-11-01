package com.ripbull.coresdk.announcement

import com.ripbull.coresdk.announcement.mapper.AnnouncementRecord
import io.reactivex.Observable

interface AnnouncementModule {

  fun subscribeToAnnouncement(): Observable<AnnouncementRecord>
}