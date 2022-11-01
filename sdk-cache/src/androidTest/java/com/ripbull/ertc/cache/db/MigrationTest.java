package com.ripbull.ertc.cache.db;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.ripbull.ertc.cache.database.ErtcDatabase;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Created by Sagar on 20/02/20. */
@RunWith(AndroidJUnit4.class)
public class MigrationTest {
  private static final String TEST_DB = "migration-test";

  static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
      // Since we didn't alter the table, there's nothing else to do here.
    }
  };

  static final Migration MIGRATION_2_3 = new Migration(2, 3) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
      database.execSQL("ALTER TABLE single_chat " + " ADD COLUMN media_name TEXT default NULL");
    }
  };

  static final Migration MIGRATION_3_4 = new Migration(3, 4) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
      database.execSQL("ALTER TABLE thread ADD COLUMN updated_at INTEGER NOT NULL default " + System.currentTimeMillis());
    }
  };

  static final Migration MIGRATION_4_5 = new Migration(4, 5) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
      database.execSQL("ALTER TABLE single_chat " + " ADD COLUMN local_media_path TEXT default NULL");
      database.execSQL("ALTER TABLE single_chat " + " ADD COLUMN download_status TEXT default NULL");
      database.execSQL("CREATE TABLE download ("
          + "id INTEGER PRIMARY KEY NOT NULL, "
          + "url TEXT, "
          + "etag TEXT, "
          + "dir_path TEXT, "
          + "file_name TEXT, "
          + "msg_id TEXT, "
          + "total_bytes INTEGER, "
          + "downloaded_bytes INTEGER, "
          + "last_modified_at INTEGER"
          + ")");
    }
  };

  static final Migration MIGRATION_5_6 = new Migration(5, 6) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
      database.execSQL("DROP INDEX IF EXISTS `index_single_chat_id`");
      database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_single_chat_id_thread_id` ON  `single_chat`(`id`, `thread_id`)");
      database.execSQL("ALTER TABLE single_chat " + " ADD COLUMN send_to_channel INTEGER default 0");
      database.execSQL("ALTER TABLE single_chat " + " ADD COLUMN parent_msg_id TEXT default NULL");
      database.execSQL("ALTER TABLE single_chat " + " ADD COLUMN parent_msg TEXT default NULL");
      database.execSQL("CREATE TABLE IF NOT EXISTS chat_thread (`id` TEXT NOT NULL, `createdAt` INTEGER, "
          + "`message` TEXT, `read` INTEGER, `type` TEXT, `sender_id` TEXT, `thread_id` TEXT, `next_msg_id` TEXT, "
          + "`last_msg_id` TEXT, `status` TEXT, `msg_type` TEXT, `msg_unique_id` TEXT, `media_path` TEXT, "
          + "`media_thumbnail` TEXT, `media_name` TEXT, `local_media_path` TEXT, `download_status` TEXT, "
          + "`contact_name` TEXT, `phone_contact_list` TEXT, `email_contact_list` TEXT, `isStarredChat` TEXT, "
          + "`gif_path` TEXT, `send_to_channel` INTEGER, `parent_msg_id` TEXT, `address` TEXT, `latitude` REAL, `longitude` REAL, "
          + "PRIMARY KEY(`id`), FOREIGN KEY(`parent_msg_id`) REFERENCES `single_chat`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
      database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_chat_thread_id_parent_msg_id` ON  `chat_thread`(`id`, `parent_msg_id`)");
    }
  };

  static final Migration MIGRATION_6_7 = new Migration(6, 7) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
      database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_single_chat_msg_unique_id_thread_id` " +
              "ON  `single_chat`(`msg_unique_id`, `thread_id`)");
      database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_chat_thread_msg_unique_id_thread_id` " +
              "ON  `chat_thread`(`msg_unique_id`, `thread_id`)");
      database.execSQL("ALTER TABLE user ADD COLUMN app_state TEXT default 'active'");
      database.execSQL("ALTER TABLE single_chat ADD COLUMN event_type TEXT default NULL");
      database.execSQL("ALTER TABLE thread ADD COLUMN notification_settings TEXT default 'all'");
      database.execSQL("ALTER TABLE user ADD COLUMN notification_settings TEXT default 'all'");
    }
  };

  static final Migration MIGRATION_7_8 = new Migration(7, 8) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
      database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_chat_reaction_chat_msg_id_unicode_user_chat_id` " +
          "ON  `chat_reaction`(`chat_msg_id`, `unicode`, `user_chat_id`)");
      database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_chat_reaction_chat_thread_msg_id_unicode_user_chat_id` " +
          "ON  `chat_reaction`(`chat_thread_msg_id`, `unicode`, `user_chat_id`)");
      database.execSQL("ALTER TABLE single_chat ADD COLUMN forward_msg_unique_id TEXT default NULL");
      database.execSQL("ALTER TABLE single_chat ADD COLUMN is_forward_message INTEGER default 0");
    }
  };

  static final Migration MIGRATION_8_9 = new Migration(8, 9) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
      database.execSQL("ALTER TABLE single_chat ADD COLUMN client_created_at INTEGER NOT NULL default " + System.currentTimeMillis());
      database.execSQL("ALTER TABLE chat_thread ADD COLUMN client_created_at INTEGER NOT NULL default " + System.currentTimeMillis());
    }
  };

  static final Migration MIGRATION_9_10 = new Migration(9, 10) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
      database.execSQL("ALTER TABLE single_chat ADD COLUMN custom_data TEXT default NULL");
      database.execSQL("ALTER TABLE chat_thread ADD COLUMN custom_data TEXT default NULL");
      database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_eKeys_keyId_deviceId_ertcUserId` " +
          "ON  `eKeys`(`keyId`, `deviceId`, `ertcUserId`)");
      database.execSQL("ALTER TABLE single_chat ADD COLUMN is_followed INTEGER default 0");
      database.execSQL("ALTER TABLE single_chat ADD COLUMN chat_report_id TEXT default NULL");
      database.execSQL("ALTER TABLE single_chat ADD COLUMN report_type TEXT default NULL");
      database.execSQL("ALTER TABLE single_chat ADD COLUMN reason TEXT default NULL");
      database.execSQL("ALTER TABLE chat_thread ADD COLUMN chat_report_id TEXT default NULL");
      database.execSQL("ALTER TABLE chat_thread ADD COLUMN report_type TEXT default NULL");
      database.execSQL("ALTER TABLE chat_thread ADD COLUMN reason TEXT default NULL");
      database.execSQL("ALTER TABLE `group` ADD COLUMN joined INTEGER default 0");
      database.execSQL("ALTER TABLE `group` ADD COLUMN participants_count INTEGER default 0");
      database.execSQL("ALTER TABLE `group` ADD COLUMN group_status TEXT NOT NULL default 'active'");
    }
  };

  // Array of all migrations
  private static final Migration[] ALL_MIGRATIONS = new Migration[]{ MIGRATION_1_2, MIGRATION_2_3,
      MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10};

  @Rule
  public MigrationTestHelper testHelper =
      new MigrationTestHelper(
          InstrumentationRegistry.getInstrumentation(),
          ErtcDatabase.class.getCanonicalName(),
          new FrameworkSQLiteOpenHelperFactory());

  @Test
  public void migrateAll() throws IOException {

    // Create earliest version of the database.
    SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB, 1);
    db.close();

    // Open latest version of the database. Room will validate the schema
    // once all migrations execute.
    ErtcDatabase ertcDB = Room.databaseBuilder(
        InstrumentationRegistry.getInstrumentation().getTargetContext(),
        ErtcDatabase.class,
        TEST_DB).addMigrations(ALL_MIGRATIONS).build();
    ertcDB.getOpenHelper().getWritableDatabase();
    ertcDB.close();
  }

  @Test
  public void migrate1To3() throws IOException {

    // Create earliest version of the database.
    SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB, 1);
    db.close();

    // Re-open the database with version 3 and provide
    // MIGRATION_1_2 and MIGRATION_2_3 as the migration process.
    testHelper.runMigrationsAndValidate(
        TEST_DB,
        3,
        true,
        MIGRATION_1_2,
        MIGRATION_2_3
    );
  }

  @Test
  public void migrate1To4() throws IOException {

    // Create earliest version of the database.
    SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB, 1);
    db.close();

    // Re-open the database with version 3 and provide
    // MIGRATION_1_2 and MIGRATION_2_3 as the migration process.
    testHelper.runMigrationsAndValidate(
        TEST_DB,
        4,
        true,
        MIGRATION_1_2,
        MIGRATION_2_3,
        MIGRATION_3_4
    );
  }

  @Test
  public void migrate1To5() throws IOException {

    // Create earliest version of the database.
    SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB, 1);
    db.close();

    // Re-open the database with version 3 and provide
    // MIGRATION_1_2 and MIGRATION_2_3 as the migration process.
    testHelper.runMigrationsAndValidate(
        TEST_DB,
        5,
        true,
        MIGRATION_1_2,
        MIGRATION_2_3,
        MIGRATION_3_4,
        MIGRATION_4_5
    );
  }

  @Test
  public void migrate1To6() throws IOException {

    // Create earliest version of the database.
    SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB, 1);
    db.close();

    testHelper.runMigrationsAndValidate(
        TEST_DB,
        6,
        true,
        MIGRATION_1_2,
        MIGRATION_2_3,
        MIGRATION_3_4,
        MIGRATION_4_5,
        MIGRATION_5_6
    );
  }

  @Test
  public void migrate7To9() throws IOException {

    // Create earliest version of the database.
    SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB, 7);
    db.close();

    testHelper.runMigrationsAndValidate(
        TEST_DB,
        9,
        true,
        MIGRATION_7_8,
        MIGRATION_8_9
    );
  }

  @Test
  public void migrate9To10() throws IOException {
    // Create earliest version of the database.
    SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB, 9);
    db.close();

    testHelper.runMigrationsAndValidate(
        TEST_DB,
        10,
        true,
        MIGRATION_9_10
    );
  }
}
