package com.ripbull.ertc.mqtt.utils;

/** Created by DK on 23/02/19. */
public class Constants {
  public static final String TAG = "MQTT";

  // Mqtt Topic
  public static final String CHAT_TOPIC = "chat:";
  public static final String CHAT_UPDATE_TOPIC = "chatUpdate:";
  public static final String MSG_READ_STATUS_TOPIC = "msgReadStatus";
  public static final String TYPING_STATUS_TOPIC = "typingStatus";
  public static final String AVAILABILITY_STATUS_TOPIC = "availabilityStatus";
  public static final String GROUP_UPDATED = "groupUpdated";
  public static final String TENANT_CONFIG_MODIFIED = "tenantConfigUpdated";
  public static final String USER_DB_UPDATED = "userDbUpdated";
  public static final String SUBSCRIBE = "subscribe";
  public static final String ANNOUNCEMENT = "announcement";
  public static final String CHAT_HISTORY_CLEARED = "chatHistoryCleared";
  public static final String CHAT_SETTING_UPDATED = "chatSettingUpdated";
  /**
   *
   */
  public static final String USER_SELF_UPDATE = "userSelfUpdate";
  public static final String CHAT_REACTION = "chatReaction:";

  //to filter FCM data
  public static final String CHAT_EVENT = "chat";
  public static final String CHAT_REACTION_EVENT = "chatReaction";
  public static final String CHAT_UPDATE_EVENT = "chatUpdate";
  public static final String LOGOUT = "logout";

  //User DB Events
  public static final String USER_ADD_UPDATED = "addUpdated";
  public static final String USER_DELETED = "deleted";
  public static final String USER_INACTIVE = "inactive";
}
