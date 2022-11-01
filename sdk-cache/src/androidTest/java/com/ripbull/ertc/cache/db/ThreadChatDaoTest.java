package com.ripbull.ertc.cache.db;

import android.content.Context;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.ripbull.ertc.cache.database.ErtcDatabase;
import com.ripbull.ertc.cache.database.dao.ChatThreadDao;
import com.ripbull.ertc.cache.database.dao.SingleChatDao;
import com.ripbull.ertc.cache.database.dao.TenantDao;
import com.ripbull.ertc.cache.database.dao.ThreadDao;
import com.ripbull.ertc.cache.database.entity.ChatThread;
import com.ripbull.ertc.cache.database.entity.EmailContact;
import com.ripbull.ertc.cache.database.entity.Location;
import com.ripbull.ertc.cache.database.entity.PhoneContact;
import com.ripbull.ertc.cache.database.entity.SingleChat;
import com.ripbull.ertc.cache.database.entity.Tenant;
import com.ripbull.ertc.cache.database.entity.TenantAndConfig;
import com.ripbull.ertc.cache.database.entity.TenantConfig;
import com.ripbull.ertc.cache.database.entity.Thread;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/** Created by DK on 22/12/18. */
@RunWith(AndroidJUnit4.class)
public class ThreadChatDaoTest {
  ErtcDatabase database;
  private TenantDao tenantDao;
  private ThreadDao threadDao;
  private SingleChatDao singleChatDao;
  private ChatThreadDao chatThreadDao;

  @Before
  public void createDb() {
    Context appContext = InstrumentationRegistry.getTargetContext();
    database = Room.inMemoryDatabaseBuilder(appContext, ErtcDatabase.class)
        .allowMainThreadQueries()
        .build();
    tenantDao = database.tenantDao();
    threadDao = database.threadDao();
    singleChatDao = database.messageDao();
    chatThreadDao = database.chatThreadDao();
  }

  @After
  public void closeDb() throws Exception {
    // database.close();
  }

  @Test
  public void testTenantDao() {
    TenantAndConfig tenantAndConfig = new TenantAndConfig();
    Tenant tenant = new Tenant("", "", "", "");
    tenant.setId("123");
    tenant.setApiKey("Test_Key");
    tenant.setName("Test_Value");
    tenant.setNamespace("Test_Value");
    tenantAndConfig.setTenant(tenant);
    tenantDao.save(tenant);

    ArrayList<TenantConfig> list = new ArrayList<>();
    TenantConfig tenantConfig = new TenantConfig("'", "", "");
    tenantConfig.setKey("Test11_key");
    tenantConfig.setValue("Test11_value");
    tenantConfig.setTenantId("123");
    list.add(tenantConfig);
    tenantAndConfig.setConfigs(list);
    tenantDao.save(list);
  }

  @Test
  public void testThreadDao() {
    testTenantDao();
    Thread thread =
            new Thread("123-thread", "", "single", "123", "senderChatId-1", "recipientChatId-1", 0, 0,
                    0, System.currentTimeMillis(), 0, "123-tenant", "creatorUserId-1", "senderUserId-1",
                    "recipientUserId-1", System.currentTimeMillis(), "all");

    threadDao.insert(thread);

    Thread buddyInfo = threadDao.getThreadByIdInSync("123-thread");
    Assert.assertEquals(buddyInfo.getName(), thread.getName());
  }


  @Test
  public void testChatWindow() {
    testThreadDao();

    List<EmailContact> emailContacts = new ArrayList<>();
    emailContacts.add(new EmailContact("home", "dinesh@gmail.com"));
    List<PhoneContact> phoneContacts = new ArrayList<>();
    phoneContacts.add(new PhoneContact("work", "9292929299"));
    SingleChat singleChat =
        new SingleChat("11", System.currentTimeMillis(), "Hello? how're you?", 1, "",
            "dinesh-1", "123-thread", "sender", "receiver",
                "Sent", "", "server-generated-1", "", "",
            "Contact Name = Dinesh", phoneContacts, emailContacts, "",
            "", new Location("address", 12.12321, 47.8211),
            "my_file.pdf", "", "");

    singleChatDao.insert(singleChat);

    SingleChat buddyInfo = singleChatDao.getChatByLocalMsgId("11", "123-thread");
    Assert.assertEquals(buddyInfo.getPhoneContactList().size(),
        singleChat.getPhoneContactList().size());
  }

  @Test
  public void testChatWindowAlongWithChatThread() {
    testChatWindow();

    List<EmailContact> emailContacts = new ArrayList<>();
    emailContacts.add(new EmailContact("homeThread", "dinesh1@gmail.com"));
    List<PhoneContact> phoneContacts = new ArrayList<>();
    phoneContacts.add(new PhoneContact("workThread", "9999999999"));
    ChatThread chatThread =
            new ChatThread("111", System.currentTimeMillis(), "I'm here! with chat thread message!", 1, "",
                    "dinesh-1", "123-thread", "sender", "receiver",
                    "Sent", "", "server-generated-2", "", "",
                    "Contact Name = Dinesh","","","", phoneContacts, emailContacts, "",
                    "", new Location("address", 12.12321, 47.8211),
                    0, "false", "11");

    chatThreadDao.insert(chatThread);

    ChatThread chatThread1 = chatThreadDao.getChatByClientId("111","");
    Assert.assertEquals(chatThread1.getPhoneContactList().size(),
            chatThread1.getPhoneContactList().size());

    ChatThread chatThreadDB = chatThreadDao.getChatByServerMsgId("server-generated-2","123-thread");
    Assert.assertEquals(chatThreadDB.getStatus(),"Sent");
  }

  @Test
  public void testChatWindowAlongWithChatThreadWhenSendToChannelIs1() {
    testChatWindow();

    List<EmailContact> emailContacts = new ArrayList<>();
    emailContacts.add(new EmailContact("homeThread", "dinesh1@gmail.com"));
    List<PhoneContact> phoneContacts = new ArrayList<>();
    phoneContacts.add(new PhoneContact("workThread", "9999999999"));
    ChatThread chatThread =
            new ChatThread("111", System.currentTimeMillis(), "I'm here! with chat thread message!", 1, "",
                    "dinesh-1", "123-thread", "sender", "receiver",
                    "Sent", "", "server-generated-2", "", "",
                    "Contact Name = Dinesh","","","", phoneContacts, emailContacts, "",
                    "", new Location("address", 12.12321, 47.8211),
                    1, "false", "11");

    chatThreadDao.insert(chatThread);


    emailContacts = new ArrayList<>();
    emailContacts.add(new EmailContact("homeThread", "dinesh@gmail.com"));
    phoneContacts = new ArrayList<>();
    phoneContacts.add(new PhoneContact("workThread", "9292929299"));
    SingleChat singleChat =
            new SingleChat("111", System.currentTimeMillis(), "I'm here! with chat thread message!", 1, "",
                    "1", "123-thread", "sender", "receiver",
                    "thread1", "", "server-generated-2", "", "",
                    "Contact Name = Dinesh", phoneContacts, emailContacts, "",
                    "", new Location("address", 12.12321, 47.8211),
                    "my_file.pdf", "", "");

    singleChatDao.insert(singleChat);

    SingleChat singleChat1 = singleChatDao.getChatByServerMsgId("server-generated-2","123-thread");
    Assert.assertEquals(singleChat1.getPhoneContactList().size(), singleChat1.getPhoneContactList().size());

    ChatThread chatThread1 = chatThreadDao.getChatByServerMsgId("server-generated-2","123-thread");
    Assert.assertEquals(chatThread1.getStatus(),"Sent");
  }

}