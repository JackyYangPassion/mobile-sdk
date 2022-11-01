package com.ripbull.coresdk.core.type



/** Created by DK on 01/03/19.  */

enum class EventType {
  // Thread
  THREAD_ADDED,
  THREAD_REMOVED,
  THREAD_DETAILS_UPDATED,
  THREAD_LAST_MESSAGE_UPDATED,
  THREAD_USERS_CHANGED,
  THREAD_READ,
  THREAD_READ_RECIPIENT,
  THREAD_META_DATA_UPDATED,
  // Message
  MESSAGE_REMOVED,
  MESSAGE_ADDED,
  MESSAGE_UPDATED,
  MESSAGE_MEDIA_DOWNLOAD,
  MESSAGE_META_DATA,
  TYPING_STATE_CHANGED,
  USER_META_DATA_UPDATED,
  USER_PRESENCE_UPDATED,
  // Contacts
  CONTACTS_ADDED,
  CONTACTS_REMOVED,
  CONTACTS_CHANGED,
  CONTACTS_UPDATE,
  // group
  GROUP_UPDATED,
  //thread message
  THREAD_MESSAGE_METADATA,
  MESSAGE_REACTION,
  // Announcement
  ANNOUNCEMENT_RECEIVED,
  // logout
  LOGOUT,
  //Tenant config updated
  CONFIG_CHANGED,
  //User DB Update
  USER_DB_UPDATED,
  //Clear Chat History
  CHAT_CLEARED,
  //Chat Setting Updated
  CHAT_SETTINGS_UPDATED
}