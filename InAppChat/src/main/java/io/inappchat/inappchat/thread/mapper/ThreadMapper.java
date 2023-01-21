package io.inappchat.inappchat.thread.mapper;

import io.inappchat.inappchat.core.type.SettingAppliedFor;
import io.inappchat.inappchat.cache.database.entity.Group;
import io.inappchat.inappchat.cache.database.entity.SingleChat;
import io.inappchat.inappchat.cache.database.entity.Thread;
import io.inappchat.inappchat.cache.database.entity.ThreadEmbedded;
import io.inappchat.inappchat.cache.database.entity.ThreadUserLink;
import io.inappchat.inappchat.cache.database.entity.User;
import io.inappchat.inappchat.remote.model.response.CreateThreadResponse;
import io.inappchat.inappchat.remote.model.response.GroupResponse;
import io.inappchat.inappchat.chat.mapper.ChatRecordMapper;
import io.inappchat.inappchat.chat.mapper.MessageRecord;
import io.inappchat.inappchat.core.type.ChatType;
import io.inappchat.inappchat.core.type.NotificationSettingsType;
import io.inappchat.inappchat.group.mapper.GroupRecord;
import io.inappchat.inappchat.user.mapper.UserRecord;
import io.inappchat.inappchat.mqtt.model.ChatTopicResponse;

import java.util.List;
import java.util.Objects;

/** Created by DK on 24/02/19. */
public class ThreadMapper {
  public static Thread from(
      CreateThreadResponse response,
      String senderChatId,
      String tenantId,
      User sender,
      User recipient,
      String recipientChatId,
      String muteSettings,
      long validTill,
      String validTillValue
  ) {
    return new Thread(
        response.getThreadId(),
        "",
        ChatType.SINGLE.getType(),
        response.getTenantId(),
        senderChatId,
        recipientChatId,
        0,
        0,
        0,
        response.getCreatedAt(),
        0,
        tenantId,
        sender.getId(),
        sender.getId(),
        recipient.getId(),
        response.getCreatedAt(),
        muteSettings,
        validTill,
        validTillValue
    );
  }

  public static Thread from(ChatTopicResponse response, String userChatId, String userAppId) {
    return new Thread(
        response.getThread().getThreadId(),
        "",
        response.getThread().getThreadType(),
        response.getTenantId(),
        userChatId,
        response.getSender().getERTCUserId(),
        0,
        0,
        response.getThread().getUnreadCount(),
        response.getCreatedAt(),
        0,
        response.getTenantId(),
        response.getSender().getAppUserId(),
        userAppId,
        response.getSender().getAppUserId(),
        response.getCreatedAt(),
        NotificationSettingsType.ALL.getMute(),
        0,
        SettingAppliedFor.ALWAYS.getDuration()
    );
  }

  public static ThreadUserLink from(String senderId, String recipientId, String threadId) {
    return new ThreadUserLink(senderId, recipientId, threadId);
  }

  public static void transform(
      ThreadEmbedded threadEmbedded,
      UserRecord recipient,
      final List<ThreadRecord> list,
      SingleChat singleChat
  ) {
    Thread thread = threadEmbedded.getThread();
    MessageRecord messageRecord = null;
    if (singleChat != null) {
      messageRecord = ChatRecordMapper.INSTANCE.transform(singleChat,
              null,null,null,null,null,null,null,false);
    }
    if(messageRecord != null){
      ThreadRecord threadRecord =
          new ThreadRecord(thread.getId(), thread.getName(), thread.getType(), thread.getMessageCount(), thread.getRead(),
              thread.getUnReadCount(), thread.getCreationDate(), thread.getHasDeleted(),
              Objects.requireNonNull(messageRecord), recipient, thread.getNotificationSettings());
      list.add(threadRecord);
    }
  }

