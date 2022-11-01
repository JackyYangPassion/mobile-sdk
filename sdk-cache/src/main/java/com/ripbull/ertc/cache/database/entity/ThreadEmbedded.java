package com.ripbull.ertc.cache.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/** Created by DK on 10/03/19. */
public class ThreadEmbedded {

  @Embedded private Thread thread;

  @Relation(parentColumn = "id", entityColumn = "thread_id")
  private List<ThreadUserLink> threadUserLinks;

  @Relation(parentColumn = "id", entityColumn = "thread_id")
  private List<SingleChat> singleChatList;

  @Relation(parentColumn = "id", entityColumn = "thread_id")
  private List<ThreadMetadata> threadMetadataList;

  public Thread getThread() {
    return thread;
  }

  public void setThread(Thread thread) {
    this.thread = thread;
  }

  public List<ThreadUserLink> getThreadUserLinks() {
    return threadUserLinks;
  }

  public void setThreadUserLinks(List<ThreadUserLink> threadUserLinks) {
    this.threadUserLinks = threadUserLinks;
  }

  public List<SingleChat> getSingleChatList() {
    return singleChatList;
  }

  public void setSingleChatList(List<SingleChat> singleChatList) {
    this.singleChatList = singleChatList;
  }

  public List<ThreadMetadata> getThreadMetadataList() {
    return threadMetadataList;
  }

  public void setThreadMetadataList(List<ThreadMetadata> threadMetadataList) {
    this.threadMetadataList = threadMetadataList;
  }
}
