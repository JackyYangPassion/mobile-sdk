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
import io.inappchat.sdk.models.MessageType
import io.inappchat.sdk.models.Reply

import okhttp3.MultipartBody

interface DraftApi {
    /**
     * Draft API
     * Send chat over a thread
     * Responses:
     *  - 200: The draft
     *  - 204: No draft
     *
     * @param threadId Get draft for a particular thread (optional)
     * @param baseMsgUniqueId Get thread for a reply thread, this is base message id (optional)
     * @return [APIMessage]
     */
    @GET("draft")
    suspend fun getDrafts(@Query("threadId") threadId: kotlin.String? = null, @Query("baseMsgUniqueId") baseMsgUniqueId: kotlin.String? = null): Response<APIMessage>

    /**
     * Draft API
     * Send chat over a thread
     * Responses:
     *  - 200: The message
     *
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
     * @return [APIMessage]
     */
    @Multipart
    @POST("draft")
    suspend fun updateDraft(@Part("senderTimeStampMs") senderTimeStampMs: java.math.BigDecimal, @Part("threadId") threadId: kotlin.String? = null, @Part("recipientAppUserId") recipientAppUserId: kotlin.String? = null, @Part("message") message: kotlin.String? = null, @Part("msgType") msgType: MessageType? = null, @Part file: MultipartBody.Part? = null, @Part("replyThreadFeatureData") replyThreadFeatureData: Reply? = null, @Part("location") location: Location? = null, @Part("contact") contact: Contact? = null, @Part("gif") gif: kotlin.String? = null, @Part("mentions") mentions: kotlin.collections.List<Mention>? = null, @Part("forwardChatFeatureData") forwardChatFeatureData: Forward? = null, @Part("media") media: Media? = null, @Part("msgCorrelationId") msgCorrelationId: kotlin.String? = null, @Part("encryptedChatList") encryptedChatList: kotlin.collections.List<EncryptedMessage>? = null): Response<APIMessage>

}
