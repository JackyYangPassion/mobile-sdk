package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.ChatReportCreateRequest
import io.inappchat.sdk.models.ChatReportUpdateRequest
import io.inappchat.sdk.models.CreateChatReportPost200Response
import io.inappchat.sdk.models.DeleteChatReportDelete200Response
import io.inappchat.sdk.models.GetChatReportGet200Response
import io.inappchat.sdk.models.GetChatReportList200Response
import io.inappchat.sdk.models.UpdateChatReportActionPut200Response

interface ChatReportApi {
    /**
     * Create Chat Report
     * Create Chat Report.
     * Responses:
     *  - 200: Create Chat Report Response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @param body  (optional)
     * @return [CreateChatReportPost200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/chatReports")
    suspend fun createChatReportPost(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Body body: ChatReportCreateRequest? = null): Response<CreateChatReportPost200Response>

    /**
     * Delete Chat Report
     * Delete Chat Report.
     * Responses:
     *  - 200: Delete Chat Report Response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param chatReportId chat Report ID
     * @param xNonce epoch timestamp
     * @return [DeleteChatReportDelete200Response]
     */
    @DELETE("{version}/tenants/{tenantId}/{eRTCUserId}/chatReports/{chatReportId}")
    suspend fun deleteChatReportDelete(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("chatReportId") chatReportId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String): Response<DeleteChatReportDelete200Response>

    /**
     * Get Chat Report Details
     * Get Chat Report Details.
     * Responses:
     *  - 200: Update Chat Report Response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param chatReportId chat Report ID
     * @param xNonce epoch timestamp
     * @return [GetChatReportGet200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/chatReports/{chatReportId}")
    suspend fun getChatReportGet(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("chatReportId") chatReportId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String): Response<GetChatReportGet200Response>

    /**
     * Get Chat Report List
     * Get Chat Report List.
     * Responses:
     *  - 200: Get Chat Report List Response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @param skip skip value for pagination. i.e. index. default 0 (optional)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional)
     * @param threadId thread ID to filter chat Reports (optional)
     * @param category chat report category to filter chat Reports(Possible values : abuse,spam,other,inappropriate) (optional)
     * @param status chat report status to filter chat Reports(Possible values : new, reportConsidered, reportIgnored) (optional)
     * @param msgType chat report msgType to filter chat Reports(Possible values : text, image, audio, video, file, gif, location, contact, sticker, gify) (optional)
     * @return [GetChatReportList200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/chatReports")
    suspend fun getChatReportList(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Query("skip") skip: kotlin.String? = null, @Query("limit") limit: kotlin.String? = null, @Query("threadId") threadId: kotlin.String? = null, @Query("category") category: kotlin.String? = null, @Query("status") status: kotlin.String? = null, @Query("msgType") msgType: kotlin.String? = null): Response<GetChatReportList200Response>

    /**
     * Update Chat Report Action
     * Update Chat Report Action.
     * Responses:
     *  - 200: Update Chat Report Response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param chatReportId chat Report ID
     * @param action action for report (possible values : reportConsidered,reportedIgnored)
     * @param xNonce epoch timestamp
     * @return [UpdateChatReportActionPut200Response]
     */
    @PUT("{version}/tenants/{tenantId}/{eRTCUserId}/chatReports/{chatReportId}/allowIgnore/{action}")
    suspend fun updateChatReportActionPut(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("chatReportId") chatReportId: kotlin.String, @Path("action") action: kotlin.String, @Header("X-nonce") xNonce: kotlin.String): Response<UpdateChatReportActionPut200Response>

    /**
     * Update Chat Report
     * Update Chat Report.
     * Responses:
     *  - 200: Update Chat Report Response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param chatReportId chat Report ID
     * @param xNonce epoch timestamp
     * @param body  (optional)
     * @return [CreateChatReportPost200Response]
     */
    @PUT("{version}/tenants/{tenantId}/{eRTCUserId}/chatReports/{chatReportId}")
    suspend fun updateChatReportPut(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("chatReportId") chatReportId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Body body: ChatReportUpdateRequest? = null): Response<CreateChatReportPost200Response>

}
