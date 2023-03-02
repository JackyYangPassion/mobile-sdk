package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.ChatSettings

interface ChatSettingApi {
    /**
     * Get chat settings that contains profanity and domain filters
     * Get profanity and domain filter.
     * Responses:
     *  - 200: Get chatSettings response
     *
     * @return [ChatSettings]
     */
    @GET("settings")
    suspend fun getSettings(): Response<ChatSettings>

}
