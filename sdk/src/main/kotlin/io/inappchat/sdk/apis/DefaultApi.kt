package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.APIMessage
import io.inappchat.sdk.models.MessageType
import io.inappchat.sdk.models.Stub

interface DefaultApi {

    /**
    * enum for parameter direction
    */
    enum class Direction_getUserMessages(val value: kotlin.String) {
        @Json(name = "future") future("future"),
        @Json(name = "past") past("past")
    }

    /**
     * 
     * 
     * Responses:
     *  - 200: The users recent messages
     *
     * @param uid the user&#39;s id
     * @param skip skip value for pagination. i.e. index. default 0 (optional, default to 0)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional, default to 20)
     * @param msgType Filters message by (optional)
     * @param direction future/past (optional, default to past)
     * @return [kotlin.collections.List<APIMessage>]
     */
    @GET("user/{uid}/messages")
    suspend fun getUserMessages(@Path("uid") uid: kotlin.String, @Query("skip") skip: kotlin.Int? = 0, @Query("limit") limit: kotlin.Int? = 20, @Query("msgType") msgType: MessageType? = null, @Query("direction") direction: Direction_getUserMessages? = Direction_getUserMessages.past): Response<kotlin.collections.List<APIMessage>>

    /**
     * 
     * This api does not exist. This is only to generate types that wouldnt have otherwise been generated
     * Responses:
     *  - 200: Stub API
     *
     * @return [Stub]
     */
    @GET("stub")
    suspend fun stub(): Response<Stub>

}
