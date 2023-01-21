package io.inappchat.inappchat.downloader.request;

import io.inappchat.inappchat.downloader.utils.Priority;
import io.inappchat.inappchat.downloader.handler.DownloadEventHandler;
import io.inappchat.inappchat.downloader.handler.DownloadRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadRequestBuilder implements RequestBuilder {

  String url;
  String dirPath;
  String fileName;
  String msgId;
  Priority priority = Priority.MEDIUM;
  DownloadRepository downloadRepository;
  DownloadEventHandler downloadEventHandler;
  Object tag;
  int readTimeout;
  int connectTimeout;
  String userAgent;
  HashMap<String, List<String>> headerMap;

  public DownloadRequestBuilder(String url, String dirPath, String fileName, String msgId) {
    this.url = url;
    this.dirPath = dirPath;
    this.fileName = fileName;
    this.msgId = msgId;
  }

  @Override
  public DownloadRequestBuilder setHeader(String name, String value) {
    if (headerMap == null) {
      headerMap = new HashMap<>();
    }
    List<String> list = headerMap.get(name);
    if (list == null) {
      list = new ArrayList<>();
      headerMap.put(name, list);
    }
    if (!list.contains(value)) {
      list.add(value);
    }
    return this;
  }

  @Override
  public DownloadRequestBuilder setPriority(Priority priority) {
    this.priority = priority;
    return this;
  }

  @Override
  public DownloadRequestBuilder callback(DownloadRepository downloadRepository) {
    this.downloadRepository = downloadRepository;
    return this;
  }

  @Override
  public DownloadRequestBuilder eventHandler(DownloadEventHandler downloadEventHandler) {
    this.downloadEventHandler = downloadEventHandler;
    return this;
  }

  @Override
  public DownloadRequestBuilder setTag(Object tag) {
    this.tag = tag;
    return this;
  }

  @Override
  public DownloadRequestBuilder setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  @Override
  public DownloadRequestBuilder setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  @Override
  public DownloadRequestBuilder setUserAgent(String userAgent) {
    this.userAgent = userAgent;
    return this;
  }

  public DownloadRequest build() {
    return new DownloadRequest(this);
  }
}
