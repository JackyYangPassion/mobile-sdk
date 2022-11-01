package com.ripbull.sdk.downloader.core;

import android.os.Process;

import java.util.concurrent.ThreadFactory;

public class PriorityThreadFactory implements ThreadFactory {

  private final int mThreadPriority;

  PriorityThreadFactory(int threadPriority) {
    mThreadPriority = threadPriority;
  }

  @Override
  public Thread newThread(final Runnable runnable) {
    Runnable wrapperRunnable = () -> {
      try {
        Process.setThreadPriority(mThreadPriority);
      } catch (Throwable ignored) {

      }
      runnable.run();
    };
    return new Thread(wrapperRunnable);
  }
}