package com.ripbull.sdk.downloader.internal;

import com.ripbull.sdk.downloader.Response;
import com.ripbull.sdk.downloader.request.DownloadRequest;

public class SynchronousCall {

  public final DownloadRequest request;

  public SynchronousCall(DownloadRequest request) {
    this.request = request;
  }

  public Response execute() {
    DownloadTask downloadTask = DownloadTask.create(request);
    return downloadTask.run();
  }
}
