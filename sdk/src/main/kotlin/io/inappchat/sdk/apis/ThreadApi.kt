package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.Thread200Response
import io.inappchat.sdk.models.ThreadHistoryGet200Response
import io.inappchat.sdk.models.ThreadHistoryGetV2200Response
import io.inappchat.sdk.models.ThreadRequest
import io.inappchat.sdk.models.ThreadUpdateRequest

interface ThreadApi {
    /**
     * Thread Creation API
     * Get or Create Thread request before starting chat session with any user. This API is applicable for only one 2 one chat.
     * Responses:
     *  - 200: Thread data
     *
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param body Unique AppID of the user to get
     * @return [Thread200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/thread/")
    suspend fun thread(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Body body: ThreadRequest): Response<Thread200Response>

    /**
     * Thread Get API
     * Get any existing thread
     * Responses:
     *  - 200: Thread data
     *
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param threadId Thread ID
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @return [Thread200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/thread/{threadId}")
    suspend fun threadGet(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("threadId") threadId: kotlin.String, @Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String): Response<Thread200Response>

    /**
     * Load thread history
     * Load thread history
     * Responses:
     *  - 200: Thread history response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @param skip skip value for pagination. i.e. index. default 0 (optional)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional)
     * @param threadType threadType in-case specific type threads are needed. supported values single/group. Don&#39;t provide this field if all threads to be returned in unified way. (optional)
     * @return [ThreadHistoryGet200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/thread/history")
    suspend fun threadHistoryGet(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Query("skip") skip: kotlin.String? = null, @Query("limit") limit: kotlin.String? = null, @Query("threadType") threadType: kotlin.String? = null): Response<ThreadHistoryGet200Response>

    /**
     * Load thread history
     * Load thread history
     * Responses:
     *  - 200: Thread history response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @param skip skip value for pagination. i.e. index. default 0 (optional)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional)
     * @param threadType threadType in-case specific type threads are needed. supported values single/group. Don&#39;t provide this field if all threads to be returned in unified way. (optional)
     * @return [ThreadHistoryGetV2200Response]
     */
    @GET("V2/tenants/{tenantId}/{eRTCUserId}/thread/history")
    suspend fun threadHistoryGetV2(@Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Query("skip") skip: kotlin.String? = null, @Query("limit") limit: kotlin.String? = null, @Query("threadType") threadType: kotlin.String? = null): Response<ThreadHistoryGetV2200Response>

    /**
     * Thread Update API
     * Update any existing thread
     * Responses:
     *  - 200: Thread data
     *
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param threadId Thread ID
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param body Unique AppID of the user to get
     * @return [Thread200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/thread/{threadId}")
    suspend fun threadUpdatePost(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("threadId") threadId: kotlin.String, @Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Body body: ThreadUpdateRequest): Response<Thread200Response>

}
