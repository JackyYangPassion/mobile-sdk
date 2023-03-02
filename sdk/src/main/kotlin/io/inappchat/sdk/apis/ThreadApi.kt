package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.APIThread
import io.inappchat.sdk.models.UpdateThreadInput

interface ThreadApi {
    /**
     * Thread Creation API
     * Get or Create Thread request before starting chat session with any user. This API is applicable for only one 2 one chat.
     * Responses:
     *  - 200: Thread data
     *
     * @param uid the user&#39;s id
     * @return [APIThread]
     */
    @POST("user/{uid}/thread")
    suspend fun createThread(@Path("uid") uid: kotlin.String): Response<APIThread>

    /**
     * 
     * Get a thread belonging to a group
     * Responses:
     *  - 200: The thread
     *
     * @param gid Group ID
     * @return [APIThread]
     */
    @GET("group/{gid}/thread")
    suspend fun getGroupThread(@Path("gid") gid: kotlin.String): Response<APIThread>

    /**
     * Thread Get API
     * Get any existing thread
     * Responses:
     *  - 200: Thread data
     *
     * @param tid The Thread ID
     * @return [APIThread]
     */
    @GET("thread/{tid}")
    suspend fun getThread(@Path("tid") tid: kotlin.String): Response<APIThread>


    /**
    * enum for parameter threadType
    */
    enum class ThreadType_getThreads(val value: kotlin.String) {
        @Json(name = "single") single("single"),
        @Json(name = "group") group("group")
    }

    /**
     * Load thread history
     * Load thread history
     * Responses:
     *  - 200: Thread history response
     *
     * @param skip skip value for pagination. i.e. index. default 0 (optional, default to 0)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional, default to 20)
     * @param threadType threadType in-case specific type threads are needed. Don&#39;t provide this field if all threads to be returned in unified way. (optional)
     * @return [kotlin.collections.List<APIThread>]
     */
    @GET("threads")
    suspend fun getThreads(@Query("skip") skip: kotlin.Int? = 0, @Query("limit") limit: kotlin.Int? = 20, @Query("threadType") threadType: ThreadType_getThreads? = null): Response<kotlin.collections.List<APIThread>>

    /**
     * Thread Update API
     * Update any existing thread
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param tid The Thread ID
     * @param updateThreadInput Thread settings
     * @return [Unit]
     */
    @PUT("thread/{tid}")
    suspend fun updateThread(@Path("tid") tid: kotlin.String, @Body updateThreadInput: UpdateThreadInput): Response<Unit>

}
