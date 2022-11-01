package com.ripbull.ertc.remote.service

import com.ripbull.ertc.remote.model.common.Response
import com.ripbull.ertc.remote.model.request.BlockUnblockUserRequest
import com.ripbull.ertc.remote.model.request.ChatReactionRequest
import com.ripbull.ertc.remote.model.request.CreateThreadRequest
import com.ripbull.ertc.remote.model.request.FollowThreadRequest
import com.ripbull.ertc.remote.model.request.GroupAdminRequest
import com.ripbull.ertc.remote.model.request.GroupParticipantsRequest
import com.ripbull.ertc.remote.model.request.Logout
import com.ripbull.ertc.remote.model.request.MessageRequest
import com.ripbull.ertc.remote.model.request.NotificationSettingsRequest
import com.ripbull.ertc.remote.model.request.ReportMessageRequest
import com.ripbull.ertc.remote.model.request.SearchRequest
import com.ripbull.ertc.remote.model.request.StarMessageRequest
import com.ripbull.ertc.remote.model.request.UpdateUserRequest
import com.ripbull.ertc.remote.model.request.UserInfoRequest
import com.ripbull.ertc.remote.model.response.BlockedUsersListResponse
import com.ripbull.ertc.remote.model.response.ChatUserResponse
import com.ripbull.ertc.remote.model.response.ChatReactionResponse
import com.ripbull.ertc.remote.model.response.ChatRestoreResponse
import com.ripbull.ertc.remote.model.response.ChatSettingsResponse
import com.ripbull.ertc.remote.model.response.ClearChatHistoryResponse
import com.ripbull.ertc.remote.model.response.CreateThreadResponse
import com.ripbull.ertc.remote.model.response.FollowThreadResponse
import com.ripbull.ertc.remote.model.response.ForwardChatResponse
import com.ripbull.ertc.remote.model.response.GroupListResponse
import com.ripbull.ertc.remote.model.response.GroupResponse
import com.ripbull.ertc.remote.model.response.MessageE2EResponse
import com.ripbull.ertc.remote.model.response.MessageReportResponse
import com.ripbull.ertc.remote.model.response.MessageResponse
import com.ripbull.ertc.remote.model.response.MessageUpdateResponse
import com.ripbull.ertc.remote.model.response.OfflineMessageResponse
import com.ripbull.ertc.remote.model.response.ThreadRestoreResponse
import com.ripbull.ertc.remote.model.response.SearchChatResponse
import com.ripbull.ertc.remote.model.response.Token
import com.ripbull.ertc.remote.model.response.UserAdditionalInfo
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/thread")
  fun createThread(
    @Path("tenantId") tenantId: String,
    @Body request: CreateThreadRequest, 
    @Path("ertcUserId") ertcUserId: String
  ): Single<CreateThreadResponse>

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/chat")
  fun sendMessage(
    @Path("tenantId") tenantId: String?,
    @Body request: MessageRequest?,
    @Path("ertcUserId") ertcUserId: String?
  ): Single<MessageResponse>

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/chat/e2eEncrypted")
  fun sendE2EMessage(
    @Path("tenantId") tenantId: String,
    @Body request: MessageRequest,
    @Path("ertcUserId") ertcUserId: String
  ): Single<MessageE2EResponse>

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/chat/multiple")
  fun sendMessages(
    @Header("apiKey") apiKey: String,
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Query("context") context: String = "offlineDispatch",
    @Body request: List<MessageRequest>
  ): Single<OfflineMessageResponse>

  @Multipart
  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/chat")
  fun sendMedia(
    @Path("tenantId") tenantId: String,
    @Part("threadId") threadId: RequestBody,
    @Part("sendereRTCUserId") senderId: RequestBody,
    @Part("metadata") metaData: RequestBody,
    @Part mFile: MultipartBody.Part,
    @Path("ertcUserId") ertcUserId: String,
    @Part("msgType") msgType: RequestBody? = null,
    @Part("replyThreadFeatureData") replyThread: RequestBody? = null,
    @Part("senderTimeStampMs") senderTimeStampMs: Long? = System.currentTimeMillis(),
    @Part("customData") customData: String?
  ): Single<MessageResponse>

  @POST("$PROV/tenants/{tenantId}/user")
  fun getChatUser(
    @Path("tenantId") tenantId: String, 
    @Body request: UpdateUserRequest
  ): Single<ChatUserResponse>

  @GET("$PROV/tenants/{tenantId}/user/{ertcUserId}/auth0/refreshToken")
  fun refreshChatToken(
    @Path("tenantId") tenantId: String, 
    @Path("ertcUserId") ertcUserId: String, 
    @Header("Authorization") auth: String
  ): Call<Token>

  @POST("$PROV/tenants/{tenantId}/user/{ertcUserId}/blockUnblock/{action}")
  fun blockUnblockUser(
    @Path("tenantId") tenantId: String, 
    @Path("ertcUserId") ertcUserId: String, 
    @Path("action") action: String?, 
    @Body request: BlockUnblockUserRequest
  ): Single<Response>

  @GET("$PROV/tenants/{tenantId}/user/{ertcUserId}/blockedUsers")
  fun getBlockedUsers(
    @Path("tenantId") tenantId: String, 
    @Path("ertcUserId") ertcUserId: String
  ): Single<BlockedUsersListResponse>

  @Multipart
  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/group")
  fun createGroup(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String, 
    @Part("name") name: RequestBody, 
    @Part("groupType") groupType: RequestBody, 
    @Part("description") description: RequestBody?, 
    @Part("participants") participants: RequestBody
  ): Single<GroupResponse>

  @Multipart
  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/group")
  fun createGroupWithPic(
    @Path("tenantId") tenantId: String, 
    @Path("ertcUserId") ertcUserId: String, 
    @Part("name") name: RequestBody, 
    @Part("groupType") groupType: RequestBody, 
    @Part("description") description: RequestBody?, 
    @Part("participants") participants: RequestBody, 
    @Part mFile: MultipartBody.Part
  ): Single<GroupResponse>

  @Multipart
  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/group")
  fun updateGroupDetail(
    @Path("tenantId") tenantId: String, 
    @Path("ertcUserId") ertcUserId: String, 
    @Part("groupId") groupId: RequestBody, 
    @Part("name") name: RequestBody, 
    @Part("description") description: RequestBody?
  ): Single<GroupResponse>

  @Multipart
  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/group")
  fun updateGroupDetailWithPic(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Part("groupId") groupId: RequestBody,
    @Part("name") name: RequestBody,
    @Part("description") description: RequestBody?,
    @Part mFile: MultipartBody.Part
  ): Single<GroupResponse>

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/group/{groupId}/addParticipants")
  fun addGroupParticipants(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Path("groupId") groupId: String,
    @Body request: GroupParticipantsRequest
  ): Single<GroupResponse>

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/group/{groupId}/removeParticipants")
  fun removeGroupParticipants(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Path("groupId") groupId: String,
    @Body request: GroupParticipantsRequest
  ): Single<GroupResponse>

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/group/{groupId}/makeDismissAdmin/{action}")
  fun adminChanges(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Path("groupId") groupId: String,
    @Path("action") action: String,
    @Body request: GroupAdminRequest
  ): Single<GroupResponse>

  @GET("$PROV/tenants/{tenantId}/{ertcUserId}/group/{groupId}")
  fun getGroup(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Path("groupId") groupId: String
  ): Single<GroupResponse>

  @GET("$PROV/tenants/{tenantId}/{ertcUserId}/group/{threadId}/groupByThreadId")
  fun getGroupByThreadId(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Path("threadId") threadId: String
  ): Single<GroupResponse>

  @GET("$PROV/tenants/{tenantId}/{ertcUserId}/group/userGroups")
  fun getGroups(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String
  ): Single<GroupListResponse>

  @POST("$PROV/tenants/{tenantId}/user/{ertcUserId}/logout")
  fun chatLogout(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") eRTCUserId: String,
    @Body request: Logout
  ): Single<Response>

  @POST("$PROV/tenants/{tenantId}/user/{ertcUserId}/chatUserDetails")
  fun getUserInfo(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Body request: UserInfoRequest
  ): Single<UserAdditionalInfo>

  @POST("$PROV/tenants/{tenantId}/user/{ertcUserId}")
  fun updateUserInfo(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Body request: UpdateUserRequest
  ): Single<ChatUserResponse>

  @DELETE("$PROV/tenants/{tenantId}/{ertcUserId}/group/{groupId}/removeProfilePic")
  fun removeGroupPic(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Path("groupId") groupId: String
  ): Single<GroupResponse>

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/thread/{threadId}")
  fun threadNotificationSettings(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Path("threadId") threadId: String,
    @Body request: NotificationSettingsRequest
  ): Single<Response>

  @POST("$PROV/tenants/{tenantId}/user/{ertcUserId}")
  fun globalNotificationSettings(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Body request: NotificationSettingsRequest
  ): Single<Response>

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/chat/reaction")
  fun sendReaction(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Body request: ChatReactionRequest
  ): Single<ChatReactionResponse>

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/chat/multiple")
  fun forwardChat(
    @Header("apiKey") apiKey: String,
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Query("context") context: String = "forwardChat",
    @Body request: List<MessageRequest>
  ): Single<ForwardChatResponse>

  @HTTP(method = "DELETE", path = "$PROV/tenants/{tenantId}/{ertcUserId}/chat", hasBody = true)
  fun deleteMessage(
    @Path("tenantId") tenantId: String?,
    @Body request: MessageRequest?,
    @Path("ertcUserId") ertcUserId: String?
  ): Single<MessageUpdateResponse>

  @HTTP(method = "PUT", path = "$PROV/tenants/{tenantId}/{ertcUserId}/chat", hasBody = true)
  fun editMessage(
    @Path("tenantId") tenantId: String?,
    @Body request: MessageRequest?,
    @Path("ertcUserId") ertcUserId: String?
  ): Single<MessageResponse>

  @HTTP(method = "PUT", path = "$PROV/tenants/{tenantId}/{eRTCUserId}/chat/e2eEncrypted", hasBody = true)
  fun editE2EMessage(
    @Path("tenantId") tenantId: String?,
    @Path("eRTCUserId") eRTCUserId: String?,
    @Body request: MessageRequest?
  ): Single<MessageE2EResponse>

  @POST("$PROV/tenants/{tenantId}/{eRTCUserId}/chat/search")
  fun globalSearch(
    @Path("tenantId") tenantId: String,
    @Path("eRTCUserId") eRTCUserId: String,
    @Query("limit") limit: Int = 50,
    @Query("skip") skip: Int = 0,
    @Body request: SearchRequest
  ): Single<SearchChatResponse>

  @GET("$PROV/tenants/{tenantId}/{eRTCUserId}/thread/history")
  fun getThreadHistory(
    @Path("tenantId") tenantId: String,
    @Path("eRTCUserId") eRTCUserId: String
  ): Single<ThreadRestoreResponse>

  @GET("$PROV/tenants/{tenantId}/{eRTCUserId}/chat/{threadId}/history")
  fun getMessageHistory(
    @Path("tenantId") tenantId: String,
    @Path("eRTCUserId") eRTCUserId: String,
    @Path("threadId") threadId: String,
    @Query("currentMsgId") currentMsgId: String?,
    @Query("direction") direction: String?,
    @Query("pageSize") pageSize: Int?,
    @Query("includeCurrentMsg") includeCurrentMsg: Boolean?
  ): Single<ChatRestoreResponse>

  @HTTP(method = "PUT", path = "$PROV/tenants/{tenantId}/{ertcUserId}/chat", hasBody = true)
  fun starMessage(
    @Path("tenantId") tenantId: String?,
    @Body request: StarMessageRequest?,
    @Path("ertcUserId") ertcUserId: String?
  ): Single<MessageResponse>

  @HTTP(method = "PUT", path = "$PROV/tenants/{tenantId}/{ertcUserId}/chat", hasBody = true)
  fun followThread(
    @Path("tenantId") tenantId: String?,
    @Body request: FollowThreadRequest?,
    @Path("ertcUserId") ertcUserId: String?
  ): Single<MessageResponse>

  @POST("$PROV/tenants/{tenantId}/{ertcUserId}/chatReports")
  fun reportMessage(
    @Path("tenantId") tenantId: String?,
    @Body request: ReportMessageRequest?,
    @Path("ertcUserId") ertcUserId: String?
  ): Single<MessageReportResponse>

  @GET("$PROV/tenants/{tenantId}/{ertcUserId}/group")
  fun getSearchedGroups(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") ertcUserId: String,
    @Query("keyword") keyword: String,
    @Query("groupType") groupType: String?,
    @Query("joined") joined: String?
  ): Single<GroupListResponse>

  @POST("$PROV/tenants/{tenantId}/user/{ertcUserId}/logoutOtherDevices")
  fun otherDeviceChatLogout(
    @Path("tenantId") tenantId: String,
    @Path("ertcUserId") eRTCUserId: String,
    @Body request: Logout
  ): Single<Response>

  @GET("$PROV/tenants/{tenantId}/{eRTCUserId}/chat/{threadId}/history")
  fun getMediaGallery(
    @Path("tenantId") tenantId: String,
    @Path("eRTCUserId") eRTCUserId: String,
    @Path("threadId") threadId: String,
    @Query("msgType") msgType: String,
    @Query("currentMsgId") currentMsgId: String?,
    @Query("direction") direction: String?,
    @Query("pageSize") pageSize: Int?,
    @Query("includeCurrentMsg") includeCurrentMsg: Boolean?,
    @Query("deep") deep: Boolean?
  ): Single<ChatRestoreResponse>

  @GET("$PROV/tenants/{tenantId}/{eRTCUserId}/chat/replyThread/history")
  fun getFollowThread(
    @Path("tenantId") tenantId: String,
    @Path("eRTCUserId") eRTCUserId: String,
    @Query("threadId") threadId: String?,
    @Query("currentMsgId") currentMsgId: String?,
    @Query("direction") direction: String?,
    @Query("pageSize") pageSize: Int?,
    @Query("includeCurrentMsg") includeCurrentMsg: Boolean?
  ): Single<FollowThreadResponse>

  @POST("$PROV/tenants/{tenantId}/{eRTCUserId}/chat/{threadId}/history/clear")
  fun clearChatHistory(
    @Path("tenantId") tenantId: String,
    @Path("eRTCUserId") eRTCUserId: String,
    @Path("threadId") threadId: String?
  ): Single<ClearChatHistoryResponse>

  @GET("$PROV/tenants/{tenantId}/{eRTCUserId}/chatSettings")
  fun getChatSettings(
    @Path("tenantId") tenantId: String,
    @Path("eRTCUserId") eRTCUserId: String
  ): Single<ChatSettingsResponse>

  @GET("$PROV/tenants/{tenantId}/{eRTCUserId}/thread/history")
  fun getThreadHistoryV2(
    @Path("tenantId") tenantId: String,
    @Path("eRTCUserId") eRTCUserId: String
  ): Single<ThreadRestoreResponse>

  companion object {

    const val PROV = "/v1"
    const val APP = "/app/v1/"
    const val V2 = "/V2"
  }
}