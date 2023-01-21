package io.inappchat.inappchat.utils;

/** Created by DK on 31/12/18. */
public interface Constants {
  interface TenantConfig {
    String CHAT_ENABLED = "chat_enabled";
    String GROUP_ENABLED = "group_enabled";
    String TYPING_STATUS = "TYPING_STATUS";
    String READ_RECEIPTS = "READ_RECEIPTS";
    String USER_PROFILE = "user_profile";
    String BLOCK_USER = "block_user";
    String STAR_MESSAGE = "star_message";
    String E2E_CHAT = "e2e_chat";
    String NOTIFICATION = "NOTIFICATION";
    String SEARCH_FILTER = "SEARCH_FILTER";
    String ATTACHMENT = "ATTACHMENT";
    String FORWARD_CHAT = "forward_chat";
    String EDIT_CHAT = "edit_chat";
    String DELETE_CHAT = "delete_chat";
    String CHAT_REACTIONS = "chat_reactions";
    String REPLY_THREAD = "reply_thread";
    String FOLLOW_CHAT = "follow_chat";
    String CHANNEL_SEARCH = "channel_search";
    String GLOBAL_SEARCH = "global_search";
    String LOCAL_SEARCH = "local_search";
    String USER_MENTIONS = "user_mentions";
    String ANNOUNCEMENT = "announcement";
    String COPY = "copy";
    String MODERATION = "moderation";
    String DOMAIN_FILTER = "domain_filter";
    String PROFANITY_FILTER = "profanity_filter";
    interface SingeChat {
      interface Attachment {
        String TEXT = "TEXT";
        String LOCATION = "LOCATION";
        String CONTACT = "CONTACT";
        String GIFY = "GIFY";
        String IMAGE = "IMAGE";
        String AUDIO = "AUDIO";
        String VIDEO = "VIDEO";
        String DOCUMENT = "DOCUMENT";

      }
      interface Notification {
        String MUTE_SETTINGS = "MUTE_SETTINGS";
      }

    }
    interface ReadReceipts {
      String SENT = "SENT";
      String READ = "READ";
      String DELIVERED = "DELIVERED";

    }
    interface E2E {
      String TEXT = "TEXT";
      String LOCATION = "LOCATION";
      String CONTACT = "CONTACT";
      String GIFY = "GIFY";
      String MEDIA = "MEDIA";

    }
    interface GroupChat {
      String ALLOW_ADMIN_CHANGE = "allowAdminChange";

      interface Attachment {
        String TEXT = "TEXT";
        String LOCATION = "LOCATION";
        String CONTACT = "CONTACT";
        String GIFY = "GIFY";
        String IMAGE = "IMAGE";
        String AUDIO = "AUDIO";
        String VIDEO = "VIDEO";
        String DOCUMENT = "DOCUMENT";

      }
      interface Notification {
        String MUTE_SETTINGS = "MUTE_SETTINGS";
      }

    }
    interface UserProfile {
      String IMAGE = "IMAGE";
      String NAME = "NAME";
      String FAV_CONTACTS = "FAV_CONTACTS";
      String AVAILABLE_STATUS = "AVAILABLE_STATUS";
      interface Image {
        String EDITABLE = "EDITABLE";
      }

      interface Name {
        String EDITABLE = "EDITABLE";
      }

      interface AvailableStatus {
        String ALLOW_OVERRIDING = "ALLOW_OVERRIDING";
      }

      interface Notification {
        String MUTE_SETTINGS = "MUTE_SETTINGS";
      }
    }

    interface DeleteChat {
      String DELETE_FOR_SELF = "delete_for_self";
      String DELETE_FOR_EVERYONE = "delete_for_everyone";
    }

    interface ForwardChat {
      String SINGLE_CHAT_TEXT = "forward_single_chat_text";
      String SINGLE_CHAT_LOCATION = "forward_single_chat_location";
      String SINGLE_CHAT_CONTACT = "forward_single_chat_contact";
      String SINGLE_CHAT_GIPHY = "forward_single_chat_giphy";
      String SINGLE_CHAT_MEDIA = "forward_single_chat_media";

      String GROUP_CHAT_TEXT = "forward_group_chat_text";
      String GROUP_CHAT_LOCATION = "forward_group_chat_location";
      String GROUP_CHAT_CONTACT = "forward_group_chat_contact";
      String GROUP_CHAT_GIPHY = "forward_group_chat_giphy";
      String GROUP_CHAT_MEDIA = "forward_group_chat_media";
    }

    // Deprecated
    String SINGLE_CHAT_IMAGE = "single_chat_image";
    String SINGLE_CHAT_AUDIO = "single_chat_audio";
    String SINGLE_CHAT_VIDEO = "single_chat_video";
    String SINGLE_CHAT_LOCATION = "single_chat_location";
    String SINGLE_CHAT_CONTACT = "single_chat_contact";
    String SINGLE_CHAT_DOCUMENT = "single_chat_document";
    String SINGLE_CHAT_GIF = "single_chat_gif";
    String SINGLE_CHAT_GIPHY = "single_chat_gify";
    String SINGLE_CHAT_TEXT = "single_chat_text";
    String SINGLE_CHAT_MEDIA = "single_chat_media";

