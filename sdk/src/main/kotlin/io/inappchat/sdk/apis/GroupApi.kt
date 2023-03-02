package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.APIGroup
import io.inappchat.sdk.models.APIThread
import io.inappchat.sdk.models.Invite
import io.inappchat.sdk.models.ModerateGroupInput
import io.inappchat.sdk.models.UpdateGroupInput

import okhttp3.MultipartBody

interface GroupApi {
    /**
     * accept group invitation
     * Accept group invitation
     * Responses:
     *  - 200: the thread the user has joined
     *
     * @param gid Group ID
     * @return [APIThread]
     */
    @POST("group/{gid}/invites/accept")
    suspend fun acceptGroupInvite(@Path("gid") gid: kotlin.String): Response<APIThread>

    /**
     * Add participant to group
     * Add participant to group
     * Responses:
     *  - 204: The operation completed successfully
     *
     * @param gid Group ID
     * @param uid the user&#39;s id
     * @return [Unit]
     */
    @PUT("group/{gid}/participants/{uid}")
    suspend fun addParticipant(@Path("gid") gid: kotlin.String, @Path("uid") uid: kotlin.String): Response<Unit>


    /**
    * enum for parameter groupType
    */
    enum class GroupType_createGroup(val value: kotlin.String) {
        @Json(name = "public") `public`("public"),
        @Json(name = "private") `private`("private")
    }

    /**
     * Create or Update group
     * Create a group. For profilePic use multipart/formdata and in this case stringify participants list.
     * Responses:
     *  - 200: The Group
     *
     * @param name Group Name
     * @param participants List of participants
     * @param groupType Type of group. for example privte/public. only private is supported as of now. (optional)
     * @param description Description of group. Optional. Min length 2. (optional)
     * @param profilePic The image for the group (optional)
     * @return [APIGroup]
     */
    @Multipart
    @POST("groups")
    suspend fun createGroup(@Part("name") name: kotlin.String, @Part("participants") participants: kotlin.collections.List<kotlin.String>, @Part("groupType") groupType: kotlin.String? = null, @Part("description") description: kotlin.String? = null, @Part profilePic: MultipartBody.Part? = null): Response<APIGroup>

    /**
     * 
     * Delete a group
     * Responses:
     *  - 204: The operation completed successfully
     *
     * @param gid Group ID
     * @return [Unit]
     */
    @DELETE("group/{gid}")
    suspend fun deleteGroup(@Path("gid") gid: kotlin.String): Response<Unit>

    /**
     * dismiss group invitation
     * Dissmiss group invitation
     * Responses:
     *  - 204: no content
     *
     * @param gid Group ID
     * @return [Unit]
     */
    @POST("group/{gid}/invites/dismiss")
    suspend fun dismissGroupInvite(@Path("gid") gid: kotlin.String): Response<Unit>

    /**
     * Get group by groupId
     * Get group by groupId
     * Responses:
     *  - 200: The Group
     *
     * @param gid Group ID
     * @return [APIGroup]
     */
    @GET("group/{gid}")
    suspend fun getGroup(@Path("gid") gid: kotlin.String): Response<APIGroup>


    /**
    * enum for parameter groupType
    */
    enum class GroupType_getGroups(val value: kotlin.String) {
        @Json(name = "public") `public`("public"),
        @Json(name = "private") `private`("private")
    }


    /**
    * enum for parameter joined
    */
    enum class Joined_getGroups(val value: kotlin.String) {
        @Json(name = "yes") yes("yes"),
        @Json(name = "no") no("no")
    }

    /**
     * Get user groups
     * Get groups where user is participant or group is public
     * Responses:
     *  - 200: Get group response
     *
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional, default to 20)
     * @param skip skip value for pagination. i.e. index. default 0 (optional, default to 0)
     * @param groupType Filter by group type (optional)
     * @param joined Get only joined/not joined groups (optional)
     * @return [kotlin.collections.List<APIGroup>]
     */
    @GET("groups")
    suspend fun getGroups(@Query("limit") limit: kotlin.Int? = 20, @Query("skip") skip: kotlin.Int? = 0, @Query("groupType") groupType: GroupType_getGroups? = null, @Query("joined") joined: Joined_getGroups? = null): Response<kotlin.collections.List<APIGroup>>

    /**
     * get group invitation
     * Get group invitations for user
     * Responses:
     *  - 200: Group invitation data
     *
     * @return [kotlin.collections.List<Invite>]
     */
    @GET("group/invites")
    suspend fun getInvites(): Response<kotlin.collections.List<Invite>>

    /**
     * Make a user an admin
     * Make a user an admin
     * Responses:
     *  - 204: The operation completed successfully
     *
     * @param uid the user&#39;s id
     * @param gid Group ID
     * @return [Unit]
     */
    @PUT("group/{gid}/admin/{uid}")
    suspend fun groupAddAdmin(@Path("uid") uid: kotlin.String, @Path("gid") gid: kotlin.String): Response<Unit>

    /**
     * Dismiss a group admin
     * Dismiss a group admin
     * Responses:
     *  - 204: The oepration completed successfully
     *
     * @param uid the user&#39;s id
     * @param gid Group ID
     * @return [Unit]
     */
    @DELETE("group/{gid}/admin/{uid}")
    suspend fun groupDismissAdmin(@Path("uid") uid: kotlin.String, @Path("gid") gid: kotlin.String): Response<Unit>

    /**
     * create group invitation
     * Invite new participant to group
     * Responses:
     *  - 204: no content
     *
     * @param gid Group ID
     * @param requestBody array of user ids to invite
     * @return [Unit]
     */
    @POST("group/{gid}/invite")
    suspend fun inviteUser(@Path("gid") gid: kotlin.String, @Body requestBody: kotlin.collections.List<kotlin.String>): Response<Unit>

    /**
     * 
     * Moderate a group. Ban or mute users
     * Responses:
     *  - 204: The operation completed successfully
     *
     * @param gid Group ID
     * @param moderateGroupInput Unique AppID of the user to get
     * @return [Unit]
     */
    @POST("group/{gid}/moderate")
    suspend fun moderateGroup(@Path("gid") gid: kotlin.String, @Body moderateGroupInput: ModerateGroupInput): Response<Unit>

    /**
     * Remove group profile pic
     * Remove group profile pic
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param gid Group ID
     * @return [Unit]
     */
    @DELETE("group/{gid}/image")
    suspend fun removeGroupImage(@Path("gid") gid: kotlin.String): Response<Unit>

    /**
     * Remove participant from group
     * Remove participant from group
     * Responses:
     *  - 204: The operation completed successfully
     *
     * @param gid Group ID
     * @param uid the user&#39;s id
     * @return [Unit]
     */
    @DELETE("group/{gid}/participants/{uid}")
    suspend fun removeParticipant(@Path("gid") gid: kotlin.String, @Path("uid") uid: kotlin.String): Response<Unit>

    /**
     * 
     * Update a group
     * Responses:
     *  - 200: The Group
     *
     * @param gid Group ID
     * @param updateGroupInput 
     * @return [APIGroup]
     */
    @PUT("group/{gid}")
    suspend fun updateGroup(@Path("gid") gid: kotlin.String, @Body updateGroupInput: UpdateGroupInput): Response<APIGroup>

}
