package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response

import io.inappchat.sdk.models.Chat200Response
import io.inappchat.sdk.models.ChatHistoryGet200Response
import io.inappchat.sdk.models.ChatMultiplePost200Response
import io.inappchat.sdk.models.ChatReactionPost200Response
import io.inappchat.sdk.models.ChatReactionRequest
import io.inappchat.sdk.models.ChatRequestObj
import io.inappchat.sdk.models.ChatSearchPost200Response
import io.inappchat.sdk.models.ChatSearchRequest
import io.inappchat.sdk.models.ClearChatHistory200Response
import io.inappchat.sdk.models.DeleteChat200Response
import io.inappchat.sdk.models.DeleteChatRequestObj
import io.inappchat.sdk.models.E2eEncryptedChatRequestObj
import io.inappchat.sdk.models.EditChatPut200Response
import io.inappchat.sdk.models.EditChatRequestObj
import io.inappchat.sdk.models.GetReplyThreads200Response

import okhttp3.MultipartBody

interface ChatApi {
    /**
     * Load chat history
     * Load Chat history on any conversation thread
     * Responses:
     *  - 200: Thread history response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @param msgType Msg type, stringified array Example [\&quot;text\&quot;.\&quot;gif\&quot;] (optional)
     * @param currentMsgId message Id. Example 5fabd417f9e67f996ce84140 (optional)
     * @param direction future/past (optional)
     * @param dateFrom ISO string of start date (optional)
     * @param dateTo ISO string of end date (optional)
     * @param pageSize page size for pagination (optional)
     * @param baseMsgUniqueId Base message id in-case reply thread restoration is happening. Example 6005dcbda026a25c2ce5f9b5 (optional)
     * @return [ChatHistoryGet200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/chat/history")
    suspend fun allChatHistoryGet(
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String,
        @Query("msgType") msgType: kotlin.String? = null,
        @Query("currentMsgId") currentMsgId: kotlin.String? = null,
        @Query("direction") direction: kotlin.String? = null,
        @Query("dateFrom") dateFrom: kotlin.String? = null,
        @Query("dateTo") dateTo: kotlin.String? = null,
        @Query("pageSize") pageSize: kotlin.String? = null,
        @Query("baseMsgUniqueId") baseMsgUniqueId: kotlin.String? = null
    ): Response<ChatHistoryGet200Response>

    /**
     * Chat API
     * Send chat over a thread
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
     * @param sendereRTCUserId eRTC user Id of sender. eg. 5c56c9a2218aec4b4a8a976e
     * @param senderTimeStampMs epoch timestamp (in ms) of message creation generated on sender device
     * @param threadId Thread ID which represents a user or a group. eg. 5c56c9a2218aec4b4a8a976f. This is exclusive peer to recipientAppUserId. (optional)
     * @param recipientAppUserId App user Id of receiver. eg. abc@def.com. This is exclusive peer to threadId. (optional)
     * @param message message text. rg. &#39;hello&#39; (optional)
     * @param msgType message type. Required only for text/contact/location message. Other supported values are gif/sticker (optional)
     * @param file Media chat file (optional)
     * @param replyThreadFeatureData JSON object of Details in-case it is a reply on another chat. eg. { \\\&quot;baseMsgUniqueId\\\&quot;: \\\&quot;5e3841dea49618da0b379480\\\&quot;, \\\&quot;replyMsgConfig\\\&quot;: 1 }. Please check replyThreadSchemaChatRequest in models (optional)
     * @param metadata JSON object which can be used for client reference in request/response context. Server will not do any processing on this object. eg. { \\\&quot;abc\\\&quot; : \\\&quot;def\\\&quot; } (optional)
     * @param location JSON object of location. check locationSchema below (optional)
     * @param contact JSON object of contact. check contactSchema below (optional)
     * @param gify gify url (optional)
     * @param mentions User mentions list. Please check mentionsSchemaChatRequest below. For example, [{\\\&quot;type\\\&quot;:\\\&quot;user\\\&quot;,\\\&quot;value\\\&quot;:\\\&quot;5c56c9a2218aec4b4a8a976f\\\&quot;}, {\\\&quot;type\\\&quot;:\\\&quot;generic\\\&quot;,\\\&quot;value\\\&quot;:\\\&quot;channel\\\&quot;] (optional)
     * @param forwardChatFeatureData JSON object of Details in-case it is a forward chat. eg. { \\\&quot;originalMsgUniqueId\\\&quot;: \\\&quot;5e3841dea49618da0b379480\\\&quot;, \\\&quot;isForwarded\\\&quot;: true }. Please check forwardChatSchemaChatRequest in models (optional)
     * @param media This is only allowed in forward messages. i.e. if forwardChatFeatureData exists. JSON object of Details in-case it is a forward chat. eg.{\\\&quot;path\\\&quot; : \\\&quot;file/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\&quot;,\\\&quot;name\\\&quot; : \\\&quot;WhatsApp Image 2020-08-25 at 2.36.35 PM.jpeg\\\&quot;,\\\&quot;thumbnail\\\&quot; : \\\&quot;file/imageThumbnail/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\&quot;}. Please check mediaSchema in models (optional)
     * @param msgCorrelationId Client generated unique identifier used to trace message delivery till receiver (optional)
     * @param customData JSON object which can be used for customer specific data which is not supported in InAppChat chat model. eg. { \\\&quot;abc\\\&quot; : \\\&quot;def\\\&quot; } (optional)
     * @return [Chat200Response]
     */
    @Multipart
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/chat/")
    suspend fun chat(
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String,
        @Header("deviceid") deviceid: kotlin.String,
        @Part("sendereRTCUserId") sendereRTCUserId: kotlin.String,
        @Part("senderTimeStampMs") senderTimeStampMs: java.math.BigDecimal,
        @Part("threadId") threadId: kotlin.String? = null,
        @Part("recipientAppUserId") recipientAppUserId: kotlin.String? = null,
        @Part("message") message: kotlin.String? = null,
        @Part("msgType") msgType: kotlin.String? = null,
        @Part file: MultipartBody.Part? = null,
        @Part("replyThreadFeatureData") replyThreadFeatureData: kotlin.String? = null,
        @Part("metadata") metadata: kotlin.String? = null,
        @Part("location") location: kotlin.String? = null,
        @Part("contact") contact: kotlin.String? = null,
        @Part("gify") gify: kotlin.String? = null,
        @Part("mentions") mentions: kotlin.String? = null,
        @Part("forwardChatFeatureData") forwardChatFeatureData: kotlin.String? = null,
        @Part("media") media: kotlin.String? = null,
        @Part("msgCorrelationId") msgCorrelationId: kotlin.String? = null,
        @Part("customData") customData: kotlin.String? = null
    ): Response<Chat200Response>

