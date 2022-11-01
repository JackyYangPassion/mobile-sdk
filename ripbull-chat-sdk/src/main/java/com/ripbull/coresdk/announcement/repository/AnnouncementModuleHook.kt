package com.ripbull.coresdk.announcement.repository

import com.ripbull.coresdk.announcement.mapper.AnnouncementRecord
import io.reactivex.Observable

interface AnnouncementModuleHook {

  fun subscribeToAnnouncement(): Observable<AnnouncementRecord>
}