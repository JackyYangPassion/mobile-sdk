package com.ripbull.sdk.downloader.request;

import com.ripbull.sdk.downloader.utils.Priority;
import com.ripbull.sdk.downloader.handler.DownloadEventHandler;
import com.ripbull.sdk.downloader.handler.DownloadRepository;

public interface RequestBuilder {

  RequestBuilder setHeader(String name, String value);

  RequestBuilder setPriority(Priority priority);

  RequestBuilder setTag(Object tag);

  RequestBuilder setReadTimeout(int readTimeout);

  RequestBuilder setConnectTimeout(int connectTimeout);

  RequestBuilder setUserAgent(String userAgent);

  RequestBuilder callback(DownloadRepository downloadRepository);

  RequestBuilder eventHandler(DownloadEventHandler downloadEventHandler);
}
