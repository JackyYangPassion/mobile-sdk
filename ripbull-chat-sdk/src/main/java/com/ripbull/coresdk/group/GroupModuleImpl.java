package com.ripbull.coresdk.group;

import androidx.annotation.NonNull;

import com.ripbull.coresdk.core.event.EventHandler;
import com.ripbull.coresdk.data.DataManager;
import com.ripbull.coresdk.data.common.Result;
import com.ripbull.coresdk.group.mapper.GroupRecord;
import com.ripbull.coresdk.group.repository.GroupRepository;
import com.ripbull.coresdk.group.repository.GroupRepositoryImpl;
import com.ripbull.coresdk.module.BaseModule;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;

public class GroupModuleImpl extends BaseModule implements GroupModule {

  public static GroupModule newInstance(DataManager dataManager, EventHandler eventHandler) {
    return new GroupModuleImpl(dataManager, eventHandler);
  }

  private final GroupRepository groupRepository;

  private GroupModuleImpl(DataManager dataManager, EventHandler eventHandler) {
    super(dataManager);
    groupRepository = GroupRepositoryImpl.newInstance(dataManager, eventHandler);
  }

  @Override
  public Flowable<List<GroupRecord>> getGroups() {
    return groupRepository.getGroups(getTenantId());
  }

  @Override
  public Flowable<GroupRecord> getGroupById(@NonNull String id) {
    return groupRepository.getGroupById(id);
  }

  @Override
  public Single<GroupRecord> createPrivateGroup(GroupRecord groupRecord) {
    return groupRepository.createPrivateGroup(groupRecord);
  }

  @Override
  public Single<GroupRecord> updateGroupDetail(
      String groupId, String groupName, String groupDesc, String groupImgPath) {
    return groupRepository.updateGroupDetail(groupId, groupName, groupDesc, groupImgPath);
  }

  @Override
  public Single<GroupRecord> addParticipants(String groupId, List<String> users) {
    return groupRepository.addParticipants(groupId, users);
  }

  @Override
  public Single<GroupRecord> removeParticipants(String groupId, List<String> users) {
    return groupRepository.removeParticipants(groupId, users);
  }

  @Override
  public Single<GroupRecord> addAdmin(String groupId, String user) {
    return groupRepository.addAdmin(groupId, user);
  }

  @Override
  public Single<GroupRecord> removeAdmin(String groupId, String user) {
    return groupRepository.removeAdmin(groupId, user);
  }

  @Override
  public Single<GroupRecord> exitGroup(String groupId) {
    return groupRepository.exitGroup(groupId);
  }

  @Override
  public Single<Result> removeGroupPic(String groupId) {
    return groupRepository.removeGroupPic(groupId);
  }

  @Override
  public Observable<GroupRecord> subscribeToGroupUpdate() {
    return groupRepository.subscribeToGroupUpdate();
  }

  @Override
  public Single<GroupRecord> createPublicGroup(GroupRecord groupRecord) {
    return groupRepository.createPublicGroup(groupRecord);
  }

  @Override
  public Single<List<GroupRecord>> getSearchedChannels(String keyword, String channelType, String joined) {
    return groupRepository.getSearchedChannels(keyword, channelType, joined);
  }

  @Override
  public Flowable<List<GroupRecord>> getActiveGroups() {
    return groupRepository.getActiveGroups(getTenantId());
  }
}
