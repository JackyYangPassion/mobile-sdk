package com.ripbull.sdk.downloader.internal;


import com.ripbull.sdk.downloader.Error;
import com.ripbull.sdk.downloader.utils.Priority;
import com.ripbull.sdk.downloader.Response;
import com.ripbull.sdk.downloader.utils.Status;
import com.ripbull.sdk.downloader.request.DownloadRequest;

public class DownloadRunnable implements Runnable {

  public final Priority priority;
  public final int sequence;
  public final DownloadRequest request;

  DownloadRunnable(DownloadRequest request) {
    this.request = request;
    this.priority = request.getPriority();
    this.sequence = request.getSequenceNumber();
  }

  @Override
  public void run() {
    request.setStatus(Status.RUNNING);
    DownloadTask downloadTask = DownloadTask.create(request);
    Response response = downloadTask.run();
    if (response.isSuccessful()) {
      request.deliverSuccess();
    } else if (response.isPaused()) {
      request.deliverPauseEvent();
    } else if (response.getError() != null) {
      request.deliverError(response.getError());
    } else if (!response.isCancelled()) {
      request.deliverError(new Error());
    }
  }
}
