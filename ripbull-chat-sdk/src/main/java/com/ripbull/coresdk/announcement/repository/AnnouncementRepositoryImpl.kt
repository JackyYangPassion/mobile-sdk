package com.ripbull.coresdk.announcement.repository

import com.ripbull.coresdk.announcement.mapper.AnnouncementRecord
import com.ripbull.coresdk.core.base.BaseRepository
import com.ripbull.coresdk.core.event.EventHandler
import com.ripbull.coresdk.core.type.EventType
import com.ripbull.coresdk.core.type.NetworkEvent
import com.ripbull.coresdk.data.DataManager
import com.ripbull.ertc.cache.preference.PreferenceManager
import com.ripbull.ertc.remote.NetworkConfig
import com.ripbull.ertc.remote.NetworkManager
import io.reactivex.Observable

class AnnouncementRepositoryImpl private constructor(
  private val dataManager: DataManager,
  private val eventHandler: EventHandler,
  private val networkManager: NetworkManager = dataManager.network(),
  private val preference: PreferenceManager = dataManager.preference(),
  private val networkConfig: NetworkConfig = dataManager.networkConfig()
): BaseRepository(dataManager), AnnouncementRepository {

  companion object {
    @JvmStatic
    fun newInstance(
      dataManager: DataManager,
      eventHandler: EventHandler,
    ): AnnouncementRepository {
      return AnnouncementRepositoryImpl(
        dataManager = dataManager,
        eventHandler = eventHandler,
        networkManager = dataManager.network(),
        preference = dataManager.preference(),
        networkConfig = dataManager.networkConfig()
      )
    }
  }

  override fun subscribeToAnnouncement(): Observable<AnnouncementRecord> {
    return eventHandler.source().filter(NetworkEvent.filterType(EventType.ANNOUNCEMENT_RECEIVED))
      .flatMap { networkEvent ->
        Observable.just(networkEvent.AnnouncementRecord())
      }
  }

}