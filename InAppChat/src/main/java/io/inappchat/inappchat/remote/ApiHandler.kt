package io.inappchat.inappchat.remote

import io.inappchat.inappchat.remote.model.common.Response
import io.inappchat.inappchat.remote.model.request.BlockUnblockUserRequest
import io.inappchat.inappchat.remote.model.request.ChangePassword
import io.inappchat.inappchat.remote.model.request.ChatReactionRequest
import io.inappchat.inappchat.remote.model.request.UpdateUserRequest
import io.inappchat.inappchat.remote.model.request.CreateThreadRequest
import io.inappchat.inappchat.remote.model.request.FollowThreadRequest
import io.inappchat.inappchat.remote.model.request.ForgotPassword
import io.inappchat.inappchat.remote.model.request.Login
import io.inappchat.inappchat.remote.model.request.Logout
import io.inappchat.inappchat.remote.model.request.MessageRequest
import io.inappchat.inappchat.remote.model.request.NotificationSettingsRequest
import io.inappchat.inappchat.remote.model.request.ReplyThread
import io.inappchat.inappchat.remote.model.request.ReportMessageRequest
import io.inappchat.inappchat.remote.model.request.SearchRequest
import io.inappchat.inappchat.remote.model.request.StarMessageRequest
import io.inappchat.inappchat.remote.model.request.UserInfoRequest
import io.inappchat.inappchat.remote.model.response.BlockedUsersListResponse
import io.inappchat.inappchat.remote.model.response.ChatUserResponse
import io.inappchat.inappchat.remote.model.response.ChatReactionResponse
import io.inappchat.inappchat.remote.model.response.ChatRestoreResponse
import io.inappchat.inappchat.remote.model.response.ChatSettingsResponse
import io.inappchat.inappchat.remote.model.response.ClearChatHistoryResponse
import io.inappchat.inappchat.remote.model.response.CreateThreadResponse
import io.inappchat.inappchat.remote.model.response.FollowThreadResponse
import io.inappchat.inappchat.remote.model.response.ForwardChatResponse
import io.inappchat.inappchat.remote.model.response.GroupListResponse
import io.inappchat.inappchat.remote.model.response.GroupResponse
import io.inappchat.inappchat.remote.model.response.MessageE2EResponse
import io.inappchat.inappchat.remote.model.response.MessageReportResponse
import io.inappchat.inappchat.remote.model.response.MessageResponse
import io.inappchat.inappchat.remote.model.response.MessageUpdateResponse
import io.inappchat.inappchat.remote.model.response.OfflineMessageResponse
import io.inappchat.inappchat.remote.model.response.SearchChatResponse
import io.inappchat.inappchat.remote.model.response.TenantDetailResponse
import io.inappchat.inappchat.remote.model.response.ThreadRestoreResponse
import io.inappchat.inappchat.remote.model.response.UserListResponse
import io.inappchat.inappchat.remote.model.response.UserResponse
import io.inappchat.inappchat.remote.model.response.UserAdditionalInfo
import io.reactivex.rxjava3.core.Single
import retrofit2.Call

/**
 * Created by DK on 24/11/18.
 */
interface ApiHandler {

  fun validateUrl(url: String): Single<TenantDetailResponse>

  fun login(tenantId: String, request: Login): Single<UserResponse>

  fun activeAuth0User(tenantId: String, appUserId: String): Single<Response>

  fun forgotPassword(tenantId: String, request: ForgotPassword): Single<Response>

  fun changePassword(tenantId: String, request: ChangePassword, userId: String): Single<Response>

  fun getUsers(userId: String, tenantId: String, lastId: String?): Single<UserListResponse>

  fun getUpdatedUsers(
    userId: String, tenantId: String, updateType: String, lastCallTime: String
  ): Single<UserListResponse>

  fun getUsersInSync(tenantId: String, lastId: String?): Call<UserListResponse?>

  fun blockUnblockUser(
    tenantId: String,
    ertcUserId: String,
    action: String?,
    blockUnblockUserRequest: BlockUnblockUserRequest
  ): Single<Response>

  fun getBlockedUsers(tenantId: String, ertcUserId: String): Single<BlockedUsersListResponse>

  // Chat Server
  fun createThread(
    tenantId: String, request: CreateThreadRequest, ertcUserId: String
  ): Single<CreateThreadResponse>

  fun sendMessage(
    tenantId: String?, request: MessageRequest?, ertcUserId: String?
  ): Single<MessageResponse>

  fun sendE2EMessage(
    tenantId: String, request: MessageRequest, ertcUserId: String
  ): Single<MessageE2EResponse>

  fun sendMessages(
    apiKey: String, tenantId: String, ertcUserId: String, request: List<MessageRequest>
  ): Single<OfflineMessageResponse>

  fun getChatUser(
    tenantId: String, request: UpdateUserRequest
  ): Single<ChatUserResponse>

  fun sendMedia(
    tenantId: String,
    threadId: String,
    senderId: String,
    mediaPath: String,
    metaData: String,
    mediaType: String,
    ertcUserId: String,
    replyThread: ReplyThread? = null,
    mediaMimeType : String,
    clientCreatedAt: Long? = null,
    customData: String? = null
  ): Single<MessageResponse>

  fun createGroup(
    tenantId: String,
    ertcUserId: String,
    name: String,
    groupType: String,
    description: String,
    participants: List<String>,
    picPath: String,
    mediaMimeType : String
  ): Single<GroupResponse>

