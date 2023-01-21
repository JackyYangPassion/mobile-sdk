package io.inappchat.inappchat.remote

import io.inappchat.inappchat.remote.model.common.Response
import io.inappchat.inappchat.remote.model.request.*
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
import io.inappchat.inappchat.remote.model.response.UserAdditionalInfo
import io.inappchat.inappchat.remote.model.response.UserListResponse
import io.inappchat.inappchat.remote.model.response.UserResponse
import io.inappchat.inappchat.remote.service.AuthApi
import io.inappchat.inappchat.remote.service.ChatApi
import io.inappchat.inappchat.remote.service.UserApi
import io.inappchat.inappchat.remote.util.Utils
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File
import java.net.URLEncoder

/**
 * Created by DK on 24/11/18.
 */
internal class ApiHandlerImpl(
  private val authApi: AuthApi, private val userApi: UserApi, private val chatApi: ChatApi
) : ApiHandler {
  override fun getUserInfo(
    tenantId: String,
    ertcUserId: String,
    appUserIds: UserInfoRequest
  ): Single<UserAdditionalInfo> {
    return chatApi.getUserInfo(tenantId, ertcUserId,  appUserIds)
  }

  override fun getBlockedUsers(
    tenantId: String, ertcUserId: String
  ): Single<BlockedUsersListResponse> {
    return chatApi.getBlockedUsers(tenantId, ertcUserId)
  }

  override fun blockUnblockUser(
    tenantId: String,
    ertcUserId: String,
    action: String?,
    blockUnblockUserRequest: BlockUnblockUserRequest
  ): Single<Response> {
    return chatApi.blockUnblockUser(tenantId, ertcUserId, action, blockUnblockUserRequest)
  }

  override fun validateUrl(url: String): Single<TenantDetailResponse> {
    return authApi.validateUrl(URLEncoder.encode(url, "UTF-8"))
  }

  override fun login(tenantId: String, request: Login): Single<UserResponse> {
    return userApi.login(tenantId, request)
  }

  override fun activeAuth0User(tenantId: String, appUserId: String): Single<Response> {
    return userApi.activeAuth0User(tenantId, AppUserIdRequest(appUserId))
  }

  override fun getUsersInSync(tenantId: String, lastId: String?): Call<UserListResponse?> {
    return userApi.getUsersInSync(tenantId, lastId)
  }

  override fun getUpdatedUsers(
    userID: String, tenantId: String, updateType: String, lastCallTime: String
  ): Single<UserListResponse> {
    return userApi.getUpdatedUsers(userID, tenantId, updateType, lastCallTime)
  }

  override fun getUsers(
    userId: String, tenantId: String, lastId: String?
  ): Single<UserListResponse> {
    return userApi.getUsers(userId, tenantId, lastId)
  }

  override fun forgotPassword(
    tenantId: String, request: ForgotPassword
  ): Single<Response> {
    return userApi.forgotPassword(tenantId, request)
  }

  override fun changePassword(
    tenantId: String, request: ChangePassword, userId: String
  ): Single<Response> {
    return userApi.changePassword(tenantId, request, userId)
  }

  override fun createThread(
    tenantId: String, request: CreateThreadRequest, ertcUserId: String
  ): Single<CreateThreadResponse> {
    return chatApi.createThread(tenantId, request, ertcUserId)
  }

  override fun sendMessage(
    tenantId: String?,
    request: MessageRequest?,
    ertcUserId: String?
  ): Single<MessageResponse> {
    return chatApi.sendMessage(tenantId, request, ertcUserId)
  }

  override fun sendE2EMessage(
    tenantId: String,
    request: MessageRequest,
    ertcUserId: String
  ): Single<MessageE2EResponse> {
    return chatApi.sendE2EMessage(tenantId, request, ertcUserId)
  }

  override fun sendMessages(
    apiKey: String, tenantId: String, ertcUserId: String, request: List<MessageRequest>
  ): Single<OfflineMessageResponse> {
    return chatApi.sendMessages(apiKey, tenantId, ertcUserId, request = request)
  }

  override fun getChatUser(
    tenantId: String, request: UpdateUserRequest
  ): Single<ChatUserResponse> {
    return chatApi.getChatUser(tenantId, request)
  }

  override fun sendMedia(
    tenantId: String,
    threadId: String,
    senderId: String,
    mediaPath: String,
    metaData: String,
    mediaType: String,
    ertcUserId: String,
    replyThread: ReplyThread?,
    mediaMimeType : String,
    clientCreatedAt: Long?,
    customData: String?
  ): Single<MessageResponse> {
    val mediaFile = File(mediaPath)
    val type: MediaType = MediaType.parse("multipart/form-data")!!

    val requestFile: RequestBody = RequestBody.create(MediaType.parse(mediaMimeType), mediaFile)

    val mFile: MultipartBody.Part =
      MultipartBody.Part.createFormData("media", mediaFile.name, requestFile)
    val rbThreadId: RequestBody = RequestBody.create(type, threadId)
    val rbSenderId: RequestBody = RequestBody.create(type, senderId)
    val rbMetaData: RequestBody = RequestBody.create(type, Utils.getMetaDataJson(metaData))

    var replyThreadBody : RequestBody? = null
    if(replyThread != null){

      //json["baseMsgUniqueId"] = replyThread.parentMsgId
      //json["replyMsgConfig"] = replyThread.replyMsgConfig
      val parentMsgId = replyThread.parentMsgId
      val replyMsgConfig = replyThread.replyMsgConfig
      val jsonString = "{\"baseMsgUniqueId\" : \"$parentMsgId\"," +
          "\"replyMsgConfig\" : \"$replyMsgConfig\"}"

      replyThreadBody = RequestBody.create(type, jsonString)
    }
    var rbMsgType : RequestBody? = null
    if (mediaType == "gif") {
      rbMsgType = RequestBody.create(type, mediaType)
    }

    return chatApi.sendMedia(tenantId = tenantId,
      threadId = rbThreadId, senderId = rbSenderId, metaData = rbMetaData
      , mFile = mFile, ertcUserId = ertcUserId, msgType = rbMsgType, replyThread = replyThreadBody,
      senderTimeStampMs = clientCreatedAt, customData = customData)

  }

  override fun createGroup(
    tenantId: String,
    ertcUserId: String,
    name: String,
    groupType: String,
    description: String,
    participants: List<String>,
    mediaPath: String,
    mediaMimeType : String
  ): Single<GroupResponse> {
    val type: MediaType = MediaType.parse("multipart/form-data")!!
    val rbName: RequestBody = RequestBody.create(type, name)
    val rbGroupType: RequestBody = RequestBody.create(type, groupType)
    var rbDescription: RequestBody? = RequestBody.create(type, description)
    val rbParticipants: RequestBody =
      RequestBody.create(type, getStringifyParticipants(participants))
    val mediaFile = File(mediaPath)
    if (description.isNullOrEmpty()) {
      rbDescription = null
    }
    return if (mediaFile.exists()) {
      val requestFile: RequestBody = RequestBody.create(MediaType.parse(mediaMimeType), mediaFile)
      val mFile: MultipartBody.Part =
        MultipartBody.Part.createFormData("media", mediaFile.name, requestFile)
      chatApi.createGroupWithPic(
        tenantId, ertcUserId, rbName, rbGroupType, rbDescription, rbParticipants, mFile
      )
    } else {
      chatApi.createGroup(
        tenantId, ertcUserId, rbName, rbGroupType, rbDescription, rbParticipants
      )
    }
  }

  override fun updateGroup(
    tenantId: String,
    ertcUserId: String,
    groupId: String,
    name: String,
    description: String,
    mediaPath: String,
    mediaMimeType : String
  ): Single<GroupResponse> {
    val type: MediaType = MediaType.parse("multipart/form-data")!!
    val rbName: RequestBody = RequestBody.create(type, name)
    var rbDescription: RequestBody? = RequestBody.create(type, description)
    val rbGroupId: RequestBody = RequestBody.create(type, groupId)
    val mediaFile = File(mediaPath)
    if (description.isNullOrEmpty()) {
      rbDescription = null
    }
    return if (mediaFile.exists()) {
      val requestFile: RequestBody = RequestBody.create(MediaType.parse(mediaMimeType), mediaFile)
      val mFile: MultipartBody.Part =
        MultipartBody.Part.createFormData("media", mediaFile.name, requestFile)
      chatApi.updateGroupDetailWithPic(
        tenantId, ertcUserId, rbGroupId, rbName, rbDescription, mFile
      )
    } else {
      chatApi.updateGroupDetail(tenantId, ertcUserId, rbGroupId, rbName, rbDescription)
    }
  }

  override fun addGroupParticipants(
    tenantId: String, ertcUserId: String, groupId: String, participants: List<String>
  ): Single<GroupResponse> {
    return chatApi.addGroupParticipants(
      tenantId, ertcUserId, groupId, GroupParticipantsRequest(participants)
    )
  }

  override fun removeGroupParticipants(
    tenantId: String, ertcUserId: String, groupId: String, participants: List<String>
  ): Single<GroupResponse> {
    return chatApi.removeGroupParticipants(
      tenantId, ertcUserId, groupId, GroupParticipantsRequest(participants)
    )
  }

  override fun adminChanges(
    tenantId: String, ertcUserId: String, groupId: String, action: String, participant: String
  ): Single<GroupResponse> {
    return chatApi.adminChanges(
      tenantId, ertcUserId, groupId, action, GroupAdminRequest(participant)
    )
  }

  override fun getGroup(
    tenantId: String, ertcUserId: String, groupId: String
  ): Single<GroupResponse> {
    return chatApi.getGroup(tenantId, ertcUserId, groupId)
  }

  override fun getGroupByThreadId(
    tenantId: String, ertcUserId: String, threadId: String
  ): Single<GroupResponse> {
    return chatApi.getGroupByThreadId(tenantId, ertcUserId, threadId)
  }

  override fun getGroups(tenantId: String, ertcUserId: String): Single<GroupListResponse> {
    return chatApi.getGroups(tenantId, ertcUserId)
  }

  private fun getStringifyParticipants(participants: List<String>): String {
    val length = participants.size
    var stringify = ""
    for (x in 0 until length) {
      stringify = if (x == length - 1) {
        stringify + "\"" + participants.get(x) + "\""
      } else {
        stringify + "\"" + participants.get(x) + "\",";
      }
    }
    return "[$stringify]"
  }

  override fun updateProfile(
    tenantId: String,
    userId: String,
    mediaPath: String,
    mediaType: String,
    loginType: String,
    profileStatus: String,
    mediaMimeType : String
  ): Single<UserResponse> {

    val type: MediaType = MediaType.parse("multipart/form-data")!!

    var mFile : MultipartBody.Part ? = null
    if(mediaPath.isNotEmpty()){
      val mediaFile = File(mediaPath)
      val requestFile: RequestBody = RequestBody.create(MediaType.parse(mediaMimeType), mediaFile)
      mFile = MultipartBody.Part.createFormData("profilePic", mediaFile.name, requestFile)
    }

    val loginType: RequestBody = RequestBody.create(type, loginType)
    val profileStatusRequestBody : RequestBody = RequestBody.create(type, profileStatus)

    return userApi.updateProfile(tenantId, userId, loginType, profileStatusRequestBody, mFile)
  }

  override fun getProfile(appUserId: String): Single<UserResponse> {
    return userApi.getProfile(appUserId)
  }

  override fun userLogout(
    tenantId: String,
    userId: String,
    request: Logout
  ): Single<Response> {
    return userApi.userLogout(tenantId, userId, request)
  }

  override fun chatLogout(
    tenantId: String,
    eRTCUserId: String,
    request: Logout
  ): Single<Response> {
    return chatApi.chatLogout(tenantId, eRTCUserId, request)
  }

  override fun updateUserInfo(
    tenantId: String, eRTCUserId: String, request: UpdateUserRequest
  ): Single<ChatUserResponse> {
    return chatApi.updateUserInfo(tenantId, eRTCUserId, request)
  }

  override fun removeProfilePic(
    tenantId: String,
    userId: String
  ): Single<UserResponse> {
    return userApi.removeProfilePic(tenantId, userId)
  }

  override fun removeGroupPic(
    tenantId: String,
    ertcUserId: String,
    groupId: String
  ): Single<GroupResponse> {
    return chatApi.removeGroupPic(tenantId, ertcUserId, groupId)
  }

  override fun globalNotificationSettings(
    tenantId: String,
    ertcUserId: String,
    request: NotificationSettingsRequest
  ): Single<Response> {
    return chatApi.globalNotificationSettings(tenantId, ertcUserId, request)
  }

  override fun threadNotificationSettings(
    tenantId: String,
    threadId: String,
    ertcUserId: String,
    request: NotificationSettingsRequest
  ): Single<Response> {
    return chatApi.threadNotificationSettings(tenantId, ertcUserId, threadId, request)
  }

  override fun sendReaction(
    tenantId: String,
    ertcUserId: String,
    request: ChatReactionRequest
  ): Single<ChatReactionResponse> {
    return chatApi.sendReaction(tenantId, ertcUserId, request)
  }

  override fun deleteMessage(
    tenantId: String,
    ertcUserId: String,
    request: MessageRequest
  ): Single<MessageUpdateResponse> {
    return chatApi.deleteMessage(tenantId, request, ertcUserId)
  }

  override fun editMessage(
    tenantId: String,
    ertcUserId: String,
    request: MessageRequest
  ): Single<MessageResponse> {
    return chatApi.editMessage(tenantId, request, ertcUserId)
  }

  override fun editE2EMessage(
    tenantId: String,
    eRTCUserId: String,
    request: MessageRequest
  ): Single<MessageE2EResponse> {
    return chatApi.editE2EMessage(tenantId, eRTCUserId, request)
  }

  override fun forwardChat(
    apiKey: String, tenantId: String, ertcUserId: String, request: List<MessageRequest>
  ): Single<ForwardChatResponse> {
    return chatApi.forwardChat(apiKey, tenantId, ertcUserId, request = request)
  }

  override fun getMessageHistory(
    tenantId: String,
    eRTCUserId: String,
    threadId: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?,
    includeCurrentMsg: Boolean?
  ): Single<ChatRestoreResponse> {
    return chatApi.getMessageHistory(tenantId, eRTCUserId, threadId, currentMsgId, direction, pageSize, includeCurrentMsg)
  }

  override fun getThreadHistory(
    tenantId: String,
    eRTCUserId: String
  ): Single<ThreadRestoreResponse> {
    return chatApi.getThreadHistoryV2(tenantId, eRTCUserId)
  }

  override fun globalSearch(
    tenantId: String,
    eRTCUserId: String,
    request: SearchRequest
  ): Single<SearchChatResponse> {
    return chatApi.globalSearch(tenantId, eRTCUserId, 50, 0, request)
  }

  override fun starMessage(
    tenantId: String,
    ertcUserId: String,
    request: StarMessageRequest
  ): Single<MessageResponse> {
    return chatApi.starMessage(tenantId, request, ertcUserId)
  }

  override fun followThread(
    tenantId: String,
    ertcUserId: String,
    request: FollowThreadRequest
  ): Single<MessageResponse> {
    return chatApi.followThread(tenantId, request, ertcUserId)
  }

  override fun reportMessage(
    tenantId: String,
    ertcUserId: String,
    request: ReportMessageRequest
  ): Single<MessageReportResponse> {
    return chatApi.reportMessage(tenantId, request, ertcUserId)
  }

  override fun getSearchedGroups(
    tenantId: String,
    ertcUserId: String,
    keyword: String,
    channelType: String?,
    joined: String?
  ): Single<GroupListResponse> {
    return chatApi.getSearchedGroups(tenantId, ertcUserId, keyword, channelType, joined)
  }

  override fun otherDeviceChatLogout(
    tenantId: String,
    eRTCUserId: String,
    request: Logout
  ): Single<Response> {
    return chatApi.otherDeviceChatLogout(tenantId, eRTCUserId, request)
  }

  override fun getMediaGallery(
    tenantId: String,
    eRTCUserId: String,
    threadId: String,
    mediaType: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?,
    includeCurrentMsg: Boolean?,
    deep: Boolean?
  ): Single<ChatRestoreResponse> {
    return chatApi.getMediaGallery(
      tenantId, eRTCUserId, threadId,
      mediaType, currentMsgId, direction,
      pageSize, includeCurrentMsg, deep
    )
  }

  override fun getFollowThread(
    tenantId: String,
    eRTCUserId: String,
    threadId: String?,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?
  ): Single<FollowThreadResponse> {
    return chatApi.getFollowThread(
      tenantId, eRTCUserId, threadId, currentMsgId, direction, pageSize, false
    )
  }

  override fun clearChatHistory(
    tenantId: String,
    eRTCUserId: String,
    threadId: String?
  ): Single<ClearChatHistoryResponse> {
    return chatApi.clearChatHistory(tenantId, eRTCUserId, threadId)
  }

  override fun getChatSettings(
    tenantId: String,
    eRTCUserId: String
  ): Single<ChatSettingsResponse> {
    return chatApi.getChatSettings(tenantId, eRTCUserId)
  }
}