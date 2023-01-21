package io.inappchat.inappchat.downloader.core;

import java.util.concurrent.Executor;

public interface ExecutorSupplier {

  DownloadExecutor forDownloadTasks();

  Executor forBackgroundTasks();

  Executor forMainThreadTasks();
}
