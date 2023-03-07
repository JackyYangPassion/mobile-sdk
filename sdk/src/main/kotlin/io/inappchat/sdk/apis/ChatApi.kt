package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.APIMessage
import io.inappchat.sdk.models.Contact
import io.inappchat.sdk.models.EncryptedMessage
import io.inappchat.sdk.models.Forward
import io.inappchat.sdk.models.Location
import io.inappchat.sdk.models.Media
import io.inappchat.sdk.models.Mention
import io.inappchat.sdk.models.MessageResponse
import io.inappchat.sdk.models.MessageType
import io.inappchat.sdk.models.Reply
import io.inappchat.sdk.models.UpdateMessageInput

import okhttp3.MultipartBody

interface ChatApi {
    /**
     * Load chat history
     * Clear chat history
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param tid The Thread ID
     * @return [Unit]
     */
    @DELETE("thread/{tid}/messages")
    suspend fun deleteChatHistory(@Path("tid") tid: kotlin.String): Response<Unit>

    /**
     * Delete Message API
     * Delete particular message
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param mid The message ID
     * @return [Unit]
     */
    @DELETE("message/{mid}")
    suspend fun deleteMessage(@Path("mid") mid: kotlin.String): Response<Unit>

    /**
     * 
     * Get users favorite messages
     * Responses:
     *  - 200: The messages
     *
     * @param skip skip value for pagination. i.e. index. default 0 (optional, default to 0)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional, default to 20)
     * @return [kotlin.collections.List<APIMessage>]
     */
    @GET("favorites")
    suspend fun getFavorites(@Query("skip") skip: kotlin.Int? = 0, @Query("limit") limit: kotlin.Int? = 20): Response<kotlin.collections.List<APIMessage>>

    /**
     * 
     * Get a single message
     * Responses:
     *  - 200: The message
     *
     * @param mid The message ID
     * @return [APIMessage]
     */
    @GET("message/{mid}")
    suspend fun getMessage(@Path("mid") mid: kotlin.String): Response<APIMessage>


    /**
    * enum for parameter direction
    */
    enum class Direction_getMessages(val value: kotlin.String) {
        @Json(name = "future") future("future"),
        @Json(name = "past") past("past")
    }

    /**
     * Load chat history
     * List messages in any chat
     * Responses:
     *  - 200: Thread history response
     *
     * @param tid The Thread ID
     * @param msgType Msg type, stringified array Example [\&quot;text\&quot;.\&quot;gif\&quot;] (optional)
     * @param currentMsgId THe message ID to paginate after or before (optional)
     * @param direction future/past (optional, default to past)
     * @param dateFrom ISO string of start date (optional)
     * @param dateTo ISO string of end date (optional)
     * @param pageSize page size for pagination (optional)
     * @param inReplyTo The ID of the message to list replies for (optional)
     * @param deep When true it returns messages from threads and main window both (optional)
     * @return [kotlin.collections.List<APIMessage>]
     */
    @GET("thread/{tid}/messages")
    suspend fun getMessages(@Path("tid") tid: kotlin.String, @Query("msgType") msgType: kotlin.String? = null, @Query("currentMsgId") currentMsgId: kotlin.String? = null, @Query("direction") direction: Direction_getMessages? = Direction_getMessages.past, @Query("dateFrom") dateFrom: kotlin.String? = null, @Query("dateTo") dateTo: kotlin.String? = null, @Query("pageSize") pageSize: kotlin.Int? = null, @Query("inReplyTo") inReplyTo: kotlin.String? = null, @Query("deep") deep: kotlin.Boolean? = null): Response<kotlin.collections.List<APIMessage>>

    /**
     * 
     * Get replies to a message
     * Responses:
     *  - 200: The replies
     *
     * @param mid The message ID
     * @param skip skip value for pagination. i.e. index. default 0 (optional, default to 0)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional, default to 20)
     * @return [kotlin.collections.List<APIMessage>]
     */
    @GET("message/{mid}/replies")
    suspend fun getReplies(@Path("mid") mid: kotlin.String, @Query("skip") skip: kotlin.Int? = 0, @Query("limit") limit: kotlin.Int? = 20): Response<kotlin.collections.List<APIMessage>>


    /**
    * enum for parameter direction
    */
    enum class Direction_getReplyThreads(val value: kotlin.String) {
        @Json(name = "future") future("future"),
        @Json(name = "past") past("past")
    }

