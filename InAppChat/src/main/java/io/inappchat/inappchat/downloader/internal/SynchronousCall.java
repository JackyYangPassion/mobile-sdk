package io.inappchat.inappchat.downloader.internal;

import io.inappchat.inappchat.downloader.Response;
import io.inappchat.inappchat.downloader.request.DownloadRequest;

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
