package com.ripbull.coresdk.group.repository;

import android.content.Context;
import androidx.annotation.NonNull;
import com.ripbull.coresdk.R;
import com.ripbull.coresdk.core.ChatSDKException;
import com.ripbull.coresdk.data.common.Result;
import com.ripbull.coresdk.eRTCSDK;
import com.ripbull.coresdk.group.GroupModule;
import com.ripbull.coresdk.group.mapper.GroupRecord;
import com.ripbull.coresdk.utils.Constants;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;

public class GroupModuleStub implements GroupModule {

  private Context appContext;

  public static GroupModule newInstance() {
    return new GroupModuleStub(eRTCSDK.getAppContext());
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