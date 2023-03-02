package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.APIThread
import io.inappchat.sdk.models.FCMValidationInput

interface FCMApi {
    /**
     * FCM Validation
     * Endpoint to just validate FCM notification by App teams
     * Responses:
     *  - 200: Thread data
     *
     * @param fcMValidationInput Unique AppID of the user to get
     * @return [APIThread]
     */
    @POST("fcmValidation")
    suspend fun fCMValidationPost(@Body fcMValidationInput: FCMValidationInput): Response<APIThread>

}
