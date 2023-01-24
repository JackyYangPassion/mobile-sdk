package io.inappchat.inappchat.group.repository;

import android.content.Context;
import androidx.annotation.NonNull;
import io.inappchat.inappchat.R;
import io.inappchat.inappchat.core.ChatSDKException;
import io.inappchat.inappchat.data.common.Result;
import io.inappchat.inappchat.InAppChat;
import io.inappchat.inappchat.group.GroupModule;
import io.inappchat.inappchat.group.mapper.GroupRecord;
import io.inappchat.inappchat.utils.Constants;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public class GroupModuleStub implements GroupModule {

  private Context appContext;

  public static GroupModule newInstance() {
    return new GroupModuleStub(InAppChat.getAppContext());
  }

  private GroupModuleStub(Context context) {
    this.appContext = context;
  }

  @Override
  public Flowable<List<GroupRecord>> getGroups() {
    return Flowable.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.GROUP)));
  }

  @Override
  public Flowable<GroupRecord> getGroupById(@NonNull String id) {
    return Flowable.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.GROUP)));
  }

  @Override
  public Single<GroupRecord> createPrivateGroup(GroupRecord groupRecord) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.CREATE_GROUP)));
  }

  @Override
  public Single<GroupRecord> updateGroupDetail(
      String groupId, String groupName, String groupDesc, String groupImgPath) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.UPDATE_GROUP)));
  }

  @Override
  public Single<GroupRecord> addParticipants(String groupId, List<String> users) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.ADD_PARTICIPANTS)));
  }

  @Override
  public Single<GroupRecord> removeParticipants(String groupId, List<String> users) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.REMOVE_PARTICIPANTS)));
  }

  @Override
  public Single<GroupRecord> addAdmin(String groupId, String user) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.SET_ADMIN)));
  }

  @Override
  public Single<GroupRecord> removeAdmin(String groupId, String user) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.REMOVE_ADMIN)));
  }

  @Override
  public Single<GroupRecord> exitGroup(String groupId) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.EXIT_GROUP)));
  }

  @Override
  public Single<Result> removeGroupPic(String groupId) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.UPDATE_GROUP)));
  }

  @Override
  public Observable<GroupRecord> subscribeToGroupUpdate() {
    return Observable.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.UPDATE_GROUP)));
  }

  @Override
  public Single<GroupRecord> createPublicGroup(GroupRecord groupRecord) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.CREATE_GROUP)));
  }

  @Override
  public Single<List<GroupRecord>> getSearchedChannels(String keyword, String channelType,
      String joined) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.SEARCH_CHANNEL)));
  }

  @Override
  public Flowable<List<GroupRecord>> getActiveGroups() {
    return Flowable.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.GROUP)));
  }
}