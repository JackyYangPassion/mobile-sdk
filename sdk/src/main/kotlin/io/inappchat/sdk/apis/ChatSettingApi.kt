package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.GetChatSettings200Response

interface ChatSettingApi {
    /**
     * Get chat settings that contains profanity and domain filters
     * Get profanity and domain filter.
     * Responses:
     *  - 200: Get chatSettings response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 60a4fc8103a6f047ca02a1df
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @return [GetChatSettings200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/chatSettings")
    suspend fun getChatSettings(
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String
    ): Response<GetChatSettings200Response>

}
