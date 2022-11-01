package com.ripbull.coresdk.announcement.repository

import com.ripbull.coresdk.announcement.mapper.AnnouncementRecord
import io.reactivex.Observable

interface AnnouncementRepository {

  fun subscribeToAnnouncement(): Observable<AnnouncementRecord>
}