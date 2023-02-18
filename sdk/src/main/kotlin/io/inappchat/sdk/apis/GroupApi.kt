package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.AddRemoveParticipantsRequest
import io.inappchat.sdk.models.CreateOrUpdateGroupPost200Response
import io.inappchat.sdk.models.DeleteGroupProfilePic200Response
import io.inappchat.sdk.models.GroupRequest
import io.inappchat.sdk.models.MakeDismissAdminPostRequest
import io.inappchat.sdk.models.RestrictParticipantsRequest
import io.inappchat.sdk.models.Thread200Response
import io.inappchat.sdk.models.UnrestrictParticipantsRequest
import io.inappchat.sdk.models.UserGroupsFilter200Response

interface GroupApi {
    /**
     * Add participants to group
     * Add participants to group
     * Responses:
     *  - 200: Forgot Password Response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param groupId Group ID
     * @param xNonce epoch timestamp
     * @param body  (optional)
     * @return [CreateOrUpdateGroupPost200Response]
     */
    @POST("{version}/tenants}/{tenantId}/{eRTCUserId}/group/{groupId}/addParticipants")
    suspend fun addParticipantsPost(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("groupId") groupId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Body body: AddRemoveParticipantsRequest? = null): Response<CreateOrUpdateGroupPost200Response>

    /**
     * Create or Update group
     * Create of Update group. For profilePic use multipart/formdata and in this case stringify participants list.
     * Responses:
     *  - 200: Forgot Password Response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @param body  (optional)
     * @return [CreateOrUpdateGroupPost200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/group")
    suspend fun createOrUpdateGroupPost(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Body body: GroupRequest? = null): Response<CreateOrUpdateGroupPost200Response>

    /**
     * Remove group profile pic
     * Remove group profile pic
     * Responses:
     *  - 200: Chat User Add Update result
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param groupId Group ID
     * @return [DeleteGroupProfilePic200Response]
     */
    @DELETE("{version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/removeProfilePic")
    suspend fun deleteGroupProfilePic(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("groupId") groupId: kotlin.String): Response<DeleteGroupProfilePic200Response>

    /**
     * Get group by groupId
     * Get group by groupId
     * Responses:
     *  - 200: Get group response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param groupId Group ID
     * @param xNonce epoch timestamp
     * @return [CreateOrUpdateGroupPost200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}")
    suspend fun getGroupByGroupIdGet(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("groupId") groupId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String): Response<CreateOrUpdateGroupPost200Response>

    /**
     * Get group by threadId
     * Get group by threadId
     * Responses:
     *  - 200: Get group by threadId response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param threadId Thread ID received in chat object
     * @param xNonce epoch timestamp
     * @return [CreateOrUpdateGroupPost200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/group/{threadId}/groupByThreadId")
    suspend fun getGroupByThreadIdGet(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("threadId") threadId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String): Response<CreateOrUpdateGroupPost200Response>

    /**
     * Make or Dismiss a group user as admin
     * Make or Dismiss a group user as admin
     * Responses:
     *  - 200: Forgot Password Response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param groupId Group ID
     * @param action Action: make/dimiss
     * @param xNonce epoch timestamp
     * @param body  (optional)
     * @return [CreateOrUpdateGroupPost200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/makeDismissAdmin/{action}")
    suspend fun makeDismissAdminPost(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("groupId") groupId: kotlin.String, @Path("action") action: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Body body: MakeDismissAdminPostRequest? = null): Response<CreateOrUpdateGroupPost200Response>

    /**
     * Delete participants from group
     * Delete participants from group
     * Responses:
     *  - 200: Forgot Password Response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param groupId Group ID
     * @param xNonce epoch timestamp
     * @param body  (optional)
     * @return [CreateOrUpdateGroupPost200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/removeParticipants")
    suspend fun removeParticipantsPost(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("groupId") groupId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Body body: AddRemoveParticipantsRequest? = null): Response<CreateOrUpdateGroupPost200Response>

    /**
     * Ban or mute users
     * Ban or mute users
     * Responses:
     *  - 200: Thread data
     *
     * @param version API key
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param groupId Group ID
     * @param body Unique AppID of the user to get
     * @return [Thread200Response]
     */
    @POST("{version}/tenants}/{tenantId}/{eRTCUserId}/group/{groupId}/restrictParticipants")
    suspend fun restrictParticipants(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("groupId") groupId: kotlin.String, @Body body: RestrictParticipantsRequest): Response<Thread200Response>

    /**
     * UnBan or unmute users
     * UnBan or unmute users
     * Responses:
     *  - 200: Thread data
     *
     * @param version API key
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param groupId Group ID
     * @param body Unique AppID of the user to get
     * @return [Thread200Response]
     */
    @POST("{version}/tenants}/{tenantId}/{eRTCUserId}/group/{groupId}/unrestrictParticipants")
    suspend fun unrestrictParticipants(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("groupId") groupId: kotlin.String, @Body body: UnrestrictParticipantsRequest): Response<Thread200Response>


    /**
    * enum for parameter groupType
    */
    enum class GroupType_userGroupsFilter(val value: kotlin.String) {
        @Json(name = "public") `public`("public"),
        @Json(name = "private") `private`("private")
    }


    /**
    * enum for parameter joined
    */
    enum class Joined_userGroupsFilter(val value: kotlin.String) {
        @Json(name = "yes") yes("yes"),
        @Json(name = "no") no("no")
    }

    /**
     * Get user groups
     * Filter All groups where user is participant or group is public
     * Responses:
     *  - 200: Get group response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @param keyword Get only public or private group (optional)
     * @param groupType Filter by group type (optional)
     * @param joined Get only joined/not joined groups (optional)
     * @return [UserGroupsFilter200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/group")
    suspend fun userGroupsFilter(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Query("keyword") keyword: kotlin.String? = null, @Query("groupType") groupType: GroupType_userGroupsFilter? = null, @Query("joined") joined: Joined_userGroupsFilter? = null): Response<UserGroupsFilter200Response>

    /**
     * Get user groups
     * Get All groups where user is participant
     * Responses:
     *  - 200: Get group response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @return [UserGroupsFilter200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/group/userGroups")
    suspend fun userGroupsGet(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String): Response<UserGroupsFilter200Response>

}
