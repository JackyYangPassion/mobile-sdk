package com.ripbull.sdk.downloader;

import android.content.Context;

import androidx.annotation.Keep;

import com.ripbull.sdk.downloader.core.Core;
import com.ripbull.sdk.downloader.internal.ComponentHolder;
import com.ripbull.sdk.downloader.internal.DownloadRequestQueue;
import com.ripbull.sdk.downloader.request.DownloadRequestBuilder;
import com.ripbull.sdk.downloader.utils.Status;

/**
 * eRTCDownloader entry point.
 * You must initialize this class before use. The simplest way is to just do
 * {#code eRTCDownloader.initialize(context)}.
 */
@Keep
public class FileDownloader {

  /**
   * private constructor to prevent instantiation of this class
   */
  private FileDownloader() {
  }

  /**
   * Initializes eRTCDownloader with the default config.
   *
   * @param context The context
   */
  public static void initialize(Context context) {
    initialize(context, FileDownloaderConfig.newBuilder().build());
  }

  /**
   * Initializes eRTCDownloader with the custom config.
   *
   * @param context The context
   * @param config The eRTCDownloaderConfig
   */
  public static void initialize(Context context, FileDownloaderConfig config) {
    ComponentHolder.getInstance().init(context, config);
    DownloadRequestQueue.initialize();
  }

  /**
   * Method to make download request
   *
   * @param url The url on which request is to be made
   * @param dirPath The directory path on which file is to be saved
   * @param fileName The file name with which file is to be saved
   * @return the DownloadRequestBuilder
   */
  public static DownloadRequestBuilder download(String url, String dirPath, String fileName, String msgId) {
    return new DownloadRequestBuilder(url, dirPath, fileName, msgId);
  }

  /**
   * Method to pause request with the given downloadId
   *
   * @param downloadId The downloadId with which request is to be paused
   */
  public static void pause(int downloadId) {
    DownloadRequestQueue.getInstance().pause(downloadId);
  }

  /**
   * Method to resume request with the given downloadId
   *
   * @param downloadId The downloadId with which request is to be resumed
   */
  public static void resume(int downloadId) {
    DownloadRequestQueue.getInstance().resume(downloadId);
  }

  /**
   * Method to cancel request with the given downloadId
   *
   * @param downloadId The downloadId with which request is to be cancelled
   */
  public static void cancel(int downloadId) {
    DownloadRequestQueue.getInstance().cancel(downloadId);
  }

  /**
   * Method to cancel requests with the given tag
   *
   * @param tag The tag with which requests are to be cancelled
   */
  public static void cancel(Object tag) {
    DownloadRequestQueue.getInstance().cancel(tag);
  }

  /**
   * Method to cancel all requests
   */
  public static void cancelAll() {
    DownloadRequestQueue.getInstance().cancelAll();
  }

  /**
   * Method to check the request with the given downloadId is running or not
   *
   * @param downloadId The downloadId with which request status is to be checked
   * @return the running status
   */
  public static Status getStatus(int downloadId) {
    return DownloadRequestQueue.getInstance().getStatus(downloadId);
  }

  /**
   * Shuts eRTCDownloader down
   */
  public static void shutDown() {
    Core.shutDown();
  }
}
