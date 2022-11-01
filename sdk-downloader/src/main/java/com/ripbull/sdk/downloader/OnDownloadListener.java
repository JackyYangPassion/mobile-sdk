package com.ripbull.sdk.downloader;

public interface OnDownloadListener {

  void onDownloadComplete();

  void onError(java.lang.Error error);
}
