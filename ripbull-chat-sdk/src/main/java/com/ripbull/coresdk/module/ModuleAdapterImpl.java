package com.ripbull.coresdk.module;

import com.ripbull.coresdk.announcement.repository.AnnouncementModuleHook;
import com.ripbull.coresdk.announcement.repository.AnnouncementModuleHookImpl;
import com.ripbull.coresdk.chat.repository.ChatModuleHook;
import com.ripbull.coresdk.chat.repository.ChatModuleHookImpl;
import com.ripbull.coresdk.core.event.EventHandler;
import com.ripbull.coresdk.core.event.EventHandlerImpl;
import com.ripbull.coresdk.data.DataManager;
import com.ripbull.coresdk.fcm.FcmModule;
import com.ripbull.coresdk.fcm.FcmModuleImpl;
import com.ripbull.coresdk.group.GroupModule;
import com.ripbull.coresdk.group.repository.GroupModuleHook;
import com.ripbull.coresdk.group.repository.GroupModuleHookImpl;
import com.ripbull.coresdk.notification.repository.NotificationModuleHook;
import com.ripbull.coresdk.notification.repository.NotificationModuleHookImpl;
import com.ripbull.coresdk.tenant.AuthenticationModule;
import com.ripbull.coresdk.tenant.AuthenticationModuleImpl;
import com.ripbull.coresdk.typing.TypingModule;
import com.ripbull.coresdk.typing.TypingModuleHook;
import com.ripbull.coresdk.typing.TypingModuleHookImpl;
import com.ripbull.coresdk.user.UserModule;
import com.ripbull.coresdk.user.repository.UserModuleHook;
import com.ripbull.coresdk.user.repository.UserModuleHookImpl;

/** Created by DK on 05/12/18. */
public class ModuleAdapterImpl implements ModuleAdapter {

  private final AuthenticationModule authenticationModule;
  private CoreModule coreModule;
  private final ChatModuleHook chatModule;
  private final UserModuleHook userModule;
  private final GroupModuleHook groupModule;

  private final TypingModuleHook typingModuleHook;
  private final FcmModule fcmModule;
  private EventHandler eventHandler;
  private NotificationModuleHook notificationModule;
  private AnnouncementModuleHook announcementModule;

  public static ModuleAdapter newInstance(DataManager dataManager) {

    EventHandler eventHandler = EventHandlerImpl.newInstance(dataManager);

    AuthenticationModule authModule = AuthenticationModuleImpl.newInstance(dataManager);
    UserModuleHook userModule = UserModuleHookImpl.newInstance(dataManager, eventHandler);
    ChatModuleHook chatModule = ChatModuleHookImpl.newInstance(dataManager, eventHandler);
    CoreModule coreModule = CoreModuleImpl.newInstance(dataManager);
    TypingModuleHook typingModule = TypingModuleHookImpl.newInstance(dataManager);
    GroupModuleHook groupModule = GroupModuleHookImpl.newInstance(dataManager, eventHandler);
    FcmModule fcmModule = FcmModuleImpl.newInstance(eventHandler,dataManager);
    NotificationModuleHook notificationModule = NotificationModuleHookImpl.newInstance(dataManager);
    AnnouncementModuleHook announcementModule = AnnouncementModuleHookImpl.newInstance(dataManager, eventHandler);

    return new ModuleAdapterImpl(authModule, chatModule, userModule, coreModule,
        typingModule,groupModule, fcmModule,eventHandler, notificationModule, announcementModule);
  }

  private ModuleAdapterImpl(AuthenticationModule authenticationModule, ChatModuleHook chatModule,
                            UserModuleHook userModule, CoreModule coreModule, TypingModuleHook typingModuleHook,
                            GroupModuleHook groupModule, FcmModule fcmModule, EventHandler eventHandler,
                            NotificationModuleHook notificationModule, AnnouncementModuleHook announcementModule) {
    this.chatModule = chatModule;
    this.userModule = userModule;
    this.authenticationModule = authenticationModule;
    this.coreModule = coreModule;
    this.typingModuleHook = typingModuleHook;
    this.groupModule = groupModule;
    this.fcmModule = fcmModule;
    this.eventHandler = eventHandler;
    this.notificationModule = notificationModule;
    this.announcementModule = announcementModule;
  }

  @Override
  public AuthenticationModule tenant() {
    return this.authenticationModule;
  }

  @Override
  public ChatModuleHook chat() {
    return this.chatModule.provideModule();
  }

  @Override
  public UserModule user() {
    return this.userModule.provideModule();
  }

  @Override
  public CoreModule core() {
    return this.coreModule;
  }

  @Override
  public TypingModule typing() {
    return this.typingModuleHook.provideModule();
  }

  @Override
  public GroupModule group() {
      return this.groupModule.provideModule();
  }

  @Override
  public FcmModule fcm() {
    return this.fcmModule;
  }

  @Override
  public EventHandler event() {
    return this.eventHandler;
  }

  @Override
  public NotificationModuleHook notification() {
    return this.notificationModule;
  }

  @Override
  public AnnouncementModuleHook announcement() {
    return this.announcementModule;
  }
}
