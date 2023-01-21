package io.inappchat.inappchat.announcement.repository

import io.inappchat.inappchat.announcement.AnnouncementModule
import io.inappchat.inappchat.announcement.AnnouncementModuleImpl
import io.inappchat.inappchat.announcement.AnnouncementModuleStub
import io.inappchat.inappchat.announcement.mapper.AnnouncementRecord
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.utils.Constants
import io.inappchat.inappchat.cache.database.dao.TenantDao
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class AnnouncementModuleHookImpl private constructor(
  private val announcementModule: AnnouncementModule,
  private val stub: AnnouncementModule,
  private val dataManager: DataManager,
  private val tenantDao: TenantDao = dataManager.db().tenantDao()
): AnnouncementModuleHook {

  private fun isFeatureEnabled(feature: String): Single<Boolean> {
    return tenantDao.getTenantConfigValue(dataManager.preference().tenantId, feature)
      .flatMap { s: String ->
        Single.just(
          s == "1"
        )
      }
  }

  override fun subscribeToAnnouncement(): Observable<AnnouncementRecord> {
    return isFeatureEnabled(Constants.TenantConfig.FOLLOW_CHAT).flatMapObservable { aBoolean ->
      if (aBoolean) {
        announcementModule.subscribeToAnnouncement()
      } else {
        stub.subscribeToAnnouncement()
      }
    }
  }

  companion object {
    @JvmStatic
    fun newInstance(
      dataManager: DataManager,
      eventHandler: EventHandler
    ): AnnouncementModuleHook {
      val announcementModule = AnnouncementModuleImpl.newInstance(dataManager, eventHandler)
      val stub = AnnouncementModuleStub.newInstance()
      return AnnouncementModuleHookImpl(announcementModule, stub, dataManager)
    }
  }
}