    /**
     * Load chat history
     * Load Chat history on any conversation thread
     * Responses:
     *  - 200: Thread history response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param threadId Thread ID
     * @param xNonce epoch timestamp
     * @param msgType Msg type, stringified array Example [\&quot;text\&quot;.\&quot;gif\&quot;] (optional)
     * @param currentMsgId message Id. Example 5fabd417f9e67f996ce84140 (optional)
     * @param direction future/past (optional)
     * @param dateFrom ISO string of start date (optional)
     * @param dateTo ISO string of end date (optional)
     * @param pageSize page size for pagination (optional)
     * @param baseMsgUniqueId Base message id in-case reply thread restoration is happening. Example 6005dcbda026a25c2ce5f9b5 (optional)
     * @param deep When true it returns messages from threads and main window both (optional)
     * @return [ChatHistoryGet200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/chat/{threadId}/history")
    suspend fun chatHistoryGet(
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Path("threadId") threadId: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String,
        @Query("msgType") msgType: kotlin.String? = null,
        @Query("currentMsgId") currentMsgId: kotlin.String? = null,
        @Query("direction") direction: kotlin.String? = null,
        @Query("dateFrom") dateFrom: kotlin.String? = null,
        @Query("dateTo") dateTo: kotlin.String? = null,
        @Query("pageSize") pageSize: kotlin.String? = null,
        @Query("baseMsgUniqueId") baseMsgUniqueId: kotlin.String? = null,
        @Query("deep") deep: kotlin.Boolean? = null
    ): Response<ChatHistoryGet200Response>

    /**
     * Chat API
     * Send multiple text chats
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
     * @param context type of sync i.e. offlineDispatch/forwardChat. default value is offlineDispatch. (optional)
     * @return [ChatMultiplePost200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/chat/multiple")
    suspend fun chatMultiplePost(
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String,
        @Header("deviceid") deviceid: kotlin.String,
        @Body body: kotlin.collections.List<ChatRequestObj>,
        @Query("context") context: kotlin.String? = null
    ): Response<ChatMultiplePost200Response>

    /**
     * Chat Reaction API
     * Send chat reaction
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
     * @return [ChatReactionPost200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/chat/reaction")
    suspend fun chatReactionPost(
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String,
        @Header("deviceid") deviceid: kotlin.String,
        @Body body: ChatReactionRequest
    ): Response<ChatReactionPost200Response>

    /**
     * Chat Search API
     * Chat Search API
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
     * @return [ChatSearchPost200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/chat/search")
    suspend fun chatSearchPost(
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String,
        @Header("deviceid") deviceid: kotlin.String,
        @Body body: ChatSearchRequest,
        @Query("skip") skip: kotlin.String? = null,
        @Query("limit") limit: kotlin.String? = null
    ): Response<ChatSearchPost200Response>

    /**
     * Load chat history
     * Clear chat history
     * Responses:
     *  - 200: Clear chat history response
     *
     * @param authorization Authorization Token
     * @param threadId Thread ID
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @return [ClearChatHistory200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/chat/{threadId}/history/clear")
    suspend fun clearChatHistory(
        @Header("Authorization") authorization: kotlin.String,
        @Path("threadId") threadId: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String
    ): Response<ClearChatHistory200Response>

    /**
     * Delete Chat API
     * Delete particular chat
     * Responses:
     *  - 200: deleted chat msgUniqueId
     *
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param deviceid Source device ID
     * @param body delete chat body
     * @return [DeleteChat200Response]
     */
    @DELETE("{version}/tenants/{tenantId}/{eRTCUserId}/chat/")
    suspend fun deleteChat(
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String,
        @Header("deviceid") deviceid: kotlin.String,
        @Body body: DeleteChatRequestObj
    ): Response<DeleteChat200Response>

