package io.inappchat.inappchat.downloader.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import io.inappchat.inappchat.downloader.utils.Constants;
import io.inappchat.inappchat.downloader.OnProgressListener;
import io.inappchat.inappchat.downloader.Progress;


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
