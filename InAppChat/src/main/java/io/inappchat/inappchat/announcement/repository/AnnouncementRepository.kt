package io.inappchat.inappchat.announcement.repository

import io.inappchat.inappchat.announcement.mapper.AnnouncementRecord
import io.reactivex.rxjava3.core.Observable

interface AnnouncementRepository {

  fun subscribeToAnnouncement(): Observable<AnnouncementRecord>
}