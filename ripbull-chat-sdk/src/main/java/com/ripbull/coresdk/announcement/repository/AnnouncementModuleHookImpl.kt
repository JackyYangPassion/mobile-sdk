package com.ripbull.coresdk.announcement.repository

import com.ripbull.coresdk.announcement.AnnouncementModule
import com.ripbull.coresdk.announcement.AnnouncementModuleImpl
import com.ripbull.coresdk.announcement.AnnouncementModuleStub
import com.ripbull.coresdk.announcement.mapper.AnnouncementRecord
import com.ripbull.coresdk.core.event.EventHandler
import com.ripbull.coresdk.data.DataManager
import com.ripbull.coresdk.utils.Constants
import com.ripbull.ertc.cache.database.dao.TenantDao
import io.reactivex.Observable
import io.reactivex.Single

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