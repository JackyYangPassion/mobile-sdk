package com.ripbull.sdk.downloader.handler

import androidx.annotation.RestrictTo
import com.ripbull.sdk.downloader.request.DownloadMediaRecord

/** Created by Sagar on 03/03/20.  */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface DownloadRepository {
  fun insertDownloadData(downloadMediaRecord: DownloadMediaRecord?)
  fun updateDownloadProgress(
    id: Int,
    downloadedBytes: Long,
    lastModifiedAt: Long
  )

  fun getDownloadById(id: Int): DownloadMediaRecord?
  fun getUnwantedModels(days: Long): List<DownloadMediaRecord?>?
  fun removeById(id: Int)
}