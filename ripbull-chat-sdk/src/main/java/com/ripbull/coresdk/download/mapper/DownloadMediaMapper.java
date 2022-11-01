package com.ripbull.coresdk.download.mapper;

import com.ripbull.ertc.cache.database.entity.DownloadMedia;
import com.ripbull.sdk.downloader.request.DownloadMediaRecord;

/** Created by Sagar on 03/03/20.  */
public final class DownloadMediaMapper {

  public static DownloadMediaRecord transform(DownloadMedia downloadMedia) {
    return new DownloadMediaRecord(
        downloadMedia.getId(),
        downloadMedia.getUrl(),
        downloadMedia.getETag(),
        downloadMedia.getDirPath(),
        downloadMedia.getFileName(),
        downloadMedia.getMsgId(),
        downloadMedia.getTotalBytes(),
        downloadMedia.getDownloadedBytes(),
        downloadMedia.getLastModifiedAt());
  }

  public static DownloadMedia transform(DownloadMediaRecord downloadMediaRecord) {
    return new DownloadMedia(
        downloadMediaRecord.getId(),
        downloadMediaRecord.getUrl(),
        downloadMediaRecord.getETag(),
        downloadMediaRecord.getDirPath(),
        downloadMediaRecord.getFileName(),
        downloadMediaRecord.getMsgId(),
        downloadMediaRecord.getTotalBytes(),
        downloadMediaRecord.getDownloadedBytes(),
        downloadMediaRecord.getLastModifiedAt());
  }
}