  public static void transform(
      ThreadEmbedded threadEmbedded,
      GroupRecord recipient,
      final List<ThreadRecord> list,
      SingleChat singleChat
  ) {
    Thread thread = threadEmbedded.getThread();
    MessageRecord messageRecord = null;
    if (singleChat != null) {
      messageRecord = ChatRecordMapper.INSTANCE.transform(singleChat,
              null,null,null,null,null,null,null,false);
    }
    if(messageRecord != null){
      ThreadRecord threadRecord =
          new ThreadRecord(thread.getId(), thread.getName(), thread.getType(),
              thread.getMessageCount(), thread.getRead(), thread.getUnReadCount(),
              thread.getCreationDate(), thread.getHasDeleted(), Objects.requireNonNull(messageRecord),
              null, recipient, thread.getNotificationSettings());
      list.add(threadRecord);
    }
  }

  public static Thread fromGroupResponse(
      GroupResponse response, String userAppId, String userChatId) {
    return new Thread(
        response.getThreadId(),
        response.getName(),
        ChatType.GROUP.getType(),
        response.getTenantId(),
        userChatId,
        response.getGroupId(),
        0,
        0,
        0,
        response.getCreatedAt(),
        0,
        response.getTenantId(),
        "",
        userAppId,
        response.getGroupId(),
        response.getCreatedAt(),
        NotificationSettingsType.ALL.getMute(),
        0,
        SettingAppliedFor.ALWAYS.getDuration()
    );
  }

  public static Thread fromGroup(
      Group group, String userAppId, String userChatId) {
    return new Thread(
        group.getThreadId(),
        group.getName(),
        ChatType.GROUP.getType(),
        group.getTenantId(),
        userChatId,
        group.getGroupId(),
        0,
        0,
        0,
        group.getLoginTimestamp(),
        0,
        group.getTenantId(),
        "",
        userAppId,
        group.getGroupId(),
        group.getLoginTimestamp(),
        NotificationSettingsType.ALL.getMute(),
        0,
        SettingAppliedFor.ALWAYS.getDuration()
    );
  }

  public static ThreadRecord transform(Thread thread, UserRecord recipient, GroupRecord groupRecipient) {
    return new ThreadRecord(
        thread.getId(),
        thread.getName(),
        thread.getType(),
        thread.getMessageCount(),
        thread.getRead(),
        thread.getUnReadCount(),
        thread.getCreationDate(),
        thread.getHasDeleted(),
        new MessageRecord(""),
        recipient,
        groupRecipient,
        thread.getNotificationSettings());
  }

  public static Thread from(ChatTopicResponse response, String userChatId, String userAppId, String recipientUserChatId, String recipientUserAppId) {
    return new Thread(
        response.getThread().getThreadId(),
        "",
        response.getThread().getThreadType(),
        response.getTenantId(),
        userChatId,
        recipientUserChatId,
        0,
        0,
        response.getThread().getUnreadCount(),
        response.getCreatedAt(),
        0,
        response.getTenantId(),
        response.getSender().getAppUserId(),
        userAppId,
        recipientUserAppId,
        response.getCreatedAt(),
        NotificationSettingsType.ALL.getMute(),
        0,
        SettingAppliedFor.ALWAYS.getDuration()
    );
  }

  public static Thread from(
      String threadId,
      String senderChatId,
      String tenantId,
      String senderId,
      String recipientId,
      String recipientChatId,
      String muteSettings,
      long validTill,
      String validTillValue,
      Long createdAt
  ) {
    return new Thread(
        threadId,
        "",
        ChatType.SINGLE.getType(),
        tenantId,
        senderChatId,
        recipientChatId,
        0,
        0,
        0,
        createdAt,
        0,
        tenantId,
        senderId,
        senderId,
        recipientId,
        createdAt,
        muteSettings,
        validTill,
        validTillValue
    );
  }

  public static Thread from(
      Thread thread,
      User user
  ) {
    return new Thread(
        thread.getId(),
        thread.getName(),
        thread.getType(),
        thread.getTenantId(),
        user.getUserChatId(),
        (thread.getType().equals("single")) ? thread.getSenderChatId() : thread.getRecipientChatId(),
        0,
        0,
        0,
        thread.getCreationDate(),
        0,
        thread.getTenantId(),
        user.getId(),
        user.getId(),
        (thread.getType().equals("single")) ? thread.getSenderUserId() : thread.getRecipientUserId(),
        thread.getUpdatedAt(),
        thread.getNotificationSettings(),
        thread.getValidTill(),
        thread.getValidTillValue()
    );
  }
}
