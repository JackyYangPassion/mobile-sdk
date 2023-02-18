package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.BlockUnblockUserPost200Response
import io.inappchat.sdk.models.BlockUnblockUserRequest
import io.inappchat.sdk.models.BlockedUsersGet200Response
import io.inappchat.sdk.models.ChatUserDetailsRequest
import io.inappchat.sdk.models.GetChatUserDetailsPost200Response
import io.inappchat.sdk.models.GetOrUpdateUserByAppId200Response
import io.inappchat.sdk.models.GetOrUpdateUserRequest
import io.inappchat.sdk.models.LogoutOtherDevices200Response
import io.inappchat.sdk.models.LogoutUser200Response
import io.inappchat.sdk.models.LogoutUserRequest
import io.inappchat.sdk.models.PendingEventsGet200Response
import io.inappchat.sdk.models.RefreshAuthToken200Response
import io.inappchat.sdk.models.ResetBadgeCount200Response
import io.inappchat.sdk.models.UpdateUserRequest

interface ERTCUserApi {
    /**
     * Update user by eRTC userId
     * Get a user by eRTC userId
     * Responses:
     *  - 200: ERTC User data
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param version API version
     * @param tenantId Tenanat Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID of caller. Example &#39;5cbdc711c25983101b1b4198&#39;.
     * @param action Action. For example block/unblock.
     * @param body Unique AppID of the user to get
     * @return [BlockUnblockUserPost200Response]
     */
    @POST("{version}/tenants/{tenantId}/user/{eRTCUserId}/blockUnblock/{action}")
    suspend fun blockUnblockUserPost(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("action") action: kotlin.String, @Body body: BlockUnblockUserRequest): Response<BlockUnblockUserPost200Response>

    /**
     * Get blocked users
     * Get blocked users
     * Responses:
     *  - 200: Blocked user list
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param deviceid device123
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39;
     * @return [BlockedUsersGet200Response]
     */
    @GET("{version}/tenants/{tenantId}/user/{eRTCUserId}/blockedUsers")
    suspend fun blockedUsersGet(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Header("deviceid") deviceid: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String): Response<BlockedUsersGet200Response>

    /**
     * Get specific details of other chatUsers
     * Get specific details of other chatUsers
     * Responses:
     *  - 200: Blocked user list
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39;
     * @param body list of appUserIds of chatUsers
     * @return [GetChatUserDetailsPost200Response]
     */
    @POST("{version}/tenants/{tenantId}/user/{eRTCUserId}/chatUserDetails")
    suspend fun getChatUserDetailsPost(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Body body: ChatUserDetailsRequest): Response<GetChatUserDetailsPost200Response>

    /**
     * Get or Update user by appUserId
     * Get a user by APP Unique ID
     * Responses:
     *  - 200: ERTC User data
     *
     * @param version API version
     * @param tenantId Tenant ID
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param body Unique AppID of the user to get
     * @return [GetOrUpdateUserByAppId200Response]
     */
    @POST("{version}/tenants/{tenantId}/user/")
    suspend fun getOrUpdateUserByAppId(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Body body: GetOrUpdateUserRequest): Response<GetOrUpdateUserByAppId200Response>

    /**
     * Logout
     * logoutOtherDevices
     * Responses:
     *  - 200: ERTC User data
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39;
     * @param body Unique AppID of the user to get
     * @return [LogoutOtherDevices200Response]
     */
    @POST("{version}/tenants/{tenantId}/user/{eRTCUserId}/logoutOtherDevices")
    suspend fun logoutOtherDevices(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Body body: LogoutUserRequest): Response<LogoutOtherDevices200Response>

    /**
     * Logout
     * LogoutUser
     * Responses:
     *  - 200: ERTC User data
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39;
     * @param body Unique AppID of the user to get
     * @return [LogoutUser200Response]
     */
    @POST("{version}/tenants/{tenantId}/user/{eRTCUserId}/logout")
    suspend fun logoutUser(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Body body: LogoutUserRequest): Response<LogoutUser200Response>

    /**
     * Get pending events for particular device
     * Get blocked users
     * Responses:
     *  - 200: Pending event list
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39;
     * @param deviceId source device Id (optional)
     * @return [PendingEventsGet200Response]
     */
    @GET("{version}/tenants/{tenantId}/user/{eRTCUserId}/pendingEvents")
    suspend fun pendingEventsGet(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Query("deviceId") deviceId: kotlin.String? = null): Response<PendingEventsGet200Response>

    /**
     * Refresh auth token
     * Refresh auth token
     * Responses:
     *  - 200: Refreshed token
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39;
     * @return [RefreshAuthToken200Response]
     */
    @GET("{version}/tenants/{tenantId}/user/{eRTCUserId}/refreshToken")
    suspend fun refreshAuthToken(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String): Response<RefreshAuthToken200Response>

    /**
     * reset notification badge count
     * reset Bagde count
     * Responses:
     *  - 200: badge updated success
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39;
     * @return [ResetBadgeCount200Response]
     */
    @GET("{version}/tenants/{tenantId}/user/{eRTCUserId}/resetBadgeCount")
    suspend fun resetBadgeCount(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String): Response<ResetBadgeCount200Response>

    /**
     * Update user by eRTC userId
     * Get a user by eRTC userId
     * Responses:
     *  - 200: ERTC User data
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39;
     * @param body Unique AppID of the user to get
     * @return [GetOrUpdateUserByAppId200Response]
     */
    @POST("{version}/tenants/{tenantId}/user/{eRTCUserId}")
    suspend fun updateUserByeRTCUserId(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Body body: UpdateUserRequest): Response<GetOrUpdateUserByAppId200Response>

}
