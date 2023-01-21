package io.inappchat.inappchat.group.repository;

import androidx.annotation.NonNull;
import io.inappchat.inappchat.cache.database.dao.TenantDao;
import io.inappchat.inappchat.core.event.EventHandler;
import io.inappchat.inappchat.data.DataManager;
import io.inappchat.inappchat.data.common.Result;
import io.inappchat.inappchat.group.GroupModule;
import io.inappchat.inappchat.group.GroupModuleImpl;
import io.inappchat.inappchat.group.mapper.GroupRecord;
import io.inappchat.inappchat.utils.Constants;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.functions.Function;
import java.util.List;
import org.reactivestreams.Publisher;

public class GroupModuleHookImpl implements GroupModuleHook, GroupModule {

  private final GroupModule groupModule;
  private final GroupModule stub;
  private DataManager dataManager;

  public static GroupModuleHook newInstance(DataManager dataManager, EventHandler eventHandler) {
    GroupModule userModule = GroupModuleImpl.newInstance(dataManager, eventHandler);
    GroupModule stub = GroupModuleStub.newInstance();
    return new GroupModuleHookImpl(userModule, stub, dataManager);
  }

  private GroupModuleHookImpl(@NonNull GroupModule groupModule, @NonNull GroupModule stub,
      DataManager dataManager) {
    this.groupModule = groupModule;
    this.stub = stub;
    this.dataManager = dataManager;
  }

  private TenantDao getTenantDao() {
    return dataManager.db().tenantDao();
  }

  private String getTenantId() {
    return dataManager.preference().getTenantId();
  }

  private Single<Boolean> isFeatureEnabled(String feature) {
    return getTenantDao().getTenantConfigValue(getTenantId(), feature)
        .flatMap((Function<String, SingleSource<Boolean>>) s -> Single.just(s.equals("1")));
  }

  private Single<Boolean> isSubscriptionAvailable() {
    return isFeatureEnabled(Constants.TenantConfig.GROUP_ENABLED);
  }

  @Override
  public GroupModule provideModule() {
    return this;
  }

  @Override
  public Flowable<List<GroupRecord>> getGroups() {
    return isSubscriptionAvailable().flatMapPublisher(
        (Function<Boolean, Publisher<List<GroupRecord>>>) aBoolean -> aBoolean
            ? groupModule.getGroups() : stub.getGroups());
  }

  @Override
  public Flowable<GroupRecord> getGroupById(@NonNull String id) {
    return isSubscriptionAvailable().flatMapPublisher(
        (Function<Boolean, Publisher<GroupRecord>>) aBoolean -> aBoolean ? groupModule.getGroupById(
            id) : stub.getGroupById(id));
  }

  @Override
  public Single<GroupRecord> createPrivateGroup(GroupRecord groupRecord) {
    return isSubscriptionAvailable().flatMap(
        (Function<Boolean, SingleSource<GroupRecord>>) aBoolean -> aBoolean
            ? groupModule.createPrivateGroup(groupRecord) : stub.createPrivateGroup(groupRecord));
  }

  @Override
  public Single<GroupRecord> updateGroupDetail(String groupId, String groupName, String groupDesc,
      String groupImgPath) {
    return isSubscriptionAvailable().flatMap(
        (Function<Boolean, SingleSource<GroupRecord>>) aBoolean -> aBoolean
            ? groupModule.updateGroupDetail(groupId, groupName, groupDesc, groupImgPath)
            : stub.updateGroupDetail(groupId, groupName, groupDesc, groupImgPath));
  }

  @Override
  public Single<GroupRecord> addParticipants(String groupId, List<String> users) {
    return isSubscriptionAvailable().flatMap(
        (Function<Boolean, SingleSource<GroupRecord>>) aBoolean -> aBoolean
            ? groupModule.addParticipants(groupId, users) : stub.addParticipants(groupId, users));
  }

  @Override
  public Single<GroupRecord> removeParticipants(String groupId, List<String> users) {
    return isSubscriptionAvailable().flatMap(
        (Function<Boolean, SingleSource<GroupRecord>>) aBoolean -> aBoolean
            ? groupModule.removeParticipants(groupId, users)
            : stub.removeParticipants(groupId, users));
  }

  @Override
  public Single<GroupRecord> addAdmin(String groupId, String user) {
    return isSubscriptionAvailable().flatMap(
        (Function<Boolean, SingleSource<GroupRecord>>) aBoolean -> aBoolean ? groupModule.addAdmin(
            groupId, user) : stub.addAdmin(groupId, user));
  }

  @Override
  public Single<GroupRecord> removeAdmin(String groupId, String user) {
    return isSubscriptionAvailable().flatMap(
        (Function<Boolean, SingleSource<GroupRecord>>) aBoolean -> aBoolean
            ? groupModule.removeAdmin(groupId, user) : stub.removeAdmin(groupId, user));
  }

  @Override
  public Single<GroupRecord> exitGroup(String groupId) {
    return isSubscriptionAvailable().flatMap(
        (Function<Boolean, SingleSource<GroupRecord>>) aBoolean -> aBoolean ? groupModule.exitGroup(
            groupId) : stub.exitGroup(groupId));
  }

  @Override
  public Single<Result> removeGroupPic(String groupId) {
    return isSubscriptionAvailable().flatMap(
        (Function<Boolean, SingleSource<Result>>) aBoolean -> aBoolean ? groupModule.removeGroupPic(
            groupId) : stub.removeGroupPic(groupId));
  }

  @Override
  public Observable<GroupRecord> subscribeToGroupUpdate() {
    return groupModule.subscribeToGroupUpdate();
  }

  @Override
  public Single<GroupRecord> createPublicGroup(GroupRecord groupRecord) {
    return isSubscriptionAvailable().flatMap(
        (Function<Boolean, SingleSource<GroupRecord>>) aBoolean -> aBoolean
            ? groupModule.createPublicGroup(groupRecord) : stub.createPublicGroup(groupRecord));
  }

  @Override
  public Single<List<GroupRecord>> getSearchedChannels(String keyword, String channelType, String joined) {
    return isFeatureEnabled(Constants.TenantConfig.CHANNEL_SEARCH).flatMap(
        (Function<Boolean, SingleSource<List<GroupRecord>>>) aBoolean -> aBoolean
            ? groupModule.getSearchedChannels(keyword,  channelType, joined)
            : stub.getSearchedChannels(keyword,  channelType, joined));
  }

  @Override
  public Flowable<List<GroupRecord>> getActiveGroups() {
    return isSubscriptionAvailable().flatMapPublisher(
        (Function<Boolean, Publisher<List<GroupRecord>>>) aBoolean -> aBoolean
            ? groupModule.getActiveGroups() : stub.getActiveGroups());
  }
}
