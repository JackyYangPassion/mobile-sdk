package com.ripbull.ertc.cache.database;

import android.content.Context;



import com.ripbull.ertc.cache.database.dao.ChatReactionDao;
import com.ripbull.ertc.cache.database.dao.ChatThreadDao;
import com.ripbull.ertc.cache.database.dao.DomainFilterDao;
import com.ripbull.ertc.cache.database.dao.DownloadMediaDao;
import com.ripbull.ertc.cache.database.dao.EKeyDao;
import com.ripbull.ertc.cache.database.dao.GroupDao;
import com.ripbull.ertc.cache.database.dao.ProfanityFilterDao;
import com.ripbull.ertc.cache.database.dao.SingleChatDao;
import com.ripbull.ertc.cache.database.dao.TenantDao;
import com.ripbull.ertc.cache.database.dao.ThreadDao;
import com.ripbull.ertc.cache.database.dao.ThreadUserLinkDao;
import com.ripbull.ertc.cache.database.dao.UserDao;


/** Created by DK on 25/11/18. */

public class DataSourceImpl implements DataSource {

  private static DataSourceImpl instance;
  private final ErtcDatabase sdkDatabase;

  public static DataSource instance(Context context) {
    if (instance == null) {
      synchronized (DataSourceImpl.class) {
        if (instance == null) {
          instance = new DataSourceImpl(ErtcDatabase.getDb(context.getApplicationContext()));
        }
      }
    }
    return instance;
  }

  private DataSourceImpl(ErtcDatabase sdkDatabase) {
    this.sdkDatabase = sdkDatabase;
  }

  @Override
  public UserDao userDao() {
    return sdkDatabase.userDao();
  }

  @Override
  public GroupDao groupDao() {
    return sdkDatabase.groupDao();
  }

  @Override
  public EKeyDao ekeyDao() {
    return sdkDatabase.eKeyDao();
  }

  @Override
  public SingleChatDao singleChatDao() {
    return sdkDatabase.messageDao();
  }

  @Override
  public TenantDao tenantDao() {
    return sdkDatabase.tenantDao();
  }

  @Override
  public ThreadDao threadDao() {
    return sdkDatabase.threadDao();
  }

  @Override
  public ChatThreadDao chatThreadDao() {
    return sdkDatabase.chatThreadDao();
  }

  @Override
  public ThreadUserLinkDao threadUserLinkDao() {
    return sdkDatabase.userLinkDao();
  }

  @Override
  public DownloadMediaDao downloadMediaDao() {
    return sdkDatabase.downloadMediaDao();
  }

  @Override
  public ChatReactionDao chatReactionDao() {
    return sdkDatabase.chatReactionDao();
  }

  @Override
  public DomainFilterDao domainFilterDao() {
    return sdkDatabase.domainFilterDao();
  }

  @Override
  public ProfanityFilterDao profanityFilterDao() {
    return sdkDatabase.profanityFilterDao();
  }
}
