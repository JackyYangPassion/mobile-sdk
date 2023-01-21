package io.inappchat.inappchat.downloader.internal;

import android.content.Context;
import io.inappchat.inappchat.downloader.utils.Constants;
import io.inappchat.inappchat.downloader.FileDownloaderConfig;
import io.inappchat.inappchat.downloader.httpclient.DefaultHttpClient;
import io.inappchat.inappchat.downloader.httpclient.HttpClient;

public class ComponentHolder {

  private final static ComponentHolder INSTANCE = new ComponentHolder();
  private int readTimeout;
  private int connectTimeout;
  private String userAgent;
  private HttpClient httpClient;

  public static ComponentHolder getInstance() {
    return INSTANCE;
  }

  public void init(Context context, FileDownloaderConfig config) {
    this.readTimeout = config.getReadTimeout();
    this.connectTimeout = config.getConnectTimeout();
    this.userAgent = config.getUserAgent();
    this.httpClient = config.getHttpClient();
  }

  public int getReadTimeout() {
    if (readTimeout == 0) {
      synchronized (ComponentHolder.class) {
        if (readTimeout == 0) {
          readTimeout = Constants.DEFAULT_READ_TIMEOUT_IN_MILLS;
        }
      }
    }
    return readTimeout;
  }

  public int getConnectTimeout() {
    if (connectTimeout == 0) {
      synchronized (ComponentHolder.class) {
        if (connectTimeout == 0) {
          connectTimeout = Constants.DEFAULT_CONNECT_TIMEOUT_IN_MILLS;
        }
      }
    }
    return connectTimeout;
  }

  public String getUserAgent() {
    if (userAgent == null) {
      synchronized (ComponentHolder.class) {
        if (userAgent == null) {
          userAgent = Constants.DEFAULT_USER_AGENT;
        }
      }
    }
    return userAgent;
  }

  public HttpClient getHttpClient() {
    if (httpClient == null) {
      synchronized (ComponentHolder.class) {
        if (httpClient == null) {
          httpClient = new DefaultHttpClient();
        }
      }
    }
    return httpClient.clone();
  }
}
