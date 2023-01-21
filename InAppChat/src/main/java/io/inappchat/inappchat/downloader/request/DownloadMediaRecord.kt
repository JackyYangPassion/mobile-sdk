package io.inappchat.inappchat.downloader.request


import java.io.Serializable

/** Created by Sagar on 03/03/20.  */

data class DownloadMediaRecord @JvmOverloads constructor(
  val id: Int,
  val url: String? = null,
  val eTag: String? = null,
  val dirPath: String? = null,
  val fileName: String? = null,
  val msgId: String? = null,
  val totalBytes: Long? = null,
  val downloadedBytes: Long? = null,
  val lastModifiedAt: Long? = null
) : Serializable