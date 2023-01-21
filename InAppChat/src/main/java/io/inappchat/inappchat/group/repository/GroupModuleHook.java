package io.inappchat.inappchat.group.repository;

import io.inappchat.inappchat.group.GroupModule;

public interface GroupModuleHook {
  GroupModule provideModule();
}
