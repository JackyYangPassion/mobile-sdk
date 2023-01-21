package io.inappchat.inappchat.announcement

import io.inappchat.inappchat.announcement.mapper.AnnouncementRecord
import io.inappchat.inappchat.announcement.repository.AnnouncementRepository
import io.inappchat.inappchat.announcement.repository.AnnouncementRepositoryImpl
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.module.BaseModule
import io.reactivex.rxjava3.core.Observable

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