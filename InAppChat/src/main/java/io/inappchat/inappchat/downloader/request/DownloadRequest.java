package io.inappchat.inappchat.downloader.request;

import io.inappchat.inappchat.downloader.Error;
import io.inappchat.inappchat.downloader.OnCancelListener;
import io.inappchat.inappchat.downloader.OnPauseListener;
import io.inappchat.inappchat.downloader.OnProgressListener;
import io.inappchat.inappchat.downloader.OnStartOrResumeListener;
import io.inappchat.inappchat.downloader.utils.Priority;
import io.inappchat.inappchat.downloader.Response;
import io.inappchat.inappchat.downloader.utils.Status;
import io.inappchat.inappchat.downloader.core.Core;
import io.inappchat.inappchat.downloader.handler.DownloadEventHandler;
import io.inappchat.inappchat.downloader.handler.DownloadRepository;
import io.inappchat.inappchat.downloader.internal.ComponentHolder;
import io.inappchat.inappchat.downloader.internal.DownloadRequestQueue;
import io.inappchat.inappchat.downloader.internal.SynchronousCall;
import io.inappchat.inappchat.downloader.utils.DownloadUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

public class DownloadRequest {

  private Priority priority;
  private Object tag;
  private String url;
  private String dirPath;
  private String fileName;
  private String msgId;
  private int sequenceNumber;
  private Future future;
  private long downloadedBytes;
  private long totalBytes;
  private int readTimeout;
  private int connectTimeout;
  private String userAgent;
  private OnProgressListener onProgressListener;
  private OnStartOrResumeListener onStartOrResumeListener;
  private OnPauseListener onPauseListener;
  private OnCancelListener onCancelListener;
  private int downloadId;
  private HashMap<String, List<String>> headerMap;
  private Status status;
  private DownloadRepository downloadRepository;
  private DownloadEventHandler downloadEventHandler;

  DownloadRequest(DownloadRequestBuilder builder) {
    this.url = builder.url;
    this.dirPath = builder.dirPath;
    this.fileName = builder.fileName;
    this.msgId = builder.msgId;
    this.headerMap = builder.headerMap;
    this.priority = builder.priority;
    this.tag = builder.tag;
    this.readTimeout = builder.readTimeout != 0 ? builder.readTimeout : getReadTimeoutFromConfig();
    this.connectTimeout =
        builder.connectTimeout != 0 ? builder.connectTimeout : getConnectTimeoutFromConfig();
    this.userAgent = builder.userAgent;
    this.downloadRepository = builder.downloadRepository;
    this.downloadEventHandler = builder.downloadEventHandler;
  }

  public Priority getPriority() {
    return priority;
  }

  public DownloadRepository getRepository() {
    return downloadRepository;
  }

