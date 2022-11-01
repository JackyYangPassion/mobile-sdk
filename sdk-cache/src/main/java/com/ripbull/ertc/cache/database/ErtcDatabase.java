package com.ripbull.ertc.cache.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ripbull.ertc.cache.database.converter.DateTypeConverter;
import com.ripbull.ertc.cache.database.converter.UserListConverter;
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
import com.ripbull.ertc.cache.database.entity.ChatReactionEntity;
import com.ripbull.ertc.cache.database.entity.DomainFilter;
import com.ripbull.ertc.cache.database.entity.DownloadMedia;
import com.ripbull.ertc.cache.database.entity.ChatThread;
import com.ripbull.ertc.cache.database.entity.EKeyTable;
import com.ripbull.ertc.cache.database.entity.Group;
import com.ripbull.ertc.cache.database.entity.GroupMetadata;
import com.ripbull.ertc.cache.database.entity.ProfanityFilter;
import com.ripbull.ertc.cache.database.entity.SingleChat;
import com.ripbull.ertc.cache.database.entity.SingleChatMetadata;
import com.ripbull.ertc.cache.database.entity.Tenant;
import com.ripbull.ertc.cache.database.entity.TenantConfig;
import com.ripbull.ertc.cache.database.entity.ThreadMetadata;
import com.ripbull.ertc.cache.database.entity.ThreadUserLink;
import com.ripbull.ertc.cache.database.entity.Thread;
import com.ripbull.ertc.cache.database.entity.User;
import com.ripbull.ertc.cache.database.entity.UserMetadata;

@Database(
    entities = {
      SingleChat.class, SingleChatMetadata.class, Tenant.class, TenantConfig.class, User.class,
      Group.class, GroupMetadata.class, UserMetadata.class, Thread.class, ThreadUserLink.class,
      ThreadMetadata.class, EKeyTable.class, ChatThread.class,DownloadMedia.class, ChatReactionEntity.class,
      DomainFilter.class, ProfanityFilter.class
    },
    version = Migrations.DB_VERSION)
@TypeConverters({DateTypeConverter.class, UserListConverter.class, Converters.class})
public abstract class ErtcDatabase extends RoomDatabase {

  private static final String DB_NAME = "ertc_db";

  private static volatile ErtcDatabase INSTANCE;

  static ErtcDatabase getDb(Context context) {
    if (INSTANCE == null) {
      synchronized (ErtcDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE =
              Room.databaseBuilder(context, ErtcDatabase.class, ErtcDatabase.DB_NAME)
                  .addMigrations(Migrations.getMigrations())
                  .build();
        }
      }
    }
    return INSTANCE;
  }

  public abstract SingleChatDao messageDao();

  public abstract ChatThreadDao chatThreadDao();

  public abstract TenantDao tenantDao();

  public abstract ThreadDao threadDao();

  public abstract UserDao userDao();

  public abstract ThreadUserLinkDao userLinkDao();

  public abstract GroupDao groupDao();

  public abstract EKeyDao eKeyDao();

  public abstract DownloadMediaDao downloadMediaDao();

  public abstract ChatReactionDao chatReactionDao();

  public abstract DomainFilterDao domainFilterDao();

  public abstract ProfanityFilterDao profanityFilterDao();
}
