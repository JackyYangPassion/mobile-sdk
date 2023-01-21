package io.inappchat.inappchat.downloader.internal;



import io.inappchat.inappchat.downloader.utils.Constants;
import io.inappchat.inappchat.downloader.Error;
import io.inappchat.inappchat.downloader.Progress;
import io.inappchat.inappchat.downloader.Response;
import io.inappchat.inappchat.downloader.utils.Status;
import io.inappchat.inappchat.downloader.handler.ProgressHandler;
import io.inappchat.inappchat.downloader.httpclient.HttpClient;
import io.inappchat.inappchat.downloader.internal.stream.FileDownloadOutputStream;
import io.inappchat.inappchat.downloader.internal.stream.FileDownloadRandomAccessFile;
import io.inappchat.inappchat.downloader.request.DownloadMediaRecord;
import io.inappchat.inappchat.downloader.request.DownloadRequest;
import io.inappchat.inappchat.downloader.utils.DownloadUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class DownloadTask {

  private static final int BUFFER_SIZE = 1024 * 4;
  private static final long TIME_GAP_FOR_SYNC = 2000;
  private static final long MIN_BYTES_FOR_SYNC = 65536;
  private final DownloadRequest request;
  private ProgressHandler progressHandler;
  private long lastSyncTime;
  private long lastSyncBytes;
  private InputStream inputStream;
  private FileDownloadOutputStream outputStream;
  private HttpClient httpClient;
  private long totalBytes;
  private int responseCode;
  private String eTag;
  private boolean isResumeSupported;
  private String tempPath;

  private DownloadTask(DownloadRequest request) {
    this.request = request;
  }

  static DownloadTask create(DownloadRequest request) {
    return new DownloadTask(request);
  }

  Response run() {

    Response response = new Response();

    if (request.getStatus() == Status.CANCELLED) {
      response.setCancelled(true);
      return response;
    } else if (request.getStatus() == Status.PAUSED) {
      response.setPaused(true);
      return response;
    }

    try {

      if (request.getOnProgressListener() != null) {
        progressHandler = new ProgressHandler(request.getOnProgressListener());
      }

      tempPath = DownloadUtils.getTempPath(request.getDirPath(), request.getFileName());

      File file = new File(tempPath);

      DownloadMediaRecord dmRecord = getDownloadModelIfAlreadyPresentInDatabase();

      if (dmRecord != null) {
        if (file.exists()) {
          request.setTotalBytes(dmRecord.getTotalBytes());
          request.setDownloadedBytes(dmRecord.getDownloadedBytes());
        } else {
          removeNoMoreNeededModelFromDatabase();
          request.setDownloadedBytes(0);
          request.setTotalBytes(0);
          dmRecord = null;
        }
      }

      httpClient = ComponentHolder.getInstance().getHttpClient();

      httpClient.connect(request);

      if (request.getStatus() == Status.CANCELLED) {
        response.setCancelled(true);
        return response;
      } else if (request.getStatus() == Status.PAUSED) {
        response.setPaused(true);
        return response;
      }

      httpClient = DownloadUtils.getRedirectedConnectionIfAny(httpClient, request);

      responseCode = httpClient.getResponseCode();

      eTag = httpClient.getResponseHeader(Constants.ETAG);

      if (checkIfFreshStartRequiredAndStart(dmRecord)) {
        dmRecord = null;
      }

      if (!isSuccessful()) {
        Error error = new Error();
        error.setServerError(true);
        error.setServerErrorMessage(convertStreamToString(httpClient.getErrorStream()));
        error.setHeaderFields(httpClient.getHeaderFields());
        error.setResponseCode(responseCode);
        response.setError(error);
        return response;
      }

      setResumeSupportedOrNot();

      totalBytes = request.getTotalBytes();

      if (!isResumeSupported) {
        deleteTempFile();
      }

      if (totalBytes == 0) {
        totalBytes = httpClient.getContentLength();
        request.setTotalBytes(totalBytes);
      }

      if (isResumeSupported && dmRecord == null) {
        createAndInsertNewModel();
      }

      if (request.getStatus() == Status.CANCELLED) {
        response.setCancelled(true);
        return response;
      } else if (request.getStatus() == Status.PAUSED) {
        response.setPaused(true);
        return response;
      }

      request.deliverStartEvent();

      inputStream = httpClient.getInputStream();

      byte[] buff = new byte[BUFFER_SIZE];

      if (!file.exists()) {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
          if (file.getParentFile().mkdirs()) {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
          }
        } else {
          //noinspection ResultOfMethodCallIgnored
          file.createNewFile();
        }
      }

      this.outputStream = FileDownloadRandomAccessFile.create(file);

      if (isResumeSupported && request.getDownloadedBytes() != 0) {
        outputStream.seek(request.getDownloadedBytes());
      }

      if (request.getStatus() == Status.CANCELLED) {
        response.setCancelled(true);
        return response;
      } else if (request.getStatus() == Status.PAUSED) {
        response.setPaused(true);
        return response;
      }

      do {

        final int byteCount = inputStream.read(buff, 0, BUFFER_SIZE);

        if (byteCount == -1) {
          break;
        }

        outputStream.write(buff, 0, byteCount);

        request.setDownloadedBytes(request.getDownloadedBytes() + byteCount);

        sendProgress();

        syncIfRequired(outputStream);

        if (request.getStatus() == Status.CANCELLED) {
          response.setCancelled(true);
          return response;
        } else if (request.getStatus() == Status.PAUSED) {
          sync(outputStream);
          response.setPaused(true);
          return response;
        }
      } while (true);

      final String path = DownloadUtils.getPath(request.getDirPath(), request.getFileName());

      DownloadUtils.renameFileName(tempPath, path);

      response.setSuccessful(true);

      if (isResumeSupported) {
        removeNoMoreNeededModelFromDatabase();
      }
    } catch (IOException | IllegalAccessException e) {
      if (!isResumeSupported) {
        deleteTempFile();
      }
      Error error = new Error();
      error.setConnectionError(true);
      error.setConnectionException(e);
      response.setError(error);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeAllSafely(outputStream);
    }

    return response;
  }

  private void deleteTempFile() {
    File file = new File(tempPath);
    if (file.exists()) {
      //noinspection ResultOfMethodCallIgnored
      file.delete();
    }
  }

  private boolean isSuccessful() {
    return responseCode >= HttpURLConnection.HTTP_OK
        && responseCode < HttpURLConnection.HTTP_MULT_CHOICE;
  }

  private void setResumeSupportedOrNot() {
    isResumeSupported = (responseCode == HttpURLConnection.HTTP_PARTIAL);
  }

  private boolean checkIfFreshStartRequiredAndStart(DownloadMediaRecord dmRecord)
      throws IOException, IllegalAccessException {
    if (responseCode == Constants.HTTP_RANGE_NOT_SATISFIABLE || isETagChanged(dmRecord)) {
      if (dmRecord != null) {
        removeNoMoreNeededModelFromDatabase();
      }
      deleteTempFile();
      request.setDownloadedBytes(0);
      request.setTotalBytes(0);
      httpClient = ComponentHolder.getInstance().getHttpClient();
      httpClient.connect(request);
      httpClient = DownloadUtils.getRedirectedConnectionIfAny(httpClient, request);
      responseCode = httpClient.getResponseCode();
      return true;
    }
    return false;
  }

  private boolean isETagChanged(DownloadMediaRecord dmRecord) {
    return !(eTag == null || dmRecord == null || dmRecord.getETag() == null) && !dmRecord.getETag()
        .equals(eTag);
  }

  private DownloadMediaRecord getDownloadModelIfAlreadyPresentInDatabase() {
    return request.getRepository().getDownloadById(request.getDownloadId());
  }

  private void createAndInsertNewModel() {
    DownloadMediaRecord downloadMediaRecord = new DownloadMediaRecord(
        request.getDownloadId(),
        request.getUrl(),
        eTag,
        request.getDirPath(),
        request.getFileName(),
        request.getMsgId(),
        request.getDownloadedBytes(),
        totalBytes,
        System.currentTimeMillis()
    );
    request.getRepository().insertDownloadData(downloadMediaRecord);
  }

  private void removeNoMoreNeededModelFromDatabase() {
    request.getRepository().removeById(request.getDownloadId());
  }

  private void sendProgress() {
    if (request.getStatus() != Status.CANCELLED) {
      if (progressHandler != null) {
        progressHandler.obtainMessage(Constants.UPDATE,
            new Progress(request.getDownloadedBytes(), totalBytes)).sendToTarget();
      }
    }
  }

  private void syncIfRequired(FileDownloadOutputStream outputStream) {
    final long currentBytes = request.getDownloadedBytes();
    final long currentTime = System.currentTimeMillis();
    final long bytesDelta = currentBytes - lastSyncBytes;
    final long timeDelta = currentTime - lastSyncTime;
    if (bytesDelta > MIN_BYTES_FOR_SYNC && timeDelta > TIME_GAP_FOR_SYNC) {
      sync(outputStream);
      lastSyncBytes = currentBytes;
      lastSyncTime = currentTime;
    }
  }

  private void sync(FileDownloadOutputStream outputStream) {
    boolean success;
    try {
      outputStream.flushAndSync();
      success = true;
    } catch (IOException e) {
      success = false;
      e.printStackTrace();
    }
    if (success && isResumeSupported) {
      request.getRepository().updateDownloadProgress(
          request.getDownloadId(),
          request.getDownloadedBytes(),
          System.currentTimeMillis()
      );
    }
  }

  private void closeAllSafely(FileDownloadOutputStream outputStream) {
    if (httpClient != null) {
      try {
        httpClient.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      if (outputStream != null) {
        try {
          sync(outputStream);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private String convertStreamToString(InputStream stream) {
    StringBuilder stringBuilder = new StringBuilder();
    if (stream != null) {
      String line;
      BufferedReader bufferedReader = null;
      try {
        bufferedReader = new BufferedReader(new InputStreamReader(stream));
        while ((line = bufferedReader.readLine()) != null) {
          stringBuilder.append(line);
        }
      } catch (IOException ignored) {

      } finally {
        try {
          if (bufferedReader != null) {
            bufferedReader.close();
          }
        } catch (NullPointerException | IOException ignored) {

        }
      }
    }
    return stringBuilder.toString();
  }
}
