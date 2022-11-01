package com.ripbull.coresdk.announcement

import com.ripbull.coresdk.announcement.mapper.AnnouncementRecord
import com.ripbull.coresdk.announcement.repository.AnnouncementRepository
import com.ripbull.coresdk.announcement.repository.AnnouncementRepositoryImpl
import com.ripbull.coresdk.core.event.EventHandler
import com.ripbull.coresdk.data.DataManager
import com.ripbull.coresdk.module.BaseModule
import io.reactivex.Observable

class AnnouncementModuleImpl private constructor(
  dataManager: DataManager,
  private val announcementRepository: AnnouncementRepository,
): BaseModule(dataManager), AnnouncementModule {

  companion object {
    fun newInstance(
      dataManager: DataManager,
      eventHandler: EventHandler
    ): AnnouncementModule {

      val announcementRepository = AnnouncementRepositoryImpl.newInstance(
        dataManager,
        eventHandler
      )
      return AnnouncementModuleImpl(dataManager, announcementRepository)
    }
  }

  override fun subscribeToAnnouncement(): Observable<AnnouncementRecord> {
    return announcementRepository.subscribeToAnnouncement()
  }
}