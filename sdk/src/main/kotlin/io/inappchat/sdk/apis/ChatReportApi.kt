package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.CreateChatReport
import io.inappchat.sdk.models.Report
import io.inappchat.sdk.models.ReportCategory
import io.inappchat.sdk.models.ReportStatus

interface ChatReportApi {
    /**
     * 
     * Approve Chat Report Action.
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param chatReportId chat Report ID
     * @return [Unit]
     */
    @PUT("reports/{chatReportId}/approve")
    suspend fun approveChatReport(@Path("chatReportId") chatReportId: kotlin.String): Response<Unit>

    /**
     * 
     * Create Chat Report.
     * Responses:
     *  - 200: The report
     *
     * @param mid The message ID
     * @param createChatReport 
     * @return [Report]
     */
    @POST("message/{mid}/report")
    suspend fun createChatReport(@Path("mid") mid: kotlin.String, @Body createChatReport: CreateChatReport): Response<Report>

    /**
     * Delete Chat Report
     * Delete Chat Report.
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param chatReportId chat Report ID
     * @return [Unit]
     */
    @DELETE("reports/{chatReportId}")
    suspend fun deleteChatReportDelete(@Path("chatReportId") chatReportId: kotlin.String): Response<Unit>

    /**
     * Get Chat Report Details
     * Get Chat Report Details.
     * Responses:
     *  - 200: Update Chat Report Response
     *
     * @param chatReportId chat Report ID
     * @return [Report]
     */
    @GET("reports/{chatReportId}")
    suspend fun getChatReport(@Path("chatReportId") chatReportId: kotlin.String): Response<Report>

    /**
     * Get Chat Report List
     * Get Chat Report List.
     * Responses:
     *  - 200: Get Chat Report List Response
     *
     * @param uid the user&#39;s id
     * @param skip skip value for pagination. i.e. index. default 0 (optional, default to 0)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional, default to 20)
     * @param threadId thread ID to filter chat Reports (optional)
     * @param category  (optional)
     * @param status  (optional)
     * @param msgType chat report msgType to filter chat Reports(Possible values : text, image, audio, video, file, gif, location, contact, sticker, gify) (optional)
     * @return [kotlin.collections.List<Report>]
     */
    @GET("reports")
    suspend fun getChatReportList(@Path("uid") uid: kotlin.String, @Query("skip") skip: kotlin.Int? = 0, @Query("limit") limit: kotlin.Int? = 20, @Query("threadId") threadId: kotlin.String? = null, @Query("category") category: ReportCategory? = null, @Query("status") status: ReportStatus? = null, @Query("msgType") msgType: kotlin.String? = null): Response<kotlin.collections.List<Report>>

    /**
     * 
     * Ignore Chat Report Action.
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param chatReportId chat Report ID
     * @return [Unit]
     */
    @PUT("reports/{chatReportId}/ignore")
    suspend fun ignoreChatReport(@Path("chatReportId") chatReportId: kotlin.String): Response<Unit>

    /**
     * 
     * Update Chat Report.
     * Responses:
     *  - 204: Operation comlpeted successfully
     *
     * @param chatReportId chat Report ID
     * @param createChatReport 
     * @return [Unit]
     */
    @PUT("reports/{chatReportId}")
    suspend fun updateChatReport(@Path("chatReportId") chatReportId: kotlin.String, @Body createChatReport: CreateChatReport): Response<Unit>

}
