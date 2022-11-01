package com.ripbull.coresdk.core.type;

import com.ripbull.coresdk.announcement.mapper.AnnouncementRecord;
import com.ripbull.coresdk.chat.mapper.ChatMetaDataRecord;
import com.ripbull.coresdk.chat.mapper.MessageMetaDataRecord;
import com.ripbull.coresdk.chat.mapper.MessageRecord;
import com.ripbull.coresdk.data.common.Result;
import com.ripbull.coresdk.group.mapper.GroupRecord;
import com.ripbull.coresdk.typing.mapper.TypingIndicatorRecord;
import com.ripbull.coresdk.user.mapper.UserMetaDataRecord;
import com.ripbull.coresdk.user.mapper.UserRecord;
import com.ripbull.ertc.mqtt.listener.ReceivedMessage;
import java.util.ArrayList;
import io.reactivex.functions.Predicate;

/** Created by DK on 09/03/19. */
public class NetworkEvent {

  private final EventType type;
  private MessageMetaDataRecord messageMetaDataRecord;
  private ReceivedMessage receivedMessage;
  private MessageRecord messageRecord;
  private TypingIndicatorRecord typingIndicatorRecord;
  private GroupRecord groupRecord;
  private ChatMetaDataRecord chatMetaDataRecord;
  private UserMetaDataRecord userMetaDataRecord;
  private String text;
  private ArrayList<MessageRecord> messageRecordList;
  private UserRecord userRecord;
  private AnnouncementRecord announcementRecord;
  private Result result;

  public NetworkEvent(EventType type) {
    this.type = type;
  }

  public NetworkEvent(EventType type, MessageRecord messageRecord) {
    this.type = type;
    this.messageRecord = messageRecord;
  }

  public NetworkEvent(EventType type, TypingIndicatorRecord typingIndicatorRecord) {
    this.type = type;
    this.typingIndicatorRecord = typingIndicatorRecord;
  }

  public NetworkEvent(EventType type, GroupRecord groupRecord) {
    this.type = type;
    this.groupRecord = groupRecord;
  }

  public NetworkEvent(EventType type, ChatMetaDataRecord chatMetaDataRecord) {
    this.type = type;
    this.chatMetaDataRecord = chatMetaDataRecord;
  }

  public NetworkEvent(EventType type, UserMetaDataRecord userMetaDataRecord) {
    this.type = type;
    this.userMetaDataRecord = userMetaDataRecord;
  }

  public NetworkEvent(EventType type, MessageMetaDataRecord messageMetaDataRecord) {
    this.type = type;
    this.messageMetaDataRecord = messageMetaDataRecord;
  }

  public NetworkEvent(EventType type, ArrayList<MessageRecord> messageRecordList) {
    this.type = type;
    this.messageRecordList = messageRecordList;
  }

  public NetworkEvent(EventType type, UserRecord userRecord) {
    this.type = type;
    this.userRecord = userRecord;
  }

  public NetworkEvent(EventType type, AnnouncementRecord announcementRecord) {
    this.type = type;
    this.announcementRecord = announcementRecord;
  }

  public NetworkEvent(EventType type, Result result) {
    this.type = type;
    this.result = result;
  }

  public EventType getType() {
    return type;
  }

  public ReceivedMessage message() {
    return receivedMessage;
  }

  public MessageRecord messageRecord() {
    return this.messageRecord;
  }

  public GroupRecord groupRecord() {
    return this.groupRecord;
  }

  public ChatMetaDataRecord chatMetaDataRecord() {
    return this.chatMetaDataRecord;
  }

  public UserMetaDataRecord userMetaDataRecord() {
    return this.userMetaDataRecord;
  }
  public MessageMetaDataRecord messageMetaData() {
    return this.messageMetaDataRecord;
  }

  public ArrayList<MessageRecord> deleteMessageData() {
    return this.messageRecordList;
  }

  public String getText() {
    return text;
  }

  public UserRecord userRecord() {
    return this.userRecord;
  }

  public AnnouncementRecord AnnouncementRecord() {
    return this.announcementRecord;
  }

  public Result Result() {
    return this.result;
  }

  public NetworkEvent(EventType type, ReceivedMessage receivedMessage) {
    this.type = type;
    this.receivedMessage = receivedMessage;
  }

  public static NetworkEvent threadAdded(ReceivedMessage receivedMessage) {
    return new NetworkEvent(EventType.THREAD_ADDED, receivedMessage);
  }

  public static NetworkEvent threadRemoved(ReceivedMessage receivedMessage) {
    return new NetworkEvent(EventType.THREAD_REMOVED, receivedMessage);
  }

  public static NetworkEvent threadDetailsUpdated(ReceivedMessage receivedMessage) {
    return new NetworkEvent(EventType.THREAD_DETAILS_UPDATED, receivedMessage);
  }

  public static NetworkEvent threadLastMessageUpdated(ReceivedMessage receivedMessage) {
    return new NetworkEvent(EventType.THREAD_LAST_MESSAGE_UPDATED, receivedMessage);
  }

  public static NetworkEvent threadMetaUpdated(ChatMetaDataRecord chatMetaDataRecord) {
    return new NetworkEvent(EventType.THREAD_META_DATA_UPDATED, chatMetaDataRecord);
  }

