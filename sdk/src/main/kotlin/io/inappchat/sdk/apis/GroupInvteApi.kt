package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.VersionTenantsTenantIdERTCUserIdGroupInvitesGet200Response

interface GroupInvteApi {
    /**
     * create group invitation
     * Invite new participant to group
     * Responses:
     *  - 204: no content
     *
     * @param version API version
     * @param tenantId Tenant ID
     * @param eRTCUserId user mongo ID
     * @param groupId group ID
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @param body array of eRTCUserIds of invitees
     * @return [Unit]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/invite")
    suspend fun versionTenantsTenantIdERTCUserIdGroupGroupIdInvitePost(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("groupId") groupId: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String, @Body body: kotlin.collections.List<kotlin.String>): Response<Unit>

    /**
     * accept group invitation
     * Accept group invitation
     * Responses:
     *  - 204: no content
     *
     * @param version API version
     * @param tenantId Tenant ID
     * @param eRTCUserId user mongo ID
     * @param groupId group ID
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @return [Unit]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/invites/accept")
    suspend fun versionTenantsTenantIdERTCUserIdGroupGroupIdInvitesAcceptPost(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("groupId") groupId: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String): Response<Unit>

    /**
     * dismiss group invitation
     * Dissmiss group invitation
     * Responses:
     *  - 204: no content
     *
     * @param version API version
     * @param tenantId Tenant ID
     * @param eRTCUserId user mongo ID
     * @param groupId group ID
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @return [Unit]
     */
    @POST("{version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/invites/dismiss")
    suspend fun versionTenantsTenantIdERTCUserIdGroupGroupIdInvitesDismissPost(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Path("groupId") groupId: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String): Response<Unit>

    /**
     * get group invitation
     * Get group invitations for user
     * Responses:
     *  - 200: Group invitation data
     *
     * @param version API version
     * @param tenantId Tenant ID
     * @param eRTCUserId user mongo ID
     * @param xRequestSignature sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt;
     * @param xNonce epoch timestamp
     * @return [VersionTenantsTenantIdERTCUserIdGroupInvitesGet200Response]
     */
    @GET("{version}/tenants/{tenantId}/{eRTCUserId}/group/invites")
    suspend fun versionTenantsTenantIdERTCUserIdGroupInvitesGet(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Path("eRTCUserId") eRTCUserId: kotlin.String, @Header("X-Request-Signature") xRequestSignature: kotlin.String, @Header("X-nonce") xNonce: kotlin.String): Response<VersionTenantsTenantIdERTCUserIdGroupInvitesGet200Response>

}
