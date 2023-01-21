package io.inappchat.inappchat.download

import androidx.annotation.RestrictTo
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.download.mapper.DownloadMediaMapper
import io.inappchat.inappchat.downloader.handler.DownloadRepository
import io.inappchat.inappchat.downloader.request.DownloadMediaRecord
import java.util.*

/** Created by Sagar on 03/03/20.  */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class DownloadRepositoryImpl private constructor(private val dataManager: DataManager) :
  DownloadRepository {

  override fun insertDownloadData(downloadMediaRecord: DownloadMediaRecord?) {
    dataManager.db().downloadMediaDao().insert(DownloadMediaMapper.transform(downloadMediaRecord))
  }

  override fun updateDownloadProgress(
    id: Int,
    downloadedBytes: Long,
    lastModifiedAt: Long
  ) {
    val download = dataManager.db().downloadMediaDao().getDownloadById(id)
    download.downloadedBytes = downloadedBytes
    download.lastModifiedAt = lastModifiedAt
    dataManager.db().downloadMediaDao().update(download)
  }

  override fun getDownloadById(id: Int): DownloadMediaRecord? {
    val downloadMedia = dataManager.db().downloadMediaDao().getDownloadById(id)
    return if (downloadMedia != null) {
      DownloadMediaMapper.transform(downloadMedia)
    } else null
  }

  override fun getUnwantedModels(days: Long): List<DownloadMediaRecord?>? {
    val downloadMediaRecordList: MutableList<DownloadMediaRecord?> =
      ArrayList()
    val downloadMediaList =
      dataManager.db().downloadMediaDao().getUnwantedModels(days)
    if (downloadMediaList != null && downloadMediaList.size > 0) {
      for (downloadMedia in downloadMediaList) {
        downloadMediaRecordList.add(DownloadMediaMapper.transform(downloadMedia))
      }
    }
    return downloadMediaRecordList
  }

  override fun removeById(id: Int) {
    dataManager.db().downloadMediaDao().removeById(id)
  }

  companion object {
    @JvmStatic
    fun newInstance(dataManager: DataManager): DownloadRepository {
      return DownloadRepositoryImpl(dataManager)
    }
  }

}