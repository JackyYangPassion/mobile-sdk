package io.inappchat.inappchat.downloader.request;

import io.inappchat.inappchat.downloader.utils.Priority;
import io.inappchat.inappchat.downloader.handler.DownloadEventHandler;
import io.inappchat.inappchat.downloader.handler.DownloadRepository;

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