  fun updateGroup(
    tenantId: String,
    ertcUserId: String,
    groupId: String,
    name: String,
    description: String,
    picPath: String,
    mediaMimeType : String
  ): Single<GroupResponse>

  fun addGroupParticipants(
    tenantId: String, ertcUserId: String, groupId: String, participants: List<String>
  ): Single<GroupResponse>

  fun removeGroupParticipants(
    tenantId: String, ertcUserId: String, groupId: String, participants: List<String>
  ): Single<GroupResponse>

  fun getGroup(
    tenantId: String, ertcUserId: String, groupId: String
  ): Single<GroupResponse>

  fun getGroupByThreadId(
    tenantId: String, ertcUserId: String, threadId: String
  ): Single<GroupResponse>

  fun getGroups(
    tenantId: String, ertcUserId: String
  ): Single<GroupListResponse>

  fun adminChanges(
    tenantId: String, ertcUserId: String, groupId: String, action: String, participant: String
  ): Single<GroupResponse>

  fun updateProfile(
    tenantId: String,
    userId: String,
    mediaPath: String,
    mediaType: String,
    loginType: String,
    profileStatus: String,
    mediaMimeType : String
  ): Single<UserResponse>

  fun getProfile(
    appUserId: String
  ): Single<UserResponse>

  fun userLogout(
    tenantId: String,
    userId: String,
    request: Logout
  ): Single<Response>

  fun chatLogout(
    tenantId: String,
    eRTCUserId: String,
    request: Logout
  ): Single<Response>

  fun getUserInfo(
    tenantId: String,
    ertcUserId: String,
    appUserIds: UserInfoRequest
  ): Single<UserAdditionalInfo>

  fun updateUserInfo(
    tenantId: String, eRTCUserId: String, request: UpdateUserRequest
  ): Single<ChatUserResponse>

  fun removeProfilePic(
    tenantId: String,
    userId: String
  ): Single<UserResponse>

  fun removeGroupPic(
    tenantId: String,
    ertcUserId: String,
    groupId: String
  ): Single<GroupResponse>

  fun globalNotificationSettings(
    tenantId: String,
    ertcUserId: String,
    request: NotificationSettingsRequest
  ): Single<Response>

  fun threadNotificationSettings(
    tenantId: String,
    threadId: String,
    ertcUserId: String,
    request: NotificationSettingsRequest
  ): Single<Response>

  fun sendReaction(
    tenantId: String,
    ertcUserId: String,
    request: ChatReactionRequest
  ): Single<ChatReactionResponse>

  fun deleteMessage(
    tenantId: String,
    ertcUserId: String,
    request: MessageRequest
  ): Single<MessageUpdateResponse>

  fun editMessage(
    tenantId: String,
    ertcUserId: String,
    request: MessageRequest
  ): Single<MessageResponse>

  fun editE2EMessage(
    tenantId: String,
    eRTCUserId: String,
    request: MessageRequest
  ): Single<MessageE2EResponse>

  fun forwardChat(
    apiKey: String, tenantId: String, ertcUserId: String, request: List<MessageRequest>
  ): Single<ForwardChatResponse>

  fun getMessageHistory(
    tenantId: String,
    eRTCUserId: String,
    threadId: String,
    currentMsgId: String? = null,
    direction: String? = null,
    pageSize: Int? = null,
    includeCurrentMsg: Boolean? = false
  ): Single<ChatRestoreResponse>

  fun getThreadHistory(
    tenantId: String,
    eRTCUserId: String
  ): Single<ThreadRestoreResponse>

  fun globalSearch(
    tenantId: String,
    eRTCUserId: String,
    request: SearchRequest
  ): Single<SearchChatResponse>

  fun starMessage(
    tenantId: String,
    ertcUserId: String,
    request: StarMessageRequest
  ): Single<MessageResponse>

  fun followThread(
    tenantId: String,
    ertcUserId: String,
    request: FollowThreadRequest
  ): Single<MessageResponse>

  fun reportMessage(
    tenantId: String,
    ertcUserId: String,
    request: ReportMessageRequest
  ): Single<MessageReportResponse>

  fun getSearchedGroups(
    tenantId: String,
    ertcUserId: String,
    keyword: String,
    channelType: String?,
    joined: String?
  ): Single<GroupListResponse>

  fun otherDeviceChatLogout(
    tenantId: String,
    eRTCUserId: String,
    request: Logout
  ): Single<Response>

  fun getMediaGallery(
    tenantId: String,
    eRTCUserId: String,
    threadId: String,
    mediaType: String,
    currentMsgId: String? = null,
    direction: String? = null,
    pageSize: Int? = null,
    includeCurrentMsg: Boolean? = false,
    deep: Boolean? = true
  ): Single<ChatRestoreResponse>

  fun getFollowThread(
    tenantId: String,
    eRTCUserId: String,
    threadId: String?,
    currentMsgId: String? = null,
    direction: String? = null,
    pageSize: Int? = null
  ): Single<FollowThreadResponse>

  fun clearChatHistory(
    tenantId: String,
    eRTCUserId: String,
    threadId: String?
  ): Single<ClearChatHistoryResponse>

  fun getChatSettings(
    tenantId: String,
    eRTCUserId: String
  ): Single<ChatSettingsResponse>
}