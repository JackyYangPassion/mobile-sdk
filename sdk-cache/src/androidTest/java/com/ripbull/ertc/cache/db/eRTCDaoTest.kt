package com.ripbull.ertc.cache.db

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.ripbull.ertc.cache.database.ErtcDatabase
import com.ripbull.ertc.cache.database.dao.DownloadMediaDao
import com.ripbull.ertc.cache.database.dao.SingleChatDao
import com.ripbull.ertc.cache.database.dao.TenantDao
import com.ripbull.ertc.cache.database.dao.ThreadDao
import com.ripbull.ertc.cache.database.dao.UserDao
import com.ripbull.ertc.cache.database.entity.EmailContact
import com.ripbull.ertc.cache.database.entity.Location
import com.ripbull.ertc.cache.database.entity.PhoneContact
import com.ripbull.ertc.cache.database.entity.SingleChat
import com.ripbull.ertc.cache.database.entity.Tenant
import com.ripbull.ertc.cache.database.entity.TenantAndConfig
import com.ripbull.ertc.cache.database.entity.TenantConfig
import com.ripbull.ertc.cache.database.entity.Thread
import com.ripbull.ertc.cache.database.entity.User
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/** Created by DK on 22/12/18.  */
@RunWith(AndroidJUnit4::class)
class eRTCDaoTest {
  var database: ErtcDatabase? = null
  private var userDao: UserDao? = null
  private var tenantDao: TenantDao? = null
  private var threadDao: ThreadDao? = null
  private var singleChatDao: SingleChatDao? = null
  private var downloadMediaDao: DownloadMediaDao? = null
  @Before
  fun `createDb`() {
    val appContext = InstrumentationRegistry.getTargetContext()
    database =
      Room.inMemoryDatabaseBuilder(appContext, ErtcDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    userDao = database!!.userDao()
    tenantDao = database!!.tenantDao()
    threadDao = database!!.threadDao()
    singleChatDao = database!!.messageDao()
    downloadMediaDao = database!!.downloadMediaDao()
  }

  @After
  @Throws(Exception::class)
  fun `closeDb`() { // database.close();
  }

  @Test
  fun `testUserDao`() {
    testTenantDao()
    val user =
      User("")
    user.name = "Dinesh"
    user.profilePic = "profile_poc"
    user.id = "1"
    user.tenantId = "123"
    user.appState = "active"
    user.blockedStatus = "Blocked"
    user.availabilityStatus = "Away"
    userDao!!.insert(user)
    user.name = "Dinesh"
    user.profilePic = "profile_poc"
    user.id = "5"
    user.tenantId = "123"
    user.appState = "active"
    user.blockedStatus = "Blocked"
    user.availabilityStatus = "Away"
    userDao!!.insert(user)
    val buddyInfo =
      userDao!!.getUsers("123", "1").blockingFirst()
    Assert.assertEquals(buddyInfo.size.toLong(), 1)
  }

  @Test
  fun `testTenantDao`() {
    val tenantAndConfig = TenantAndConfig()
    val tenant = Tenant("", "", "", "")
    tenant.id = "123"
    tenant.apiKey = "Test_Key"
    tenant.name = "Test_Value"
    tenant.namespace = "Test_Value"
    tenantAndConfig.tenant = tenant
    tenantDao!!.save(tenant)
    val list =
      ArrayList<TenantConfig>()
    val tenantConfig =
      TenantConfig("'", "", "")
    tenantConfig.key = "Test11_key"
    tenantConfig.value = "Test11_value"
    tenantConfig.tenantId = "123"
    list.add(tenantConfig)
    tenantAndConfig.configs = list
    tenantDao!!.save(list)
    //tenantDao.getTenant("123")
    //    .test()
    //    .assertValue(tenantAndConfig1 -> tenantAndConfig1 != null && tenantAndConfig1.getTenant()
    //        .getId()
    //        .equals("123"));
  }

  @Test
  fun `testThreadDao`() {
    testTenantDao()
    val thread =
      Thread(
        "123-thread",
        "",
        "single",
        "123",
        "senderChatId-1",
        "recipientChatId-1",
        0,
        0,
        0,
        System.currentTimeMillis(),
        0,
        "123-tenant",
        "creatorUserId-1",
        "senderUserId-1",
        "recipientUserId-1",
        System.currentTimeMillis(),
        null
      )
    threadDao!!.insert(thread)
    val buddyInfo =
      threadDao!!.getThreadByIdInSync("123-thread")
    Assert.assertEquals(buddyInfo.name, thread.name)
  }

  @Test
  fun `testMessageDao`() {
    testThreadDao()
    val emailContacts: MutableList<EmailContact> =
      ArrayList()
    emailContacts.add(EmailContact("home", "dinesh@gmail.com"))
    val phoneContacts: MutableList<PhoneContact> =
      ArrayList()
    phoneContacts.add(PhoneContact("work", "9292929299"))
    val singleChat = SingleChat(
      "11", System.currentTimeMillis(), "send message", 1, "",
      "1", "123-thread", "sender", "receiver",
      "thread1", "", "", "", "",
      "Contact Name = Dinesh", phoneContacts, emailContacts, "",
      "", Location("address", 12.12321, 47.8211),
      "my_file.pdf", "", ""
    )
    singleChatDao!!.insert(singleChat)
    val (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, phoneContactList) = singleChatDao!!.getChatByLocalMsgId(
      "11",
      "123-thread"
    )
    Assert.assertEquals(
      phoneContactList!!.size.toLong(),
      singleChat.phoneContactList!!.size.toLong()
    )
  }

  @Test
  fun `testDownloadDao`() {
    downloadMediaDao!!.insert(FakeData.DOWNLOAD_MEDIA)
  }

  @Test
  fun `test`() {
    testTenantDao()
    val thread =
      Thread(
        "123-thread",
        "",
        "single",
        "123",
        "senderChatId-1",
        "recipientChatId-1",
        0,
        0,
        0,
        System.currentTimeMillis(),
        0,
        "123-tenant",
        "creatorUserId-1",
        "senderUserId-1",
        "recipientUserId-1",
        System.currentTimeMillis(),
        null
      )
    threadDao!!.insert(thread)
    val buddyInfo =
      threadDao!!.getThreadByIdInSync("123-thread")
    Assert.assertEquals(buddyInfo.name, thread.name)
  }
}