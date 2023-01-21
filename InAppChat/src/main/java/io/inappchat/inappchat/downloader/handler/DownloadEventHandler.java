package io.inappchat.inappchat.downloader.handler;

import io.inappchat.inappchat.downloader.utils.Status;

/**
 * Created by DK on 01/05/20.
 */
public interface DownloadEventHandler {
    void download(String msgId, String localPath, Status status);
}
