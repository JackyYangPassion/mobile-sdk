package com.ripbull.ertc.cache.db

import com.ripbull.ertc.cache.database.entity.ChatReactionEntity
import com.ripbull.ertc.cache.database.entity.ChatThread
import com.ripbull.ertc.cache.database.entity.DownloadMedia
import com.ripbull.ertc.cache.database.entity.EmailContact
import com.ripbull.ertc.cache.database.entity.Location
import com.ripbull.ertc.cache.database.entity.PhoneContact
import com.ripbull.ertc.cache.database.entity.SingleChat
import com.ripbull.ertc.cache.database.entity.Tenant
import com.ripbull.ertc.cache.database.entity.TenantConfig
import com.ripbull.ertc.cache.database.entity.Thread
import com.ripbull.ertc.cache.database.entity.User
import java.util.*

/**
 * Created by DK on 19/06/20.
 */
object FakeData {
  const val TENANT_ID = "5e540ba9e978107d1900db70";
  const val THREAD_ID = "5eecfcbf25e1140008c96754";

  const val SENDER_CHAT_ID = "5ec25d9dfd06b3075ccfc1b5";
  const val USER_CHAT_ID = "5ec25d9dfd06b3075ccfc1b523";
  const val USER_CHAT_ID1 = "5ec25d9dfd06b3075ccfc1b5231";
  const val USER_CHAT_ID2= "5ec25d9dfd06b3075ccfc1b52322";
  const val USER_ID = "dinesh@ripbullnetworks.com";

  const val RECIPIENT_CHAT_ID = "5ec25d9dfd06b3075ccdmck3";
  const val RECIPIENT_APP_USER_ID = "sagar@ripbullnetworks.com";

  const val LOCAL_MSG_ID = "18e9bad8-5244-44b6-856f-2e1921a52027";
  const val SETVER_MSG_UNIQUE_ID = "5efffd5a02afb5000a34e363";
  const val LOCAL_CHAT_THREAD_MSG_ID = "5efffd5a02afb5000a34e4343";
  const val SERVER_CHAT_THREAD_MSG_UNIQUE_ID = "5efffd5a02afb5000a34e363";

  const val DELETE_FOR_USER = "self"
  const val DELETE_FOR_EVERYONE = "everyone"

  private val LOCATION = Location("address", 12.12321, 47.8211);

  val USER =
    User(
      id = USER_ID,
      tenantId = TENANT_ID,
      name = "Dinesh",
      appState = "active",
      loginType = "login_type",
      profilePic = "profile_pic",
      profileThumb =  "profile_thumb",
      profileStatus = "active",
      loginTimestamp = System.currentTimeMillis(),
      userChatId = USER_CHAT_ID,
      availabilityStatus = "online",
      blockedStatus = "Blocked",
      notificationSettings = "all"
    )

  val TENANT = Tenant(
    TENANT_ID,
    "QA Test",
    "server_generated_key",
    "qa.test.ertc.com")

  val TENANT_CONFIG = TenantConfig(
    TENANT_ID,
    "IS_CHAT_ENABLED",
    "1"
  )

  val THREAD = Thread(
    THREAD_ID,
    "default_name",
    "single",
    TENANT_ID,
    SENDER_CHAT_ID,
    RECIPIENT_CHAT_ID,
    0,
    0,
    0,
    System.currentTimeMillis(),
    0,
    "123-tenant",
    USER_ID,
    USER_ID,
    RECIPIENT_APP_USER_ID,
    System.currentTimeMillis(),
    null,
    System.currentTimeMillis(),
    "3 Days"
  )

  val CHAT_THREAD = ChatThread(
    LOCAL_CHAT_THREAD_MSG_ID, System.currentTimeMillis(), "I'm here! with chat thread message!", 1, "single",
  "dinesh-1", THREAD_ID, "sender", "receiver",
  "Sent", "", SERVER_CHAT_THREAD_MSG_UNIQUE_ID, "", "",
  "Contact Name = Dinesh", "", "", "", phoneContacts(), emailContacts(), "",
  "",LOCATION ,
  0, "", LOCAL_MSG_ID, "")


  val SINGLE_CHAT = SingleChat(
    LOCAL_MSG_ID, System.currentTimeMillis(), "I'm here! with chat thread message!", 1,
    "single",
    USER_ID, THREAD_ID, "sender", "receiver",
    "thread1", "text", SETVER_MSG_UNIQUE_ID, "", "",
    "Contact Name = Dinesh", phoneContacts(), emailContacts(), "",
    "", LOCATION,
    "my_file.pdf", "", ""
  )

  val DOWNLOAD_MEDIA = DownloadMedia(
  785645,
  "http://www.ertc-qa.com/myVideo_575775757.mp4",
  "error",
  "SDCARD://eRTC/media/video",
  "myVideo_575775757.mp4",
  "ffsf-455-dfsfsss",
  System.currentTimeMillis(),
  System.currentTimeMillis(),
  System.currentTimeMillis()
  )

  private fun emailContacts(): MutableList<EmailContact> {
    val emailContacts: MutableList<EmailContact> = ArrayList()
    emailContacts.add(EmailContact("homeThread", "dinesh1@gmail.com"))
    return emailContacts;
  }

  private fun phoneContacts(): MutableList<PhoneContact> {
    val phoneContacts: MutableList<PhoneContact> =
      ArrayList()
    phoneContacts.add(PhoneContact("workThread", "9999999999"))
    return phoneContacts
  }

  val SINGLE_CHAT_REACTION = ChatReactionEntity(
    unicode = "U+1F600",
    threadId = THREAD_ID,
    userChatId = SENDER_CHAT_ID,
    chatMsgId = LOCAL_MSG_ID
  )
  val THREAD_CHAT_REACTION = ChatReactionEntity(
    unicode = "U+1F601",
    threadId = THREAD_ID,
    userChatId = SENDER_CHAT_ID,
    chatThreadMsgId = LOCAL_CHAT_THREAD_MSG_ID
  )
  val THREAD_CHAT_REACTIONS = arrayListOf(
    ChatReactionEntity(
      1,
      "U+1F6001",
      THREAD_ID,
      USER_CHAT_ID,
      chatThreadMsgId = LOCAL_CHAT_THREAD_MSG_ID
    ),
    ChatReactionEntity(
      2,
      "U+1F6002",
      THREAD_ID,
      USER_CHAT_ID1,
      chatThreadMsgId = LOCAL_CHAT_THREAD_MSG_ID
    ),
    ChatReactionEntity(
      3,
      "U+1F6003",
      THREAD_ID,
      USER_CHAT_ID2,
      chatThreadMsgId = LOCAL_CHAT_THREAD_MSG_ID
    )
  )

}