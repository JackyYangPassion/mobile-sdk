package io.inappchat.inappchat.module;

import io.inappchat.inappchat.announcement.repository.AnnouncementModuleHook;
import io.inappchat.inappchat.announcement.repository.AnnouncementModuleHookImpl;
import io.inappchat.inappchat.chat.repository.ChatModuleHook;
import io.inappchat.inappchat.chat.repository.ChatModuleHookImpl;
import io.inappchat.inappchat.core.event.EventHandler;
import io.inappchat.inappchat.core.event.EventHandlerImpl;
import io.inappchat.inappchat.data.DataManager;
import io.inappchat.inappchat.fcm.FcmModule;
import io.inappchat.inappchat.fcm.FcmModuleImpl;
import io.inappchat.inappchat.group.GroupModule;
import io.inappchat.inappchat.group.repository.GroupModuleHook;
import io.inappchat.inappchat.group.repository.GroupModuleHookImpl;
import io.inappchat.inappchat.notification.repository.NotificationModuleHook;
import io.inappchat.inappchat.notification.repository.NotificationModuleHookImpl;
import io.inappchat.inappchat.tenant.AuthenticationModule;
import io.inappchat.inappchat.tenant.AuthenticationModuleImpl;
import io.inappchat.inappchat.typing.TypingModule;
import io.inappchat.inappchat.typing.TypingModuleHook;
import io.inappchat.inappchat.typing.TypingModuleHookImpl;
import io.inappchat.inappchat.user.UserModule;
import io.inappchat.inappchat.user.repository.UserModuleHook;
import io.inappchat.inappchat.user.repository.UserModuleHookImpl;

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
