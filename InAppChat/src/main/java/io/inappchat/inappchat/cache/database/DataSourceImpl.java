package io.inappchat.inappchat.cache.database;

import android.content.Context;



import io.inappchat.inappchat.cache.database.dao.ChatReactionDao;
import io.inappchat.inappchat.cache.database.dao.ChatThreadDao;
import io.inappchat.inappchat.cache.database.dao.DomainFilterDao;
import io.inappchat.inappchat.cache.database.dao.DownloadMediaDao;
import io.inappchat.inappchat.cache.database.dao.EKeyDao;
import io.inappchat.inappchat.cache.database.dao.GroupDao;
import io.inappchat.inappchat.cache.database.dao.ProfanityFilterDao;
import io.inappchat.inappchat.cache.database.dao.SingleChatDao;
import io.inappchat.inappchat.cache.database.dao.TenantDao;
import io.inappchat.inappchat.cache.database.dao.ThreadDao;
import io.inappchat.inappchat.cache.database.dao.ThreadUserLinkDao;
import io.inappchat.inappchat.cache.database.dao.UserDao;


/** Created by DK on 25/11/18. */

public class DataSourceImpl implements DataSource {

  private static DataSourceImpl instance;
  private final IACDatabase sdkDatabase;

  public static DataSource instance(Context context) {
    if (instance == null) {
      synchronized (DataSourceImpl.class) {
        if (instance == null) {
          instance = new DataSourceImpl(IACDatabase.getDb(context.getApplicationContext()));
        }
      }
    }
    return instance;
  }

  private DataSourceImpl(IACDatabase sdkDatabase) {
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
