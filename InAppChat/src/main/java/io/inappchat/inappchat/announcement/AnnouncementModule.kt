package io.inappchat.inappchat.announcement

import io.inappchat.inappchat.announcement.mapper.AnnouncementRecord
import io.reactivex.rxjava3.core.Observable

interface AnnouncementModule {

  fun subscribeToAnnouncement(): Observable<AnnouncementRecord>
}