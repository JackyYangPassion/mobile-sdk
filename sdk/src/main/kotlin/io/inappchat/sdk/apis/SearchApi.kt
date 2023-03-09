package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.SearchInput
import io.inappchat.sdk.models.SearchResults

interface SearchApi {
    /**
     * Unified search API
     * One stop for all search APIs, can be used to search files, messages or groups.
     * Responses:
     *  - 200: The search results
     *
     * @param searchInput Chat multiple request
     * @param skip skip value for pagination. i.e. index. default 0 (optional, default to 0)
     * @param limit limit value for pagination. i.e. page-size. default 10 (optional, default to 20)
     * @return [SearchResults]
     */
    @POST("search")
    suspend fun search(@Body searchInput: SearchInput, @Query("skip") skip: kotlin.Int? = 0, @Query("limit") limit: kotlin.Int? = 20): Response<SearchResults>

}
