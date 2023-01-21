package io.inappchat.inappchat.module;

import io.inappchat.inappchat.announcement.repository.AnnouncementModuleHook;
import io.inappchat.inappchat.chat.repository.ChatModuleHook;
import io.inappchat.inappchat.core.event.EventHandler;
import io.inappchat.inappchat.fcm.FcmModule;
import io.inappchat.inappchat.group.GroupModule;
import io.inappchat.inappchat.notification.repository.NotificationModuleHook;
import io.inappchat.inappchat.tenant.AuthenticationModule;
import io.inappchat.inappchat.typing.TypingModule;
import io.inappchat.inappchat.user.UserModule;

/** Created by DK on 05/12/18. */
public interface ModuleAdapter {

  AuthenticationModule tenant();

  ChatModuleHook chat();

  UserModule user();

  CoreModule core();

  TypingModule typing();

  GroupModule group();

  FcmModule fcm();

  EventHandler event();

  NotificationModuleHook notification();

  AnnouncementModuleHook announcement();
}
