package com.ripbull.coresdk.module;

import com.ripbull.coresdk.announcement.repository.AnnouncementModuleHook;
import com.ripbull.coresdk.chat.repository.ChatModuleHook;
import com.ripbull.coresdk.core.event.EventHandler;
import com.ripbull.coresdk.fcm.FcmModule;
import com.ripbull.coresdk.group.GroupModule;
import com.ripbull.coresdk.notification.repository.NotificationModuleHook;
import com.ripbull.coresdk.tenant.AuthenticationModule;
import com.ripbull.coresdk.typing.TypingModule;
import com.ripbull.coresdk.user.UserModule;

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