    /**
     * e2e Encrypted Chat API
     * Send e2e encrypted chat over a thread
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
     * @param body e2e Encrypted Cha
     * @return [Chat200Response]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/chat/e2eEncrypted")
    suspend fun e2eEncryptionChat(
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String,
        @Header("deviceid") deviceid: kotlin.String,
        @Body body: E2eEncryptedChatRequestObj
    ): Response<Chat200Response>

    /**
     * Edit Chat API
     * Edit particular chat
     * Responses:
     *  - 200: deleted chat msgUniqueId
     *
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param deviceid Source device ID
     * @param body delete chat body
     * @return [EditChatPut200Response]
     */
    @PUT("{version}/tenants/{tenantId}/{eRTCUserId}/chat/")
    suspend fun editChatPut(
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String,
        @Header("deviceid") deviceid: kotlin.String,
        @Body body: EditChatRequestObj
    ): Response<EditChatPut200Response>

    /**
     * Load chat history
     * Load Chat history on any conversation thread
     * Responses:
     *  - 200: Reply thread history response
     *
     * @param authorization Authorization Token
     * @param xRequestSignature sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param version API version
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param eRTCUserId eRTC user ID
     * @param xNonce epoch timestamp
     * @param threadId Thread ID (optional)
     * @param follow To get all threads user following, just send true (optional)
     * @param starred To get all starred messages, just send true (optional)
     * @param msgType Msg type, stringified array Example [\&quot;text\&quot;.\&quot;gif\&quot;] (optional)
     * @param currentMsgId message Id. Example 5fabd417f9e67f996ce84140 (optional)
     * @param direction future/past (optional)
     * @param pageSize page size for pagination (optional)
     * @param baseMsgUniqueId Base message id in-case reply thread restoration is happening. Example 6005dcbda026a25c2ce5f9b5 (optional)
     * @param deep When true it returns messages from threads and main window both (optional)
     * @return [GetReplyThreads200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/chat/replyThread/history")
    suspend fun getReplyThreads(
        @Header("Authorization") authorization: kotlin.String,
        @Header("X-Request-Signature") xRequestSignature: kotlin.String,
        @Path("version") version: kotlin.String,
        @Path("tenantId") tenantId: kotlin.String,
        @Path("eRTCUserId") eRTCUserId: kotlin.String,
        @Header("X-nonce") xNonce: kotlin.String,
        @Query("threadId") threadId: kotlin.String? = null,
        @Query("follow") follow: kotlin.Boolean? = null,
        @Query("starred") starred: kotlin.Boolean? = null,
        @Query("msgType") msgType: kotlin.String? = null,
        @Query("currentMsgId") currentMsgId: kotlin.String? = null,
        @Query("direction") direction: kotlin.String? = null,
        @Query("pageSize") pageSize: kotlin.String? = null,
        @Query("baseMsgUniqueId") baseMsgUniqueId: kotlin.String? = null,
        @Query("deep") deep: kotlin.Boolean? = null
    ): Response<GetReplyThreads200Response>

}
