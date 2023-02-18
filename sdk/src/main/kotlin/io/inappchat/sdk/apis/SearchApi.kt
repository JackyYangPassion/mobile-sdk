package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.UnifiedSearch200Response
import io.inappchat.sdk.models.UnifiedSearchRequest

interface SearchApi {
    /**
     * Unified search API
     * One stop for all search APIs, can be used to search files, messages or groups.
     * Responses:
     *  - 200: ERTC User data
     *
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param deviceid Source device ID
     * @param body Chat multiple request
     * @param skip skip value for pagination. i.e. index. default 0 (optional)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional)
     * @return [UnifiedSearch200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/search")
    suspend fun unifiedSearch(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Header("deviceid") deviceid: kotlin.String, @Body body: UnifiedSearchRequest, @Query("skip") skip: kotlin.String? = null, @Query("limit") limit: kotlin.String? = null): Response<UnifiedSearch200Response>

}
