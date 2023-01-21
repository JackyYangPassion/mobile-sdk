package io.inappchat.inappchat.chat

import com.ripbull.ertc.cache.database.entity.Location

object FakeRepoData {
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
}