  public static NetworkEvent messageAdded(MessageRecord messageRecord) {
    return new NetworkEvent(EventType.MESSAGE_ADDED, messageRecord);
  }

  public static NetworkEvent messageUpdated(MessageRecord messageRecord) {
    return new NetworkEvent(EventType.MESSAGE_UPDATED, messageRecord);
  }
  public static NetworkEvent messageMediaDownload(MessageRecord messageRecord) {
    return new NetworkEvent(EventType.MESSAGE_MEDIA_DOWNLOAD, messageRecord);
  }

  public static NetworkEvent messageRemoved(MessageRecord receivedMessage) {
    return new NetworkEvent(EventType.MESSAGE_REMOVED, receivedMessage);
  }

  public static NetworkEvent threadUsersChanged(ReceivedMessage receivedMessage) {
    return new NetworkEvent(EventType.THREAD_USERS_CHANGED, receivedMessage);
  }

  public static NetworkEvent userMetaDataUpdated(ReceivedMessage message) {
    return new NetworkEvent(EventType.USER_META_DATA_UPDATED, message);
  }

  public static NetworkEvent messageMetaData(MessageMetaDataRecord message) {
    return new NetworkEvent(EventType.MESSAGE_META_DATA, message);
  }


  public static NetworkEvent userPresenceUpdated(ReceivedMessage message) {
    return new NetworkEvent(EventType.USER_PRESENCE_UPDATED, message);
  }

  public static NetworkEvent contactAdded(ReceivedMessage message) {
    return new NetworkEvent(EventType.CONTACTS_ADDED, message);
  }

  public static NetworkEvent contactDeleted(ReceivedMessage message) {
    return new NetworkEvent(EventType.CONTACTS_REMOVED, message);
  }

  public static NetworkEvent contactChanged(ReceivedMessage message) {
    return new NetworkEvent(EventType.CONTACTS_CHANGED, message);
  }

  public static NetworkEvent contactsUpdated() {
    return new NetworkEvent(EventType.CONTACTS_UPDATE);
  }

  public static NetworkEvent threadRead(ReceivedMessage receivedMessage) {
    return new NetworkEvent(EventType.THREAD_READ, receivedMessage);
  }

  public static NetworkEvent typingStateChanged(TypingIndicatorRecord typingIndicatorRecord) {
    return new NetworkEvent(EventType.TYPING_STATE_CHANGED, typingIndicatorRecord);
  }

  public static NetworkEvent typingStateChanged(ReceivedMessage receivedMessage) {
    return new NetworkEvent(EventType.TYPING_STATE_CHANGED, receivedMessage);
  }

  public static NetworkEvent groupUpdated(GroupRecord receivedMessage) {
    return new NetworkEvent(EventType.GROUP_UPDATED, receivedMessage);
  }

  public static NetworkEvent userMetaDataUpdated(UserMetaDataRecord userMetaDataRecord) {
    return new NetworkEvent(EventType.USER_META_DATA_UPDATED, userMetaDataRecord);
  }

  public static NetworkEvent userMetaDataUpdated(UserRecord userRecord) {
    return new NetworkEvent(EventType.USER_META_DATA_UPDATED, userRecord);
  }

  public static NetworkEvent announcement(AnnouncementRecord announcementRecord) {
    return new NetworkEvent(EventType.ANNOUNCEMENT_RECEIVED, announcementRecord);
  }

  public static NetworkEvent logout(Result result) {
    return new NetworkEvent(EventType.LOGOUT, result);
  }

  public static NetworkEvent tenantConfigUpdated(Result result) {
    return new NetworkEvent(EventType.CONFIG_CHANGED, result);
  }

  public static NetworkEvent userDbUpdated(UserRecord userRecord) {
    return new NetworkEvent(EventType.USER_DB_UPDATED, userRecord);
  }

  public static NetworkEvent chatCleared(ReceivedMessage receivedMessage) {
    return new NetworkEvent(EventType.CHAT_CLEARED, receivedMessage);
  }

  public static NetworkEvent fetchChatSettings(Result result) {
    return new NetworkEvent(EventType.CHAT_SETTINGS_UPDATED, result);
  }

  public static Predicate<NetworkEvent> filterType(final EventType type) {
    return networkEvent -> networkEvent.type == type;
  }

  public static Predicate<NetworkEvent> filterType(final EventType... types) {
    return networkEvent -> {
      for (EventType type : types) {
        if (networkEvent.type == type) return true;
      }
      return false;
    };
  }

  public static Predicate<NetworkEvent> threadsUpdated() {
    return filterType(
        EventType.THREAD_DETAILS_UPDATED,
        EventType.THREAD_ADDED,
        EventType.THREAD_REMOVED,
        EventType.THREAD_LAST_MESSAGE_UPDATED,
        EventType.THREAD_USERS_CHANGED,
        EventType.MESSAGE_REMOVED,
        EventType.USER_META_DATA_UPDATED);
  }

  public static Predicate<NetworkEvent> filterContactsChanged() {
    return filterType(
        EventType.CONTACTS_CHANGED,
        EventType.CONTACTS_ADDED,
        EventType.CONTACTS_REMOVED,
        EventType.CONTACTS_UPDATE);
  }

  public static Predicate<NetworkEvent> threadUsersUpdated() {
    return filterType(EventType.THREAD_USERS_CHANGED, EventType.USER_PRESENCE_UPDATED);
  }
}
