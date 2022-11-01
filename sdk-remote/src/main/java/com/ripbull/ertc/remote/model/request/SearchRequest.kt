package com.ripbull.ertc.remote.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Sagar on 16/12/2020.
 */
data class SearchRequest(
  @SerializedName("searchQuery") @Expose val searchQuery: SearchQuery,
  @SerializedName("resultCategories") @Expose val resultCategories: List<String>
)

data class SearchQuery(
  @SerializedName("text") @Expose var text: String,
  @SerializedName("threadId") @Expose var threadId: String? = null
)