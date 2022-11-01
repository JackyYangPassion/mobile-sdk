package com.ripbull.ertc.cache.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Created by Sagar on 03/03/20. */
@Entity(tableName = "download")
// Single chat -> Download media in one-one relation-ship
data class DownloadMedia @JvmOverloads constructor(
  @PrimaryKey @ColumnInfo(name = "id") var id: Int,
  @ColumnInfo(name = "url") var url: String? = null,
  @ColumnInfo(name = "etag") var eTag: String? = null,
  @ColumnInfo(name = "dir_path") var dirPath: String? = null,
  @ColumnInfo(name = "file_name") var fileName: String? = null,
  @ColumnInfo(name = "msg_id") var msgId: String? = null,
  @ColumnInfo(name = "total_bytes") var totalBytes: Long? = null,
  @ColumnInfo(name = "downloaded_bytes") var downloadedBytes: Long? = null,
  @ColumnInfo(name = "last_modified_at") var lastModifiedAt: Long? = null
)