    String GROUP_CHAT_IMAGE = "group_chat_image";
    String GROUP_CHAT_AUDIO = "group_chat_audio";
    String GROUP_CHAT_VIDEO = "group_chat_video";
    String GROUP_CHAT_LOCATION = "group_chat_location";
    String GROUP_CHAT_CONTACT = "group_chat_contact";
    String GROUP_CHAT_DOCUMENT = "group_chat_document";
    String GROUP_CHAT_GIF = "group_chat_gif";
    String GROUP_CHAT_GIPHY = "group_chat_gify";
    String GROUP_CHAT_TEXT = "group_chat_text";
    String GROUP_CHAT_MEDIA = "group_chat_media";

    String E2E_CHAT_TEXT = "e2e_chat_text";
    String E2E_CHAT_LOCATION = "e2e_chat_location";
    String E2E_CHAT_CONTACT = "e2e_chat_contact";
    String E2E_CHAT_GIFY = "e2e_chat_gify";
    String E2E_CHAT_MEDIA = "e2e_chat_media";

    String DEVICE_TYPE = "android";
  }
  String BUG_SNAG = "a8b9435c5791f744df3be911c7953a41";

  interface Features {
    // Chat Module
    String CHAT = "Chat";
    String SEND_TEXT = "Send text";
    String SEND = "Send ";
    String SEND_LOCATION = "Location Sharing";
    String SEND_CONTACT = "Send contact";
    String SEND_DOCUMENT = "Send document";
    String SEND_GIF = "Send gif";
    String ADD_TO_FAVOURITE = "Add to favourite";
    String READ_RECEIPT = "Read receipt";
    String BLOCK_USER = "Block user";
    String DOWNLOAD_MEDIA = "Download media";
    String CHAT_MUTE_NOTIFICATIONS = "Mute chat notifications";
    String FORWARD_CHAT = "Forward chat";
    String EDIT_CHAT = "Edit chat";
    String DELETE_CHAT = "Delete chat";
    String CHAT_REACTIONS = "Chat reactions";
    String REPLY_THREAD = "Reply thread";
    String FOLLOW_THREAD = "Follow thread";
    String REPORT_MESSAGE = "Report message";
    String CLEAR_CHAT = "Clear chat";
    String USER_MENTIONS = "User/Channel mentions";
    String SEND_MEDIA = "Send media";
    String MEDIA = "Media";

    // Group Module
    String GROUP = "Channel";
    String CREATE_GROUP = "Create channel";
    String UPDATE_GROUP = "Update channel";
    String ADD_PARTICIPANTS = "Add participants to channel";
    String REMOVE_PARTICIPANTS = "Remove participants from channel";
    String SET_ADMIN = "Set as admin";
    String REMOVE_ADMIN = "Remove as admin";
    String EXIT_GROUP = "Exit from channel";
    String SEARCH_CHANNEL = "Search channel";

    // Tenant Module
    String NAMESPACE = "Namespace";
    String LOGIN = "Login";
    String CONNECT = "Connection";
    String CHANGE_PASSWORD = "Change password";
    String FORGOT_PASSWORD = "Forgot password";

    // Typing Module
    String TYPING = "Typing";

    //Delete Message
    String THIS_MESSAGE_WAS_DELETED="This message was deleted";
    String THIS_MESSAGE_WAS_DELETED_FOR_YOU="This message was deleted for you";
    String DELETE="delete";
    String EDIT="edit";
    String SELF = "self";
    String EVERYONE = "everyone";

    // User Module
    String USER_MODULE = "User module";
    String USER_PROFILE = "User profile";
    String AVAILABILITY_STATUS = "User availability status";
    String LOGOUT = "Logout";
    String MUTE_NOTIFICATIONS = "Mute notifications";
    String USER_PROFILE_IMAGE = "Update user profile image";

    //Search messages
    String LOCAL_SEARCH = "Local search";
    String GLOBAL_SEARCH = "Global search";
    String CHAT_RESTORE = "Chat Restore";

    // Announcement
    String ANNOUNCEMENT = "Announcement";
    // Copy Message
    String COPY = "Copy message";
    // Moderation
    String MODERATION = "Moderation";
    String DOMAIN_FILTER = "Domain filter";
    String PROFANITY_FILTER = "Profanity filter";
  }

  interface ErrorMessage {
    String ERROR_403 = "User account is deactivated. Contact admin for re-activation.";
  }

  String GLOBAL_NOTIFICATION_SETTINGS_CHANGED = "notificationSettingsChangedGlobal";
  String THREAD_NOTIFICATION_SETTINGS_CHANGED = "notificationSettingsChangedThread";
  String AVAILABILITY_STATUS_CHANGED = "availabilityStatusChanged";
  String USER_BLOCKED_STATUS_CHANGED = "userBlockedStatusChanged";

  int USER_ERROR = 403;
  String USER_NOT_EXIT = "This user is not exist";
}
