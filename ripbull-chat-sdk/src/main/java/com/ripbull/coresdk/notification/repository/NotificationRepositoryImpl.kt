package com.ripbull.coresdk.notification.repository

import com.ripbull.ertc.cache.preference.PreferenceManager
import com.ripbull.ertc.remote.NetworkManager
import com.ripbull.ertc.remote.model.request.NotificationSettings
import com.ripbull.ertc.remote.model.request.NotificationSettingsRequest
import com.ripbull.coresdk.core.base.BaseRepository
import com.ripbull.coresdk.core.type.NotificationSettingsType
import com.ripbull.coresdk.core.type.SettingAppliedFor
import com.ripbull.coresdk.data.DataManager
import com.ripbull.coresdk.notification.mapper.NotificationSettingRecord
import io.reactivex.Single
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

/** @author Sagar */
class NotificationRepositoryImpl private constructor(
  private val dataManager: DataManager,
  private val networkManager: NetworkManager = dataManager.network(),
  private val preference: PreferenceManager = dataManager.preference()
) : BaseRepository(dataManager), NotificationRepository {

  companion object {
    @JvmStatic fun newInstance(
      dataManager: DataManager
    ): NotificationRepository {
      return NotificationRepositoryImpl(dataManager)
    }
  }

  override fun muteAppNotifications(
    action: String,
    timePeriod: SettingAppliedFor
  ): Single<NotificationSettingRecord> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    var validTill = Date()
    var request = NotificationSettingsRequest(NotificationSettings(action))
    if (timePeriod != SettingAppliedFor.ALWAYS) {
      val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
      val calender: Calendar = Calendar.getInstance()
      calender.time = validTill
      validTill = if (timePeriod == SettingAppliedFor.HOURS_24) {
        calender.add(Calendar.HOUR, 24)
        calender.time
      } else if (timePeriod == SettingAppliedFor.HOURS_72) {
        calender.add(Calendar.HOUR, 72)
        calender.time
      } else if (timePeriod == SettingAppliedFor.WEEK_1) {
        calender.add(Calendar.WEEK_OF_MONTH, 1)
        calender.time
      } else if (timePeriod == SettingAppliedFor.WEEKS_2) {
        calender.add(Calendar.WEEK_OF_MONTH, 2)
        calender.time
      } else if (timePeriod == SettingAppliedFor.MONTH_1) {
        calender.add(Calendar.MONTH, 1)
        calender.time
      } else {
        calender.add(Calendar.HOUR, 24)
        calender.time
      }
      val validTillDate = sdf.format(validTill)
      request = NotificationSettingsRequest(NotificationSettings(action, validTillDate, timePeriod.duration))
    }
    return networkManager.api()
      .globalNotificationSettings(tenantId, chatUserId, request)
      .map {
        val userDao = dataManager.db().userDao()
        val user = userDao.getUserByIdInSync(tenantId, appUserId)
        user.notificationSettings = action
        user.validTillValue = timePeriod.duration
        user.validTill = validTill.time
        userDao.update(user)
        NotificationSettingRecord(action, validTill.time, timePeriod.duration)
      }
  }

  override fun muteNotifications(
    threadId: String,
    action: String,
    timePeriod: SettingAppliedFor
  ): Single<NotificationSettingRecord> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    var validTill = Date()
    var request = NotificationSettingsRequest(NotificationSettings(action))
    if (timePeriod != SettingAppliedFor.ALWAYS) {
      val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
      val calender: Calendar = Calendar.getInstance()
      calender.time = validTill
      validTill = if (timePeriod == SettingAppliedFor.HOURS_24) {
        calender.add(Calendar.HOUR, 24)
        calender.time
      } else if (timePeriod == SettingAppliedFor.HOURS_72) {
        calender.add(Calendar.HOUR, 72)
        calender.time
      } else if (timePeriod == SettingAppliedFor.WEEK_1) {
        calender.add(Calendar.WEEK_OF_MONTH, 1)
        calender.time
      } else if (timePeriod == SettingAppliedFor.WEEKS_2) {
        calender.add(Calendar.WEEK_OF_MONTH, 2)
        calender.time
      } else if (timePeriod == SettingAppliedFor.MONTH_1) {
        calender.add(Calendar.MONTH, 1)
        calender.time
      } else {
        calender.add(Calendar.HOUR, 24)
        calender.time
      }
      val validTillDate = sdf.format(validTill)
      request = NotificationSettingsRequest(NotificationSettings(action, validTillDate, timePeriod.duration))
    }

    return networkManager.api()
      .threadNotificationSettings(tenantId, threadId, chatUserId, request)
      .map {
        val threadDao = dataManager.db().threadDao()
        val thread = threadDao.getThreadByIdInSync(threadId)
        thread.notificationSettings = action
        thread.validTillValue = timePeriod.duration
        thread.validTill = validTill.time
        threadDao.update(thread)
        NotificationSettingRecord(action, validTill.time, timePeriod.duration)
      }
  }

  override fun getSettings(threadId: String): Single<NotificationSettingRecord> {
    if (threadId.isNullOrEmpty()) {
      val userDao = dataManager.db().userDao()
      val user = userDao.getUserByIdInSync(tenantId, appUserId)
      if (user.notificationSettings.isNullOrEmpty()) {
        return Single.just(NotificationSettingRecord(NotificationSettingsType.ALL.mute))
      }
      return Single.just(
        NotificationSettingRecord(
          user.notificationSettings,
          user.validTill,
          user.validTillValue
        )
      )
    } else {
      val threadDao = dataManager.db().threadDao()
      val thread = threadDao.getThreadByIdInSync(threadId)
      if (thread.notificationSettings.isNullOrEmpty()) {
        return Single.just(NotificationSettingRecord(NotificationSettingsType.ALL.mute))
      }
      return Single.just(
        NotificationSettingRecord(
          thread.notificationSettings,
          thread.validTill,
          thread.validTillValue
        )
      )
    }

  }

  private fun noInternetConnection(): Boolean {
    return try {
      // Connect to Google DNS to check for connection
      val timeoutMs = 1500
      val socket = Socket()
      val socketAddress = InetSocketAddress("8.8.8.8", 53)

      socket.connect(socketAddress, timeoutMs)
      socket.close()

      false
    } catch (ex: IOException) {
      true
    }
  }
}