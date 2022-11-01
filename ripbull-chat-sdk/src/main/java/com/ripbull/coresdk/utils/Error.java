package com.ripbull.coresdk.utils;

import io.reactivex.functions.Consumer;
import java.io.IOException;
import java.net.SocketTimeoutException;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

import static com.ripbull.ertc.remote.Constants.HTTP_CODE_CLIENT_TIMEOUT;
import static com.ripbull.ertc.remote.Constants.HTTP_CODE_NETWORK;
import static com.ripbull.ertc.remote.Constants.HTTP_CODE_UNEXPECTED;

public class Error<T extends Throwable> implements Consumer<T> {

  private ErrorCallback callback;

  public interface ErrorCallback {
    void onError(int httpCode, Status status, String message);
  }

  public Error(ErrorCallback callback) {
    this.callback = callback;
  }

  @Override
  public void accept(Throwable e) {
    if (e instanceof HttpException) {
      HttpException exception = (HttpException) e;
      int code = exception.code();
      ResponseBody responseBody = exception.response().errorBody();
      String errorMessage = getErrorMessage(responseBody);
      if (code == 401) {
        callback.onError(code, Status.UNAUTHENTICATED, errorMessage);
      } else if (code >= 400 && code < 500) {
        callback.onError(code, Status.SERVER_ERROR, errorMessage);
      } else if (code >= 500 && code < 600) {
        callback.onError(code, Status.SERVER_TIMEOUT, errorMessage);
      } else {
        callback.onError(code, Status.UNEXPECTED, errorMessage);
      }
    } else if (e instanceof SocketTimeoutException) {
      callback.onError(HTTP_CODE_CLIENT_TIMEOUT, Status.CLIENT_TIMEOUT, "Socket exception");
    } else if (e instanceof IOException) {
      callback.onError(HTTP_CODE_NETWORK, Status.NETWORK, "Network unavailable");
    } else {
      callback.onError(HTTP_CODE_UNEXPECTED, Status.UNEXPECTED, e.getMessage());
    }
  }

  private String getErrorMessage(ResponseBody responseBody) {
    try {
      return responseBody.string();
    } catch (Exception e) {
      return e.getMessage();
    }
  }
}
