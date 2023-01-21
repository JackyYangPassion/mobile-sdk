package io.inappchat.inappchat.announcement.repository

import io.inappchat.inappchat.announcement.mapper.AnnouncementRecord
import io.inappchat.inappchat.core.base.BaseRepository
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.core.type.EventType
import io.inappchat.inappchat.core.type.NetworkEvent
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.cache.preference.PreferenceManager
import io.inappchat.inappchat.remote.NetworkConfig
import io.inappchat.inappchat.remote.NetworkManager
import io.reactivex.rxjava3.core.Observable

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