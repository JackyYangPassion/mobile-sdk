package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.APIUser
import io.inappchat.sdk.models.Event
import io.inappchat.sdk.models.SyncContactsInput
import io.inappchat.sdk.models.Token
import io.inappchat.sdk.models.UpdateUserInput

interface UserApi {
    /**
     * Block a user
     * Block a user
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param uid the user&#39;s id
     * @return [Unit]
     */
    @PUT("blocks/{uid}")
    suspend fun blockUser(@Path("uid") uid: kotlin.String): Response<Unit>

    /**
     * 
     * Remove user profile pic
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param uid the user&#39;s id
     * @return [Unit]
     */
    @DELETE("users/{uid}/avatar")
    suspend fun deleteUserAvatar(@Path("uid") uid: kotlin.String): Response<Unit>

    /**
     * Get blocked users
     * Get blocked users
     * Responses:
     *  - 200: Blocked user list
     *
     * @return [kotlin.collections.List<APIUser>]
     */
    @GET("blocks")
    suspend fun getBlockedUsers(): Response<kotlin.collections.List<APIUser>>

    /**
     * 
     * Get pending events for particular device
     * Responses:
     *  - 200: Pending event list
     *
     * @return [kotlin.collections.List<Event>]
     */
    @GET("events")
    suspend fun getPendingEvents(): Response<kotlin.collections.List<Event>>

    /**
     * 
     * Get a user
     * Responses:
     *  - 200: The user
     *
     * @param uid the user&#39;s id
     * @return [APIUser]
     */
    @GET("user/{uid}")
    suspend fun getUser(@Path("uid") uid: kotlin.String): Response<APIUser>

    /**
     * 
     * List users
     * Responses:
     *  - 200: List of Users
     *
     * @param lastId To be used for Pagination (optional)
     * @param lastCallTime epoch time value for time based sunc. Do not pass this param itself for retrieving all data. (optional)
     * @param updateType type of sync i.e. addUpdated/inactive/deleted. Default value is addUpdated (optional)
     * @return [kotlin.collections.List<APIUser>]
     */
    @GET("users")
    suspend fun getUsers(@Query("lastId") lastId: kotlin.String? = null, @Query("lastCallTime") lastCallTime: kotlin.Int? = null, @Query("updateType") updateType: kotlin.String? = null): Response<kotlin.collections.List<APIUser>>

    /**
     * 
     * Get current user
     * Responses:
     *  - 200: The user
     *
     * @return [APIUser]
     */
    @GET("me")
    suspend fun me(): Response<APIUser>

    /**
     * Refresh auth token
     * Refresh auth token
     * Responses:
     *  - 200: Refreshed token
     *
     * @param uid the user&#39;s id
     * @return [Token]
     */
    @GET("token/refresh")
    suspend fun refreshAuthToken(@Path("uid") uid: kotlin.String): Response<Token>

    /**
     * reset notification badge count
     * reset badge count
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @return [Unit]
     */
    @GET("resetBadgeCount")
    suspend fun resetBadgeCount(): Response<Unit>

    /**
     * Sync Contacts
     * Sync contacts
     * Responses:
     *  - 200: Contact users
     *
     * @param syncContactsInput  (optional)
     * @return [kotlin.collections.List<APIUser>]
     */
    @POST("contacts/sync")
    suspend fun syncContacts(@Body syncContactsInput: SyncContactsInput? = null): Response<kotlin.collections.List<APIUser>>

    /**
     * Unblock a user
     * Unblock a user
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param uid the user&#39;s id
     * @return [Unit]
     */
    @DELETE("blocks/{uid}")
    suspend fun unblockUser(@Path("uid") uid: kotlin.String): Response<Unit>

    /**
     * 
     * Update current user
     * Responses:
     *  - 200: The User
     *
     * @param updateUserInput User properties to update with
     * @return [APIUser]
     */
    @POST("me")
    suspend fun updateMe(@Body updateUserInput: UpdateUserInput): Response<APIUser>

}
