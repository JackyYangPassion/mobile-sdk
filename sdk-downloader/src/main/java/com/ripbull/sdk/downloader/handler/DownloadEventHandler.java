package com.ripbull.sdk.downloader.handler;

import com.ripbull.sdk.downloader.utils.Status;

/**
 * Created by DK on 01/05/20.
 */
public interface DownloadEventHandler {
    void download(String msgId, String localPath, Status status);
}
