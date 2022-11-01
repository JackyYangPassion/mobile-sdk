package com.ripbull.sdk.downloader.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ripbull.sdk.downloader.utils.Constants;
import com.ripbull.sdk.downloader.OnProgressListener;
import com.ripbull.sdk.downloader.Progress;


public class ProgressHandler extends Handler {

  private final OnProgressListener listener;

  public ProgressHandler(OnProgressListener listener) {
    super(Looper.getMainLooper());
    this.listener = listener;
  }

  @Override
  public void handleMessage(Message msg) {
    if (msg.what == Constants.UPDATE) {
      if (listener != null) {
        final Progress progress = (Progress) msg.obj;
        listener.onProgress(progress);
      }
    } else {
      super.handleMessage(msg);
    }
  }
}
