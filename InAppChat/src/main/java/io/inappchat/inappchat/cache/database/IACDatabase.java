package io.inappchat.inappchat.cache.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import io.inappchat.inappchat.cache.database.converter.DateTypeConverter;
import io.inappchat.inappchat.cache.database.converter.UserListConverter;
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
import io.inappchat.inappchat.cache.database.entity.ChatReactionEntity;
import io.inappchat.inappchat.cache.database.entity.DomainFilter;
import io.inappchat.inappchat.cache.database.entity.DownloadMedia;
import io.inappchat.inappchat.cache.database.entity.ChatThread;
import io.inappchat.inappchat.cache.database.entity.EKeyTable;
import io.inappchat.inappchat.cache.database.entity.Group;
import io.inappchat.inappchat.cache.database.entity.GroupMetadata;
import io.inappchat.inappchat.cache.database.entity.ProfanityFilter;
import io.inappchat.inappchat.cache.database.entity.SingleChat;
import io.inappchat.inappchat.cache.database.entity.SingleChatMetadata;
import io.inappchat.inappchat.cache.database.entity.Tenant;
import io.inappchat.inappchat.cache.database.entity.TenantConfig;
import io.inappchat.inappchat.cache.database.entity.ThreadMetadata;
import io.inappchat.inappchat.cache.database.entity.ThreadUserLink;
import io.inappchat.inappchat.cache.database.entity.Thread;
import io.inappchat.inappchat.cache.database.entity.User;
import io.inappchat.inappchat.cache.database.entity.UserMetadata;

@Database(
    entities = {
      SingleChat.class, SingleChatMetadata.class, Tenant.class, TenantConfig.class, User.class,
      Group.class, GroupMetadata.class, UserMetadata.class, Thread.class, ThreadUserLink.class,
      ThreadMetadata.class, EKeyTable.class, ChatThread.class,DownloadMedia.class, ChatReactionEntity.class,
      DomainFilter.class, ProfanityFilter.class
    },
    version = Migrations.DB_VERSION)
@TypeConverters({DateTypeConverter.class, UserListConverter.class, Converters.class})
public abstract class IACDatabase extends RoomDatabase {

  private static final String DB_NAME = "ertc_db";

  private static volatile IACDatabase INSTANCE;

  static IACDatabase getDb(Context context) {
    if (INSTANCE == null) {
      synchronized (IACDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE =
              Room.databaseBuilder(context, IACDatabase.class, IACDatabase.DB_NAME)
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
