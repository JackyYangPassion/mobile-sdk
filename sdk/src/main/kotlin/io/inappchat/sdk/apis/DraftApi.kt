package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.GetDrafts200Response

import okhttp3.MultipartBody

interface DraftApi {
    /**
     * Draft API
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
     * @param metadata JSON object which can be used for client reference in request/response context. Server will not do any processing on this object. eg. { \\\&quot;abc\\\&quot; : \\\&quot;def\\\&quot; } (optional)
     * @param location JSON object of location. check locationSchema below (optional)
     * @param contact JSON object of contact. check contactSchema below (optional)
     * @param gify gify url (optional)
     * @param mentions User mentions list. Please check mentionsSchemaChatRequest below. For example, [{\\\&quot;type\\\&quot;:\\\&quot;user\\\&quot;,\\\&quot;value\\\&quot;:\\\&quot;5c56c9a2218aec4b4a8a976f\\\&quot;}, {\\\&quot;type\\\&quot;:\\\&quot;generic\\\&quot;,\\\&quot;value\\\&quot;:\\\&quot;channel\\\&quot;] (optional)
     * @param media This is only allowed in forward messages. i.e. if forwardChatFeatureData exists. JSON object of Details in-case it is a forward chat. eg.{\\\&quot;path\\\&quot; : \\\&quot;file/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\&quot;,\\\&quot;name\\\&quot; : \\\&quot;WhatsApp Image 2020-08-25 at 2.36.35 PM.jpeg\\\&quot;,\\\&quot;thumbnail\\\&quot; : \\\&quot;file/imageThumbnail/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\&quot;}. Please check mediaSchema in models (optional)
     * @param msgCorrelationId Client generated unique identifier used to trace message delivery till receiver (optional)
     * @param customData JSON object which can be used for customer specific data which is not supported in InAppChat chat model. eg. { \\\&quot;abc\\\&quot; : \\\&quot;def\\\&quot; } (optional)
     * @return [GetDrafts200Response]
     */
    @Multipart
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/draft/")
    suspend fun draftUpdate(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Header("deviceid") deviceid: kotlin.String, @Part("sendereRTCUserId") sendereRTCUserId: kotlin.String, @Part("senderTimeStampMs") senderTimeStampMs: java.math.BigDecimal, @Part("threadId") threadId: kotlin.String? = null, @Part("recipientAppUserId") recipientAppUserId: kotlin.String? = null, @Part("message") message: kotlin.String? = null, @Part("msgType") msgType: kotlin.String? = null, @Part file: MultipartBody.Part? = null, @Part("metadata") metadata: kotlin.String? = null, @Part("location") location: kotlin.String? = null, @Part("contact") contact: kotlin.String? = null, @Part("gify") gify: kotlin.String? = null, @Part("mentions") mentions: kotlin.String? = null, @Part("media") media: kotlin.String? = null, @Part("msgCorrelationId") msgCorrelationId: kotlin.String? = null, @Part("customData") customData: kotlin.String? = null): Response<GetDrafts200Response>

    /**
     * Draft API
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
     * @param threadId Get draft for a particular thread (optional)
     * @param baseMsgUniqueId Get thread for a reply thread, this is base message id (optional)
     * @return [GetDrafts200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/draft/")
    suspend fun getDrafts(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("Authorization") authorization: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Header("deviceid") deviceid: kotlin.String, @Query("threadId") threadId: kotlin.String? = null, @Query("baseMsgUniqueId") baseMsgUniqueId: kotlin.String? = null): Response<GetDrafts200Response>

}