  public DownloadEventHandler getDownloadEventHandler() {
    return downloadEventHandler;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public Object getTag() {
    return tag;
  }

  public void setTag(Object tag) {
    this.tag = tag;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDirPath() {
    return dirPath;
  }

  public void setDirPath(String dirPath) {
    this.dirPath = dirPath;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public int getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public HashMap<String, List<String>> getHeaders() {
    return headerMap;
  }

  public Future getFuture() {
    return future;
  }

  public void setFuture(Future future) {
    this.future = future;
  }

  public long getDownloadedBytes() {
    return downloadedBytes;
  }

  public void setDownloadedBytes(long downloadedBytes) {
    this.downloadedBytes = downloadedBytes;
  }

  public long getTotalBytes() {
    return totalBytes;
  }

  public void setTotalBytes(long totalBytes) {
    this.totalBytes = totalBytes;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public String getUserAgent() {
    if (userAgent == null) {
      userAgent = ComponentHolder.getInstance().getUserAgent();
    }
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public int getDownloadId() {
    return downloadId;
  }

  public void setDownloadId(int downloadId) {
    this.downloadId = downloadId;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public OnProgressListener getOnProgressListener() {
    return onProgressListener;
  }

  public DownloadRequest setOnStartOrResumeListener(
      OnStartOrResumeListener onStartOrResumeListener) {
    this.onStartOrResumeListener = onStartOrResumeListener;
    return this;
  }

  public DownloadRequest setOnProgressListener(OnProgressListener onProgressListener) {
    this.onProgressListener = onProgressListener;
    return this;
  }

  public DownloadRequest setOnPauseListener(OnPauseListener onPauseListener) {
    this.onPauseListener = onPauseListener;
    return this;
  }

  public DownloadRequest setOnCancelListener(OnCancelListener onCancelListener) {
    this.onCancelListener = onCancelListener;
    return this;
  }

  public int start() {
    downloadId = DownloadUtils.getUniqueId(url, dirPath, fileName);
    DownloadRequestQueue.getInstance().addRequest(this);
    getDownloadEventHandler().download(getMsgId(),getDirPath(), Status.START);
    return downloadId;
  }

  public Response executeSync() {
    downloadId = DownloadUtils.getUniqueId(url, dirPath, fileName);
    return new SynchronousCall(this).execute();
  }

  public void deliverError(final Error error) {
    if (status != Status.CANCELLED) {
      setStatus(Status.FAILED);
      Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(() -> {
        getDownloadEventHandler().download(getMsgId(),getDirPath(),getStatus());
        finish();
      });
    }
  }

  public void deliverSuccess() {
    if (status != Status.CANCELLED) {
      setStatus(Status.COMPLETED);
      Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(() -> {
        String localPath = getDirPath() + "/" + getFileName();
        getDownloadEventHandler().download(getMsgId(), localPath, getStatus());
        finish();
      });
    }
  }

  public void deliverStartEvent() {
    if (status != Status.CANCELLED) {
      Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(() -> {
        if (onStartOrResumeListener != null) {
          onStartOrResumeListener.onStartOrResume();
        }
      });
    }
  }

  public void deliverPauseEvent() {
    if (status != Status.CANCELLED) {
      Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(() -> {
        if (onPauseListener != null) {
          onPauseListener.onPause();
        }
      });
    }
  }

  private void deliverCancelEvent() {
    Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(() -> {
      if (onCancelListener != null) {
        onCancelListener.onCancel();
      }
    });
  }

  public void cancel() {
    status = Status.CANCELLED;
    if (future != null) {
      future.cancel(true);
    }
    deliverCancelEvent();
    deleteTempFileAndDatabaseEntryInBackground(
        DownloadUtils.getTempPath(dirPath, fileName),
        downloadId);
  }

  private void finish() {
    deleteUnwantedModelsAndTempFiles();
    destroy();
    DownloadRequestQueue.getInstance().finish(this);
  }

  private void destroy() {
    this.onProgressListener = null;
    this.onStartOrResumeListener = null;
    this.onPauseListener = null;
    this.onCancelListener = null;
  }

  private int getReadTimeoutFromConfig() {
    return ComponentHolder.getInstance().getReadTimeout();
  }

  private int getConnectTimeoutFromConfig() {
    return ComponentHolder.getInstance().getConnectTimeout();
  }

  private void deleteTempFileAndDatabaseEntryInBackground(final String path,
      final int downloadId) {
    Core.getInstance().getExecutorSupplier().forBackgroundTasks().execute(new Runnable() {
      @Override
      public void run() {
        getRepository().removeById(downloadId);
        File file = new File(path);
        if (file.exists()) {
          //noinspection ResultOfMethodCallIgnored
          file.delete();
        }
      }
    });
  }

  /**
   * Method to clean up temporary resumed files which is older than 30 day
   */
  private void deleteUnwantedModelsAndTempFiles() {
    Core.getInstance().getExecutorSupplier().forBackgroundTasks().execute(new Runnable() {
      @Override
      public void run() {
        //run memory cleaner for 30 days
        final long daysInMillis = 30 * 24 * 60 * 60 * 1000L;
        final long beforeTimeInMillis = System.currentTimeMillis() - daysInMillis;
        if (getRepository() == null) return;
        List<DownloadMediaRecord> models = getRepository().getUnwantedModels(beforeTimeInMillis);
        if (models != null) {
          for (DownloadMediaRecord model : models) {
            final String tempPath = DownloadUtils.getTempPath(model.getDirPath(), model.getFileName());
            getRepository().removeById(model.getId());
            File file = new File(tempPath);
            if (file.exists()) {
              //noinspection ResultOfMethodCallIgnored
              file.delete();
            }
          }
        }
      }
    });
  }
}
