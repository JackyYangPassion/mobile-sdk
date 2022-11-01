package com.ripbull.coresdk.core.type;

public enum DownloadStatus {
  SUCCESS("success"),
  PROGRESS("progress"),
  FAILED("failed");

  private String status;

  DownloadStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