    /**
     * List reply threads
     * List messages with reply threads
     * Responses:
     *  - 200: Reply thread history response
     *
     * @param threadId Thread ID (optional)
     * @param follow To get all threads user following, just send true (optional)
     * @param starred To get all starred messages, just send true (optional)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional, default to 20)
     * @param skip skip value for pagination. i.e. index. default 0 (optional, default to 0)
     * @param direction future/past (optional, default to past)
     * @param deep When true it returns messages from threads and main window both (optional, default to true)
     * @return [kotlin.collections.List<APIMessage>]
     */
    @GET("reply-threads")
    suspend fun getReplyThreads(@Query("threadId") threadId: kotlin.String? = null, @Query("follow") follow: kotlin.Boolean? = null, @Query("starred") starred: kotlin.Boolean? = null, @Query("limit") limit: kotlin.Int? = 20, @Query("skip") skip: kotlin.Int? = 0, @Query("direction") direction: Direction_getReplyThreads? = Direction_getReplyThreads.past, @Query("deep") deep: kotlin.Boolean? = true): Response<kotlin.collections.List<APIMessage>>

    /**
     * Chat Reaction API
     * Send message reaction
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param mid The message ID
     * @param emoji The emoji to react with
     * @return [Unit]
     */
    @PUT("message/{mid}/reactions/{emoji}")
    suspend fun react(@Path("mid") mid: kotlin.String, @Path("emoji") emoji: kotlin.String): Response<Unit>

    /**
     * Send a chat message
     * Send a chat message
     * Responses:
     *  - 200: The message
     *
     * @param deviceid Source device ID
     * @param senderTimeStampMs epoch timestamp (in ms) of message creation generated on sender device
     * @param threadId Thread ID which represents a user or a group. eg. 5c56c9a2218aec4b4a8a976f. This is mutually exclusive with recipientAppUserId. (optional)
     * @param recipientAppUserId App user Id of receiver. eg. abc@def.com. This is mutually exclusive with threadId. (optional)
     * @param message message text. rg. &#39;hello&#39; (optional)
     * @param msgType  (optional)
     * @param file File share (optional)
     * @param replyThreadFeatureData  (optional)
     * @param location  (optional)
     * @param contact  (optional)
     * @param gif gify url (optional)
     * @param mentions  (optional)
     * @param forwardChatFeatureData  (optional)
     * @param media  (optional)
     * @param msgCorrelationId Client generated unique identifier used to trace message delivery till receiver (optional)
     * @param encryptedChatList List of user+device wise eencrypted chat objects. (optional)
     * @return [MessageResponse]
     */
    @Multipart
    @POST("message")
    suspend fun sendMessage(@Header("deviceid") deviceid: kotlin.String, @Part("senderTimeStampMs") senderTimeStampMs: java.math.BigDecimal, @Part("threadId") threadId: kotlin.String? = null, @Part("recipientAppUserId") recipientAppUserId: kotlin.String? = null, @Part("message") message: kotlin.String? = null, @Part("msgType") msgType: MessageType? = null, @Part file: MultipartBody.Part? = null, @Part("replyThreadFeatureData") replyThreadFeatureData: Reply? = null, @Part("location") location: Location? = null, @Part("contact") contact: Contact? = null, @Part("gif") gif: kotlin.String? = null, @Part("mentions") mentions: kotlin.collections.List<Mention>? = null, @Part("forwardChatFeatureData") forwardChatFeatureData: Forward? = null, @Part("media") media: Media? = null, @Part("msgCorrelationId") msgCorrelationId: kotlin.String? = null, @Part("encryptedChatList") encryptedChatList: kotlin.collections.List<EncryptedMessage>? = null): Response<MessageResponse>

    /**
     * 
     * Remove a message reaction
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param mid The message ID
     * @param emoji The emoji to react with
     * @return [Unit]
     */
    @DELETE("message/{mid}/reactions/{emoji}")
    suspend fun unreact(@Path("mid") mid: kotlin.String, @Path("emoji") emoji: kotlin.String): Response<Unit>

    /**
     * Edit Message API
     * Edit particular message
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param mid The message ID
     * @param updateMessageInput edit chat body
     * @return [Unit]
     */
    @PUT("message/{mid}")
    suspend fun updateMessage(@Path("mid") mid: kotlin.String, @Body updateMessageInput: UpdateMessageInput): Response<Unit>

}
