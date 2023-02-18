package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.FcmValidationRequest
import io.inappchat.sdk.models.Thread200Response

interface FCMApi {
    /**
     * FCM Validation
     * Endpoint to just validate FCM notification by App teams
     * Responses:
     *  - 200: Thread data
     *
     * @param version API key
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param body Unique AppID of the user to get
     * @return [Thread200Response]
     */
    @POST("{version}/tenants/{tenantId}/fcmValidation")
    suspend fun fCMValidationPost(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Body body: FcmValidationRequest): Response<Thread200Response>

}
