package com.ripbull.coresdk.group;

import androidx.annotation.NonNull;
import com.ripbull.coresdk.data.common.Result;
import com.ripbull.coresdk.group.mapper.GroupRecord;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;

public interface GroupModule {

  Flowable<List<GroupRecord>> getGroups();

  Flowable<GroupRecord> getGroupById(@NonNull String id);

  Single<GroupRecord> createPrivateGroup(GroupRecord groupRecord);

  Single<GroupRecord> updateGroupDetail(
      String groupId, String groupName, String groupDesc, String groupImgPath);

  Single<GroupRecord> addParticipants(String groupId, List<String> users);

  Single<GroupRecord> removeParticipants(String groupId, List<String> users);

  Single<GroupRecord> addAdmin(String groupId, String user);

  Single<GroupRecord> removeAdmin(String groupId, String user);

  Single<GroupRecord> exitGroup(String groupId);

  Single<Result> removeGroupPic(String groupId);

  Observable<GroupRecord> subscribeToGroupUpdate();

  Single<GroupRecord> createPublicGroup(GroupRecord groupRecord);

  Single<List<GroupRecord>> getSearchedChannels(String keyword, String channelType, String joined);

  Flowable<List<GroupRecord>> getActiveGroups();
}
